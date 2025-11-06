package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entities.Decoration;
import types.DecorationType;
import world.Farm;
import world.Tile;
import world.TileType;

/**
 * Gerencia todas as decorações do mapa
 * Responsável por geração, renderização e colisão
 */
public class DecorationManager {
    private List<Decoration> decorations;
    private Random random;
    
    public DecorationManager() {
        this.decorations = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Gera decorações aleatórias no mapa
     */
    public void generateDecorations(Farm farm) {
        int width = farm.getWidth();
        int height = farm.getHeight();
        
        // Limpar decorações existentes
        decorations.clear();
        
        // Gerar árvores (esparsas)
        generateTrees(farm, width, height, 15); // 15 árvores
        
        // Gerar arbustos (mais densos)
        generateBushes(farm, width, height, 30); // 30 arbustos
        
        // Gerar algumas ruínas (raras)
        generateRuins(farm, width, height, 3); // 3 ruínas
        
        System.out.println("✓ Decorações geradas: " + decorations.size() + " objetos");
    }
    
    /**
     * Gera árvores aleatórias
     */
    private void generateTrees(Farm farm, int width, int height, int count) {
        int attempts = 0;
        int maxAttempts = count * 10;
        
        while (decorations.stream().filter(d -> d.getLayer() == 3).count() < count && attempts < maxAttempts) {
            attempts++;
            
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            // Obter tipo da árvore antes de verificar posição
            DecorationType treeType = DecorationType.getRandomTree();
            
            // Verificar se a posição é válida (incluindo toda a área ocupada)
            if (isValidPosition(farm, x, y, false, treeType)) {
                decorations.add(new Decoration(x, y, treeType));
            }
        }
    }
    
    /**
     * Gera arbustos aleatórios
     */
    private void generateBushes(Farm farm, int width, int height, int count) {
        int attempts = 0;
        int maxAttempts = count * 10;
        
        while (decorations.stream().filter(d -> d.getLayer() == 1).count() < count && attempts < maxAttempts) {
            attempts++;
            
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            // Obter tipo do arbusto antes de verificar posição
            DecorationType bushType = DecorationType.getRandomBush();
            
            // Verificar se a posição é válida (incluindo toda a área ocupada)
            if (isValidPosition(farm, x, y, true, bushType)) {
                decorations.add(new Decoration(x, y, bushType));
            }
        }
    }
    
    /**
     * Gera ruínas aleatórias
     */
    private void generateRuins(Farm farm, int width, int height, int count) {
        int attempts = 0;
        int maxAttempts = count * 20;
        
        while (decorations.stream().filter(d -> d.getType().getSpriteFile().contains("ruins")).count() < count && attempts < maxAttempts) {
            attempts++;
            
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            // Obter tipo da ruína antes de verificar posição
            DecorationType ruinType = DecorationType.getRandomRuin();
            
            // Verificar se a posição é válida (incluindo toda a área ocupada)
            if (isValidPosition(farm, x, y, false, ruinType)) {
                decorations.add(new Decoration(x, y, ruinType));
            }
        }
    }
    
    /**
     * Verifica se uma posição é válida para colocar uma decoração
     * @param decoType Tipo da decoração (para calcular área ocupada)
     */
    private boolean isValidPosition(Farm farm, int x, int y, boolean canBeOnDirt, DecorationType decoType) {
        // Tamanho do tile em pixels (deve corresponder ao TILE_SIZE do GameWindow)
        final int TILE_SIZE = 60;
        
        // Calcular quantos tiles a decoração ocupa
        int widthInTiles = (int) Math.ceil((double) decoType.getWidth() / TILE_SIZE);
        int heightInTiles = (int) Math.ceil((double) decoType.getHeight() / TILE_SIZE);
        
        // Verificar se toda a área cabe no mapa
        if (x < 2 || x + widthInTiles >= farm.getWidth() - 2 || 
            y < 2 || y + heightInTiles >= farm.getHeight() - 2) {
            return false;
        }
        
        // Não colocar perto do spawn do player (centro do mapa)
        int centerX = farm.getWidth() / 2;
        int centerY = farm.getHeight() / 2;
        double distanceFromCenter = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        if (distanceFromCenter < 5) {
            return false;
        }
        
        // Verificar se todos os tiles da área ocupada são válidos
        for (int dx = 0; dx < widthInTiles; dx++) {
            for (int dy = 0; dy < heightInTiles; dy++) {
                int checkX = x + dx;
                int checkY = y + dy;
                
                // Verificar limites do mapa
                if (checkX >= farm.getWidth() || checkY >= farm.getHeight()) {
                    return false;
                }
                
                Tile tile = farm.getTile(checkX, checkY);
                
                // Não colocar na água
                if (tile.getType() == TileType.WATER) {
                    return false;
                }
                
                // Verificar se já existe uma decoração nesta posição
                for (Decoration deco : decorations) {
                    if (deco.collidesWith(checkX, checkY)) {
                        return false;
                    }
                }
                
                // Árvores e ruínas só em grass (verificar cada tile)
                if (!canBeOnDirt && tile.getType() != TileType.GRASS) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Retorna todas as decorações de uma layer específica
     */
    public List<Decoration> getDecorationsByLayer(int layer) {
        List<Decoration> result = new ArrayList<>();
        for (Decoration deco : decorations) {
            if (deco.getLayer() == layer) {
                result.add(deco);
            }
        }
        return result;
    }
    
    /**
     * Verifica se uma posição pode ser andada (considerando decorações)
     */
    public boolean isPositionWalkable(int x, int y) {
        for (Decoration deco : decorations) {
            if (deco.collidesWith(x, y) && !deco.isWalkable()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retorna todas as decorações
     */
    public List<Decoration> getAllDecorations() {
        return new ArrayList<>(decorations);
    }
    
    /**
     * Retorna a quantidade de decorações
     */
    public int getCount() {
        return decorations.size();
    }
}

