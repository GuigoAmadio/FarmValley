package core;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.shape.ArcType;

import entities.Player;
import entities.Decoration;
import entities.Crop;
import world.Farm;
import world.Tile;
import world.TileType;
import world.TimeSystem;
import types.CropType;
import systems.DecorationManager;
import systems.UIManager;
import systems.HarvestSystem;
import systems.Inventory;
import utils.SpriteLoader;

public class GameWindow extends Application {
    private static final int TILE_SIZE = 60; // ZOOM: Tiles maiores para melhor visibilidade
    private static final int CANVAS_WIDTH = 900; // Ajustado para nova escala
    private static final int CANVAS_HEIGHT = 720; // Ajustado para nova escala

    private Canvas canvas;
    private GraphicsContext gc;
    private GameEngine engine;
    private VBox hudBox;

    @Override
    public void start(Stage primaryStage) {
        engine = new GameEngine();

        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        
        // HUD no topo
        hudBox = new VBox(8);
        hudBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1a1a2e 0%, #16213e 100%);" +
            "-fx-padding: 15;" +
            "-fx-border-color: #0f3460;" +
            "-fx-border-width: 0 0 3 0;"
        );
        root.setTop(hudBox);

        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT + 140);
        
        // Input handling
        scene.setOnKeyPressed(e -> engine.keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> engine.keyReleased(e.getCode()));

        primaryStage.setTitle("Farm Valley - Jogo de Fazenda");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Game loop
        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Atualizar a cada 16ms (~60 FPS)
                if (now - lastUpdate >= 16_000_000) {
                    engine.update();
                    render();
                    updateHUD();
                    lastUpdate = now;
                }
            }
        }.start();
    }

    // Obter sprite do player baseado na direção e frame de animação
    private Image getPlayerSprite(Player player) {
        String spriteName = null;
        Player.Direction facing = player.getFacing();
        int frame = player.getAnimationFrame();
        
        // Carregar sprite animado (6 frames!) com caminho correto
        switch (facing) {
            case DOWN:
                spriteName = "player/player_down_" + frame + ".png";
                break;
            case UP:
                spriteName = "player/player_up_" + frame + ".png";
                break;
            case LEFT:
                spriteName = "player/player_left_" + frame + ".png";
                break;
            case RIGHT:
                spriteName = "player/player_right_" + frame + ".png";
                break;
        }
        
        Image sprite = SpriteLoader.loadSprite(spriteName);
        if (sprite != null) {
            return sprite;
        }
        
        // Fallback final: sprite padrão
        return SpriteLoader.loadSprite(SpriteLoader.Sprites.PLAYER);
    }

    private void render() {
        // Fundo gradiente
        gc.setFill(Color.rgb(20, 30, 48));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        Farm farm = engine.getFarm();
        Player player = engine.getPlayer();

        // Calcular offset da câmera (centralizar no jogador)
        int offsetX = CANVAS_WIDTH / 2 - player.getX() * TILE_SIZE - TILE_SIZE / 2;
        int offsetY = CANVAS_HEIGHT / 2 - player.getY() * TILE_SIZE - TILE_SIZE / 2;

        // Otimização: Calcular viewport (área visível na tela)
        int tilesOnScreenX = (CANVAS_WIDTH / TILE_SIZE) + 2; // +2 para margem
        int tilesOnScreenY = (CANVAS_HEIGHT / TILE_SIZE) + 2;
        
        // Calcular tiles visíveis baseado na posição do jogador
        int playerTileX = player.getX();
        int playerTileY = player.getY();
        
        int startX = Math.max(0, playerTileX - tilesOnScreenX / 2);
        int endX = Math.min(farm.getWidth(), playerTileX + tilesOnScreenX / 2);
        int startY = Math.max(0, playerTileY - tilesOnScreenY / 2);
        int endY = Math.min(farm.getHeight(), playerTileY + tilesOnScreenY / 2);
        
        // LAYER 0: Desenhar APENAS tiles visíveis (View Frustum Culling)
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = farm.getTile(x, y);
                if (tile != null) {
                    int screenX = x * TILE_SIZE + offsetX;
                    int screenY = y * TILE_SIZE + offsetY;
                    
                    // Verificar se está dentro da tela (otimização extra)
                    if (screenX > -TILE_SIZE && screenX < CANVAS_WIDTH + TILE_SIZE &&
                        screenY > -TILE_SIZE && screenY < CANVAS_HEIGHT + TILE_SIZE) {
                        drawTileBase(tile, screenX, screenY);
                    }
                }
            }
        }
        
        // Agora desenhar transições suaves apenas para tiles visíveis
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = farm.getTile(x, y);
                if (tile != null) {
                    int screenX = x * TILE_SIZE + offsetX;
                    int screenY = y * TILE_SIZE + offsetY;
                    
                    if (screenX > -TILE_SIZE && screenX < CANVAS_WIDTH + TILE_SIZE &&
                        screenY > -TILE_SIZE && screenY < CANVAS_HEIGHT + TILE_SIZE) {
                        drawTileTransitions(farm, tile, x, y, screenX, screenY);
                    }
                }
            }
        }
        
        // LAYER 1: Decorações baixas (arbustos) - Abaixo do player
        DecorationManager decorManager = farm.getDecorationManager();
        for (Decoration deco : decorManager.getDecorationsByLayer(1)) {
            drawDecoration(deco, offsetX, offsetY);
        }

        // LAYER 2: Desenhar jogador
        int playerScreenX = player.getX() * TILE_SIZE + offsetX;
        int playerScreenY = player.getY() * TILE_SIZE + offsetY;
        
        // Tentar carregar sprite animado baseado na direção
        Image playerSprite = getPlayerSprite(player);
        
        if (playerSprite != null) {
            // Desenhar sprite customizado 64x64 (quadrado)
            int spriteSize = (int)(TILE_SIZE * 1.07);  // 64px
            int spriteOffsetX = (TILE_SIZE - spriteSize) / 2;
            int spriteOffsetY = (TILE_SIZE - spriteSize) / 2;
            gc.drawImage(playerSprite, playerScreenX + spriteOffsetX, playerScreenY + spriteOffsetY, spriteSize, spriteSize);
        } else {
            // Fallback: desenhar jogador padrão (círculo amarelo)
            // Sombra
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillOval(playerScreenX + 6, playerScreenY + TILE_SIZE - 6, TILE_SIZE - 12, 6);
            
            // Corpo do jogador
            gc.setFill(Color.rgb(255, 220, 100));
            gc.fillOval(playerScreenX + 8, playerScreenY + 8, TILE_SIZE - 16, TILE_SIZE - 16);
            
            // Olhos
            gc.setFill(Color.BLACK);
            gc.fillOval(playerScreenX + 14, playerScreenY + 16, 4, 4);
            gc.fillOval(playerScreenX + 22, playerScreenY + 16, 4, 4);
            
            // Sorriso
            gc.strokeArc(playerScreenX + 14, playerScreenY + 18, 12, 8, 180, 180, javafx.scene.shape.ArcType.OPEN);
        }
        
        // LAYER 3: Decorações altas (árvores, ruínas) - Sobre o player
        for (Decoration deco : decorManager.getDecorationsByLayer(3)) {
            drawDecoration(deco, offsetX, offsetY);
        }
        
        // Mensagem de status (se houver)
        String message = engine.getStatusMessage();
        if (!message.isEmpty()) {
            drawStatusMessage(message);
        }
        
        // Indicador visual do tile sob o jogador
        drawTileHighlight(playerScreenX, playerScreenY);
        
        // HOT BAR sempre visível
        UIManager uiManager = engine.getUIManager();
        uiManager.renderHotBar(gc, player.getInventory(), CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // Renderizar inventário completo (se aberto)
        if (uiManager.isInventoryOpen()) {
            uiManager.renderInventory(gc, player.getInventory(), CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }
    
    private void drawStatusMessage(String message) {
        double msgWidth = 450;
        double msgHeight = 55;
        double msgX = (CANVAS_WIDTH - msgWidth) / 2;
        double msgY = CANVAS_HEIGHT - 90;
        
        // Sombra
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(msgX + 3, msgY + 3, msgWidth, msgHeight, 12, 12);
        
        // Fundo da mensagem com gradiente simulado
        gc.setFill(Color.rgb(20, 20, 25, 0.95));
        gc.fillRoundRect(msgX, msgY, msgWidth, msgHeight, 10, 10);
        
        // Borda externa
        gc.setStroke(Color.rgb(255, 215, 0));
        gc.setLineWidth(3);
        gc.strokeRoundRect(msgX, msgY, msgWidth, msgHeight, 10, 10);
        
        // Borda interna brilhante
        gc.setStroke(Color.rgb(255, 235, 150, 0.6));
        gc.setLineWidth(1);
        gc.strokeRoundRect(msgX + 2, msgY + 2, msgWidth - 4, msgHeight - 4, 8, 8);
        
        // Brilho superior
        gc.setFill(Color.rgb(255, 215, 0, 0.1));
        gc.fillRoundRect(msgX, msgY, msgWidth, msgHeight / 3, 10, 10);
        
        // Texto com sombra
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(message, CANVAS_WIDTH / 2 + 1, msgY + msgHeight / 2 + 1);
        
        // Texto principal
        gc.setFill(Color.rgb(255, 255, 255));
        gc.fillText(message, CANVAS_WIDTH / 2, msgY + msgHeight / 2);
    }
    
    private void drawDecoration(Decoration deco, int offsetX, int offsetY) {
        Image sprite = deco.getSprite();
        
        int x = deco.getX() * TILE_SIZE + offsetX;
        int y = deco.getY() * TILE_SIZE + offsetY;
        int width = deco.getWidth();
        int height = deco.getHeight();
        
        // Centralizar decoração no tile
        int centerOffsetX = (TILE_SIZE - width) / 2;
        int centerOffsetY = TILE_SIZE - height; // Alinhar parte inferior com o tile
        
        // Desenhar sprite (se existir e não estiver depletado)
        if (sprite != null && !deco.isDepleted()) {
            // Efeito visual de fade quando quase quebrado
            double opacity = 1.0;
            if (deco.canHarvest() && deco.getHealth() < deco.getMaxHealth()) {
                opacity = 0.6 + (0.4 * deco.getHealth() / deco.getMaxHealth());
            }
            
            gc.setGlobalAlpha(opacity);
            gc.drawImage(sprite, x + centerOffsetX, y + centerOffsetY, width, height);
            gc.setGlobalAlpha(1.0);
            
            // Barra de progresso se estiver sendo coletado
            if (deco.canHarvest() && deco.getHealth() < deco.getMaxHealth()) {
                drawHealthBar(x, y - 5, TILE_SIZE, deco.getHealth(), deco.getMaxHealth());
            }
        } else if (deco.isDepleted()) {
            // Desenhar "toco" ou resíduo visual
            gc.setFill(Color.rgb(101, 67, 33, 0.5));
            gc.fillOval(x + TILE_SIZE/4, y + TILE_SIZE - 10, TILE_SIZE/2, 8);
        }
    }
    
    /**
     * Desenha barra de vida/progresso
     */
    private void drawHealthBar(int x, int y, int width, int current, int max) {
        int barWidth = width - 10;
        int barHeight = 6;
        int barX = x + 5;
        
        // Fundo da barra
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRoundRect(barX, y, barWidth, barHeight, 3, 3);
        
        // Barra de progresso
        double progress = (double)current / max;
        Color barColor = progress > 0.5 ? Color.rgb(76, 175, 80) : 
                        progress > 0.25 ? Color.rgb(255, 193, 7) : 
                        Color.rgb(244, 67, 54);
        
        gc.setFill(barColor);
        gc.fillRoundRect(barX + 1, y + 1, (barWidth - 2) * progress, barHeight - 2, 2, 2);
        
        // Borda
        gc.setStroke(Color.rgb(255, 255, 255, 0.8));
        gc.setLineWidth(1);
        gc.strokeRoundRect(barX, y, barWidth, barHeight, 3, 3);
    }

    /**
     * Desenha a base do tile sem bordas
     */
    private void drawTileBase(Tile tile, int x, int y) {
        TileType type = tile.getType();
        
        // Tentar carregar sprite do tile
        String spriteFile = null;
        switch (type) {
            case GRASS:
                spriteFile = SpriteLoader.Sprites.GRASS;
                break;
            case DIRT:
            case PLANTED:
                spriteFile = SpriteLoader.Sprites.DIRT;
                break;
            case WATER:
                spriteFile = SpriteLoader.Sprites.WATER;
                break;
            case STONE:
                spriteFile = SpriteLoader.Sprites.STONE;
                break;
        }
        
        Image tileSprite = spriteFile != null ? SpriteLoader.loadSprite(spriteFile) : null;
        
        if (tileSprite != null) {
            // Desenhar sprite do tile SEM bordas
            gc.drawImage(tileSprite, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            // Fallback: desenhar tile com cores e texturas procedurais
            Color baseColor = Color.rgb(type.getRed(), type.getGreen(), type.getBlue());
            gc.setFill(baseColor);
            gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            
            // Adicionar textura dependendo do tipo
            if (type == TileType.GRASS) {
                // Textura de grama aleatória
                for (int i = 0; i < 8; i++) {
                    int tx = x + (int)(Math.random() * TILE_SIZE);
                    int ty = y + (int)(Math.random() * TILE_SIZE);
                    gc.setFill(Color.rgb(0, 180, 0, 0.2));
                    gc.fillRect(tx, ty, 2, 2);
                }
            } else if (type == TileType.DIRT || type == TileType.PLANTED) {
                // Linhas de terra arada
                gc.setStroke(Color.rgb(100, 60, 30, 0.4));
                gc.setLineWidth(1);
                for (int i = 0; i < 3; i++) {
                    gc.strokeLine(x + 5, y + 10 + i * 15, x + TILE_SIZE - 5, y + 10 + i * 15);
                }
            } else if (type == TileType.WATER) {
                // Ondas na água
                gc.setFill(Color.rgb(100, 200, 255, 0.3));
                gc.fillOval(x + 5, y + 5, 10, 6);
                gc.fillOval(x + 35, y + 20, 15, 8);
                gc.fillOval(x + 20, y + 45, 12, 7);
            }
        }

        // Desenhar planta se houver (sobre o tile base)
        if (tile.hasCrop()) {
            Crop crop = tile.getCrop();
            CropType cropType = crop.getType();
            double progress = crop.getGrowthProgress();

            // Cor da planta fica mais intensa conforme cresce
            Color plantColor = Color.rgb(
                cropType.getRed(), 
                cropType.getGreen(), 
                cropType.getBlue(), 
                Math.min(1.0, 0.3 + progress * 0.7)
            );
            
            // Tamanho da planta aumenta
            int plantSize = (int)(TILE_SIZE * 0.7 * progress);
            int plantOffset = (TILE_SIZE - plantSize) / 2;
            
            // Sombra da planta
            gc.setFill(Color.rgb(0, 0, 0, 0.2));
            gc.fillOval(x + plantOffset, y + plantOffset + 2, plantSize, plantSize / 2);
            
            // Planta
            gc.setFill(plantColor);
            gc.fillOval(x + plantOffset, y + plantOffset, plantSize, plantSize);
            
            // Detalhes da planta (folhas)
            if (progress > 0.3) {
                gc.setFill(plantColor.darker());
                gc.fillOval(x + plantOffset + 2, y + plantOffset + 2, plantSize / 3, plantSize / 3);
                gc.fillOval(x + plantOffset + plantSize - plantSize / 3 - 2, y + plantOffset + 2, plantSize / 3, plantSize / 3);
            }

            // Indicador de planta madura (estrela dourada)
            if (crop.isFullyGrown()) {
                gc.setFill(Color.GOLD);
                gc.fillOval(x + TILE_SIZE - 12, y + 4, 8, 8);
                gc.setFill(Color.rgb(255, 255, 0));
                gc.fillOval(x + TILE_SIZE - 10, y + 6, 4, 4);
            }
        }
    }

    private void updateHUD() {
        hudBox.getChildren().clear();

        Player player = engine.getPlayer();
        TimeSystem time = engine.getTimeSystem();
        
        // Container horizontal para informações principais
        javafx.scene.layout.HBox infoBox = new javafx.scene.layout.HBox(15);
        infoBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Título
        javafx.scene.text.Text title = new javafx.scene.text.Text("🌾 FARM VALLEY 🌾");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setFill(Color.rgb(255, 215, 0));
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 2);");
        
        // Card de Data
        javafx.scene.layout.VBox dateCard = createInfoCard("📅", "DATA", time.getDateString(), Color.rgb(70, 130, 180));
        
        // Card de Dinheiro
        javafx.scene.layout.VBox moneyCard = createInfoCard("💰", "DINHEIRO", "$" + player.getMoney(), Color.rgb(255, 215, 0));
        
        // Card de Sementes
        javafx.scene.layout.VBox seedCard = createInfoCard("🌱", "SEMENTE", 
            player.getSelectedSeed().getName() + " ($" + player.getSelectedSeed().getSeedCost() + ")", 
            Color.rgb(34, 139, 34));
        
        // Barra de energia visual melhorada
        javafx.scene.canvas.Canvas energyBar = createEnergyBar(player);
        
        infoBox.getChildren().addAll(dateCard, moneyCard, seedCard, energyBar);
        
        // Controles
        javafx.scene.text.Text controls = new javafx.scene.text.Text(
            "⌨️  WASD: Mover  |  E/Space: Coletar  |  I: Inventário  |  1-6: Seleção Rápida  |  Z: Dormir"
        );
        controls.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        controls.setFill(Color.rgb(180, 180, 180));
        controls.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 1, 0, 0, 1);");
        
        // Adicionar tudo ao HUD
        hudBox.getChildren().addAll(title, infoBox, controls);
    }
    
    /**
     * Cria um card de informação estilizado
     */
    private javafx.scene.layout.VBox createInfoCard(String icon, String label, String value, Color accentColor) {
        javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(4);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle(
            "-fx-background-color: rgba(30, 30, 30, 0.9);" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 15;" +
            "-fx-border-color: " + toHexColor(accentColor) + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2);"
        );
        
        // Ícone e label
        javafx.scene.text.Text iconText = new javafx.scene.text.Text(icon + " " + label);
        iconText.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        iconText.setFill(Color.rgb(200, 200, 200));
        
        // Valor
        javafx.scene.text.Text valueText = new javafx.scene.text.Text(value);
        valueText.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        valueText.setFill(accentColor);
        
        card.getChildren().addAll(iconText, valueText);
        return card;
    }
    
    /**
     * Converte Color para string hex para CSS
     */
    private String toHexColor(Color color) {
        return String.format("#%02X%02X%02X",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255));
    }
    
    /**
     * Cria barra de energia melhorada com gradiente e efeitos
     */
    private javafx.scene.canvas.Canvas createEnergyBar(Player player) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(280, 50);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        
        double energyPercent = (double) player.getEnergy() / player.getMaxEnergy();
        
        // Fundo com sombra
        gc.setFill(Color.rgb(20, 20, 20, 0.9));
        gc.fillRoundRect(3, 3, 280, 44, 12, 12);
        
        // Fundo da barra
        gc.setFill(Color.rgb(40, 40, 40));
        gc.fillRoundRect(8, 8, 270, 34, 10, 10);
        
        // Borda da barra
        gc.setStroke(Color.rgb(60, 60, 60));
        gc.setLineWidth(2);
        gc.strokeRoundRect(8, 8, 270, 34, 10, 10);
        
        // Barra de energia com gradiente simulado
        double barWidth = 266 * energyPercent;
        if (barWidth > 0) {
            Color energyColor = energyPercent > 0.5 ? Color.rgb(76, 175, 80) : 
                               energyPercent > 0.25 ? Color.rgb(255, 193, 7) : 
                               Color.rgb(244, 67, 54);
            
            // Gradiente simples (cor principal)
            gc.setFill(energyColor);
            gc.fillRoundRect(10, 10, barWidth, 30, 8, 8);
            
            // Brilho superior
            gc.setFill(Color.rgb(255, 255, 255, 0.3));
            gc.fillRoundRect(10, 10, barWidth, 8, 8, 8);
        }
        
        // Texto da energia
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("⚡ ENERGIA: " + player.getEnergy() + "/" + player.getMaxEnergy(), 140, 25);
        
        // Porcentagem
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText(String.format("%.0f%%", energyPercent * 100), 140, 40);
        
        return canvas;
    }
    
    /**
     * Desenha transições suaves entre tiles diferentes para eliminar divisões
     */
    private void drawTileTransitions(Farm farm, Tile tile, int tileX, int tileY, int screenX, int screenY) {
        TileType currentType = tile.getType();
        
        // Verificar tiles adjacentes (8 direções)
        TileType[] neighbors = new TileType[8];
        neighbors[0] = getNeighborType(farm, tileX, tileY - 1); // Norte
        neighbors[1] = getNeighborType(farm, tileX + 1, tileY - 1); // Nordeste
        neighbors[2] = getNeighborType(farm, tileX + 1, tileY); // Leste
        neighbors[3] = getNeighborType(farm, tileX + 1, tileY + 1); // Sudeste
        neighbors[4] = getNeighborType(farm, tileX, tileY + 1); // Sul
        neighbors[5] = getNeighborType(farm, tileX - 1, tileY + 1); // Sudoeste
        neighbors[6] = getNeighborType(farm, tileX - 1, tileY); // Oeste
        neighbors[7] = getNeighborType(farm, tileX - 1, tileY - 1); // Noroeste
        
        // Desenhar bordas suaves apenas onde há transições
        int blendSize = 3; // Tamanho da zona de transição em pixels
        
        // Norte
        if (neighbors[0] != null && neighbors[0] != currentType) {
            drawEdgeBlend(screenX, screenY, 0, blendSize, TILE_SIZE, blendSize, currentType, neighbors[0]);
        }
        
        // Sul
        if (neighbors[4] != null && neighbors[4] != currentType) {
            drawEdgeBlend(screenX, screenY, 0, TILE_SIZE - blendSize, TILE_SIZE, blendSize, currentType, neighbors[4]);
        }
        
        // Leste
        if (neighbors[2] != null && neighbors[2] != currentType) {
            drawEdgeBlend(screenX, screenY, TILE_SIZE - blendSize, 0, blendSize, TILE_SIZE, currentType, neighbors[2]);
        }
        
        // Oeste
        if (neighbors[6] != null && neighbors[6] != currentType) {
            drawEdgeBlend(screenX, screenY, 0, 0, blendSize, TILE_SIZE, currentType, neighbors[6]);
        }
        
        // Cantos - apenas se ambos os tiles adjacentes são diferentes
        // Canto superior esquerdo
        if (neighbors[7] != null && neighbors[7] != currentType && 
            (neighbors[0] == null || neighbors[0] != currentType) &&
            (neighbors[6] == null || neighbors[6] != currentType)) {
            drawCornerBlend(screenX, screenY, 0, 0, blendSize, blendSize, currentType, neighbors[7]);
        }
        
        // Canto superior direito
        if (neighbors[1] != null && neighbors[1] != currentType &&
            (neighbors[0] == null || neighbors[0] != currentType) &&
            (neighbors[2] == null || neighbors[2] != currentType)) {
            drawCornerBlend(screenX, screenY, TILE_SIZE - blendSize, 0, blendSize, blendSize, currentType, neighbors[1]);
        }
        
        // Canto inferior esquerdo
        if (neighbors[5] != null && neighbors[5] != currentType &&
            (neighbors[4] == null || neighbors[4] != currentType) &&
            (neighbors[6] == null || neighbors[6] != currentType)) {
            drawCornerBlend(screenX, screenY, 0, TILE_SIZE - blendSize, blendSize, blendSize, currentType, neighbors[5]);
        }
        
        // Canto inferior direito
        if (neighbors[3] != null && neighbors[3] != currentType &&
            (neighbors[4] == null || neighbors[4] != currentType) &&
            (neighbors[2] == null || neighbors[2] != currentType)) {
            drawCornerBlend(screenX, screenY, TILE_SIZE - blendSize, TILE_SIZE - blendSize, blendSize, blendSize, currentType, neighbors[3]);
        }
    }
    
    /**
     * Obtém o tipo de tile adjacente
     */
    private TileType getNeighborType(Farm farm, int x, int y) {
        Tile neighbor = farm.getTile(x, y);
        return neighbor != null ? neighbor.getType() : null;
    }
    
    /**
     * Desenha uma transição suave em uma borda
     */
    private void drawEdgeBlend(int baseX, int baseY, int offsetX, int offsetY, int width, int height, 
                               TileType type1, TileType type2) {
        Color color1 = Color.rgb(type1.getRed(), type1.getGreen(), type1.getBlue());
        Color color2 = Color.rgb(type2.getRed(), type2.getGreen(), type2.getBlue());
        
        // Criar gradiente simples (blend entre as duas cores)
        if (width > height) {
            // Borda horizontal - gradiente da esquerda para direita
            for (int i = 0; i < width; i++) {
                double ratio = (double)i / width;
                Color blendColor = color1.interpolate(color2, ratio * 0.5); // 50% de mistura suave
                gc.setFill(blendColor);
                gc.fillRect(baseX + offsetX + i, baseY + offsetY, 1, height);
            }
        } else {
            // Borda vertical - gradiente de cima para baixo
            for (int i = 0; i < height; i++) {
                double ratio = (double)i / height;
                Color blendColor = color1.interpolate(color2, ratio * 0.5); // 50% de mistura suave
                gc.setFill(blendColor);
                gc.fillRect(baseX + offsetX, baseY + offsetY + i, width, 1);
            }
        }
    }
    
    /**
     * Desenha uma transição suave em um canto
     */
    private void drawCornerBlend(int baseX, int baseY, int offsetX, int offsetY, int width, int height,
                                 TileType type1, TileType type2) {
        Color color1 = Color.rgb(type1.getRed(), type1.getGreen(), type1.getBlue());
        Color color2 = Color.rgb(type2.getRed(), type2.getGreen(), type2.getBlue());
        
        // Criar gradiente radial no canto
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double dist = Math.sqrt(x * x + y * y);
                double maxDist = Math.sqrt(width * width + height * height);
                double ratio = Math.min(1.0, dist / maxDist);
                Color blendColor = color1.interpolate(color2, ratio * 0.7);
                
                gc.setFill(blendColor);
                gc.fillRect(baseX + offsetX + x, baseY + offsetY + y, 1, 1);
            }
        }
    }
    
    /**
     * Desenha highlight visual no tile onde o jogador está
     */
    private void drawTileHighlight(int tileX, int tileY) {
        // Sombra do highlight
        gc.setFill(Color.rgb(255, 215, 0, 0.1));
        gc.fillRoundRect(tileX - 2, tileY - 2, TILE_SIZE + 4, TILE_SIZE + 4, 8, 8);
        
        // Borda pulsante sutil (pode ser animado depois)
        gc.setStroke(Color.rgb(255, 215, 0, 0.4));
        gc.setLineWidth(2);
        gc.strokeRoundRect(tileX, tileY, TILE_SIZE, TILE_SIZE, 5, 5);
        
        // Cantos decorativos
        double cornerSize = 6;
        gc.setFill(Color.rgb(255, 215, 0, 0.6));
        // Canto superior esquerdo
        gc.fillPolygon(
            new double[]{tileX, tileX + cornerSize, tileX},
            new double[]{tileY, tileY, tileY + cornerSize},
            3
        );
        // Canto superior direito
        gc.fillPolygon(
            new double[]{tileX + TILE_SIZE, tileX + TILE_SIZE - cornerSize, tileX + TILE_SIZE},
            new double[]{tileY, tileY, tileY + cornerSize},
            3
        );
        // Canto inferior esquerdo
        gc.fillPolygon(
            new double[]{tileX, tileX + cornerSize, tileX},
            new double[]{tileY + TILE_SIZE, tileY + TILE_SIZE, tileY + TILE_SIZE - cornerSize},
            3
        );
        // Canto inferior direito
        gc.fillPolygon(
            new double[]{tileX + TILE_SIZE, tileX + TILE_SIZE - cornerSize, tileX + TILE_SIZE},
            new double[]{tileY + TILE_SIZE, tileY + TILE_SIZE, tileY + TILE_SIZE - cornerSize},
            3
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}

