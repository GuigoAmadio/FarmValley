package systems;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import world.Farm;
import world.Tile;
import world.TileType;
import entities.Player;
import entities.NPC;
import entities.Enemy;

import java.util.List;

/**
 * Sistema de minimapa
 * Mostra visão geral do mapa, jogador, NPCs e inimigos
 */
public class Minimap {
    private static final int SIZE = 120;
    private static final int PADDING = 10;
    private double scale;
    
    public Minimap() {
        this.scale = 1.0;
    }
    
    /**
     * Renderiza o minimapa
     */
    public void render(GraphicsContext gc, Farm farm, Player player, 
                       List<NPC> npcs, List<Enemy> enemies,
                       int screenWidth, int screenHeight) {
        
        int mapWidth = farm.getWidth();
        int mapHeight = farm.getHeight();
        
        // Calcular escala para caber no minimapa
        scale = Math.min((double) SIZE / mapWidth, (double) SIZE / mapHeight);
        
        // Posição do minimapa (canto superior direito)
        double x = screenWidth - SIZE - PADDING;
        double y = PADDING;
        
        // Fundo do minimapa
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRoundRect(x - 5, y - 5, SIZE + 10, SIZE + 30, 10, 10);
        
        // Borda
        gc.setStroke(Color.rgb(100, 100, 100));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x - 5, y - 5, SIZE + 10, SIZE + 30, 10, 10);
        
        // Título
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 10));
        gc.fillText("MAPA", x + SIZE/2 - 15, y + SIZE + 15);
        
        // Calcular área visível no minimapa (centrada no jogador)
        int viewRadius = 30; // Raio de tiles visíveis
        int playerX = player.getX();
        int playerY = player.getY();
        
        int startX = Math.max(0, playerX - viewRadius);
        int endX = Math.min(mapWidth, playerX + viewRadius);
        int startY = Math.max(0, playerY - viewRadius);
        int endY = Math.min(mapHeight, playerY + viewRadius);
        
        // Recalcular escala para a área visível
        int visibleWidth = endX - startX;
        int visibleHeight = endY - startY;
        scale = Math.min((double) SIZE / visibleWidth, (double) SIZE / visibleHeight);
        
        // Desenhar tiles
        for (int tx = startX; tx < endX; tx++) {
            for (int ty = startY; ty < endY; ty++) {
                Tile tile = farm.getTile(tx, ty);
                if (tile != null) {
                    Color color = getTileColor(tile.getType());
                    
                    double pixelX = x + (tx - startX) * scale;
                    double pixelY = y + (ty - startY) * scale;
                    
                    gc.setFill(color);
                    gc.fillRect(pixelX, pixelY, Math.max(1, scale), Math.max(1, scale));
                }
            }
        }
        
        // Desenhar NPCs (azul)
        gc.setFill(Color.rgb(0, 150, 255));
        for (NPC npc : npcs) {
            if (npc.getX() >= startX && npc.getX() < endX &&
                npc.getY() >= startY && npc.getY() < endY) {
                double npcX = x + (npc.getX() - startX) * scale;
                double npcY = y + (npc.getY() - startY) * scale;
                gc.fillOval(npcX - 2, npcY - 2, 4, 4);
            }
        }
        
        // Desenhar inimigos (vermelho)
        gc.setFill(Color.rgb(255, 50, 50));
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() &&
                enemy.getX() >= startX && enemy.getX() < endX &&
                enemy.getY() >= startY && enemy.getY() < endY) {
                double enemyX = x + (enemy.getX() - startX) * scale;
                double enemyY = y + (enemy.getY() - startY) * scale;
                gc.fillRect(enemyX - 2, enemyY - 2, 4, 4);
            }
        }
        
        // Desenhar jogador (verde brilhante, maior)
        double playerMinimapX = x + (playerX - startX) * scale;
        double playerMinimapY = y + (playerY - startY) * scale;
        
        // Círculo externo pulsante
        double pulse = 1 + 0.3 * Math.sin(System.currentTimeMillis() / 200.0);
        gc.setFill(Color.rgb(0, 255, 0, 0.3));
        gc.fillOval(playerMinimapX - 5 * pulse, playerMinimapY - 5 * pulse, 10 * pulse, 10 * pulse);
        
        // Círculo interno
        gc.setFill(Color.rgb(0, 255, 0));
        gc.fillOval(playerMinimapX - 3, playerMinimapY - 3, 6, 6);
        
        // Seta de direção
        drawDirectionArrow(gc, playerMinimapX, playerMinimapY, player.getFacing());
        
        // Coordenadas do jogador
        gc.setFill(Color.rgb(200, 200, 200));
        gc.setFont(javafx.scene.text.Font.font("Arial", 8));
        gc.fillText("(" + playerX + ", " + playerY + ")", x, y + SIZE + 25);
    }
    
    private Color getTileColor(TileType type) {
        switch (type) {
            case GRASS:
                return Color.rgb(60, 120, 60);
            case DIRT:
            case PLANTED:
                return Color.rgb(100, 70, 40);
            case WATER:
                return Color.rgb(50, 100, 180);
            case STONE:
                return Color.rgb(100, 100, 100);
            default:
                return Color.rgb(80, 80, 80);
        }
    }
    
    private void drawDirectionArrow(GraphicsContext gc, double x, double y, Player.Direction facing) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        
        double arrowLength = 8;
        double endX = x, endY = y;
        
        switch (facing) {
            case UP: endY = y - arrowLength; break;
            case DOWN: endY = y + arrowLength; break;
            case LEFT: endX = x - arrowLength; break;
            case RIGHT: endX = x + arrowLength; break;
        }
        
        gc.strokeLine(x, y, endX, endY);
    }
}

