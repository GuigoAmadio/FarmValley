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
import entities.NPC;
import entities.Enemy;
import world.Farm;
import world.Tile;
import world.TileType;
import world.TimeSystem;
import types.CropType;
import systems.DecorationManager;
import systems.UIManager;
import systems.HarvestSystem;
import systems.Inventory;
import systems.EntityManager;
import utils.SpriteLoader;

public class GameWindow extends Application {
    private static final int TILE_SIZE = 60; // ZOOM: Tiles maiores para melhor visibilidade
    private static int CANVAS_WIDTH = 1920; // Full HD
    private static int CANVAS_HEIGHT = 1000; // Altura ajustada

    private Canvas canvas;
    private GraphicsContext gc;
    private GameEngine engine;
    private VBox hudBox;

    @Override
    public void start(Stage primaryStage) {
        // Detectar tamanho da tela - reduzido para caber melhor
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        CANVAS_WIDTH = (int) (screenBounds.getWidth() * 0.85); // 85% da largura
        CANVAS_HEIGHT = (int) (screenBounds.getHeight() * 0.80); // 80% da altura
        
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
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen(); // Centralizar na tela
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
        Player.Direction facing = player.getFacing();
        int frame = player.getAnimationFrame() % 6;
        
        // Mapeamento: 0=down, 1=up, 2=left, 3=right
        int direction;
        switch (facing) {
            case DOWN: direction = 0; break;
            case UP: direction = 1; break;
            case LEFT: direction = 2; break;
            case RIGHT: direction = 3; break;
            default: direction = 0;
        }
        
        String spriteName = "player/frame_" + direction + "_" + frame + ".png";
        Image sprite = SpriteLoader.loadSprite(spriteName);
        
        if (sprite != null) {
            return sprite;
        }
        
        // Fallback: sprite padrão
        return SpriteLoader.loadSprite("player/frame_0_0.png");
    }

    private void render() {
        // Fundo gradiente
        gc.setFill(Color.rgb(20, 30, 48));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        Farm farm = engine.getFarm();
        Player player = engine.getPlayer();
        TimeSystem time = engine.getTimeSystem();

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
        
        // LAYER 1.5: Desenhar NPCs e Inimigos
        EntityManager entityManager = engine.getEntityManager();
        
        // Desenhar NPCs
        for (NPC npc : entityManager.getNPCs()) {
            drawNPC(npc, offsetX, offsetY, player.getX(), player.getY());
        }
        
        // Desenhar Inimigos
        for (Enemy enemy : entityManager.getEnemies()) {
            if (!enemy.isDead()) {
                drawEnemy(enemy, offsetX, offsetY, player.getX(), player.getY());
            }
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
        
        // LAYER 4: Sistema de partículas
        systems.ParticleSystem particles = engine.getParticleSystem();
        particles.render(gc, offsetX, offsetY, TILE_SIZE);
        
        // LAYER 5: Efeitos de clima
        systems.WeatherSystem weather = engine.getWeatherSystem();
        renderWeather(weather, offsetX, offsetY);
        
        // Mensagem de status (se houver)
        String message = engine.getStatusMessage();
        if (!message.isEmpty()) {
            drawStatusMessage(message);
        }
        
        // HOT BAR sempre visível
        UIManager uiManager = engine.getUIManager();
        uiManager.renderHotBar(gc, player.getInventory(), CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // Renderizar inventário completo (se aberto)
        if (uiManager.isInventoryOpen()) {
            uiManager.renderInventory(gc, player.getInventory(), CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        
        // Renderizar loja (se aberta)
        if (uiManager.isShopOpen()) {
            uiManager.renderShop(gc, player.getInventory(), engine.getShopSystem(), CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        
        // Minimapa
        systems.Minimap minimap = engine.getMinimap();
        minimap.render(gc, farm, player, 
                      engine.getEntityManager().getNPCs(),
                      engine.getEntityManager().getEnemies(),
                      CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // Missões ativas (canto superior esquerdo)
        renderQuestTracker();
        
        // Efeito de iluminação dia/noite
        double darkness = time.getDarknessLevel();
        if (darkness > 0) {
            double[] lightColor = time.getLightColor();
            gc.setFill(Color.rgb(
                (int)(0 * lightColor[0]),
                (int)(0 * lightColor[1]),
                (int)(30 * lightColor[2]),
                darkness * 0.7
            ));
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
        
        // Relâmpago (se tempestade)
        if (weather.shouldFlashLightning()) {
            gc.setFill(Color.rgb(255, 255, 255, 0.7));
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }
    
    /**
     * Renderiza efeitos de clima
     */
    private void renderWeather(systems.WeatherSystem weather, int offsetX, int offsetY) {
        systems.ParticleSystem particles = engine.getParticleSystem();
        
        // Spawnar partículas de chuva/neve
        if (weather.shouldSpawnRain()) {
            particles.spawnRainDrop(CANVAS_WIDTH, CANVAS_HEIGHT, TILE_SIZE);
        }
        if (weather.shouldSpawnSnow()) {
            particles.spawnSnowflake(CANVAS_WIDTH, CANVAS_HEIGHT, TILE_SIZE);
        }
        
        // Overlay de clima
        double[] overlay = weather.getOverlayColor();
        if (overlay[3] > 0) {
            gc.setFill(Color.rgb(
                (int)(overlay[0] * 255),
                (int)(overlay[1] * 255),
                (int)(overlay[2] * 255),
                overlay[3]
            ));
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }
    
    /**
     * Renderiza tracker de missões no canto superior esquerdo
     */
    private void renderQuestTracker() {
        systems.QuestSystem questSystem = engine.getQuestSystem();
        java.util.List<systems.QuestSystem.Quest> quests = questSystem.getActiveQuests();
        
        if (quests.isEmpty()) return;
        
        int x = 15;
        int y = 15;
        int width = 180;
        int lineHeight = 20;
        int shown = Math.min(3, quests.size()); // Máximo 3 missões visíveis
        int height = 35 + shown * lineHeight;
        
        // Fundo
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRoundRect(x, y, width, height, 8, 8);
        gc.setStroke(Color.rgb(255, 200, 100));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, width, height, 8, 8);
        
        // Título
        gc.setFill(Color.rgb(255, 215, 0));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("MISSÕES", x + 10, y + 18);
        
        // Missões
        gc.setFont(Font.font("Arial", 9));
        int idx = 0;
        for (systems.QuestSystem.Quest quest : quests) {
            if (idx >= shown) break;
            
            int qy = y + 32 + idx * lineHeight;
            
            // Truncar nome se muito longo
            String text = quest.getName();
            if (text.length() > 18) {
                text = text.substring(0, 15) + "...";
            }
            
            // Cor baseada no estado
            if (quest.isCompleted()) {
                gc.setFill(Color.rgb(100, 255, 100));
            } else {
                gc.setFill(Color.rgb(200, 200, 200));
            }
            
            gc.fillText(text, x + 8, qy);
            
            // Barra de progresso pequena
            double progress = quest.getProgressPercent();
            int barWidth = 40;
            int barX = x + width - barWidth - 8;
            
            gc.setFill(Color.rgb(50, 50, 50));
            gc.fillRect(barX, qy - 7, barWidth, 6);
            
            Color barColor = quest.isCompleted() ? Color.rgb(100, 255, 100) : Color.rgb(255, 200, 100);
            gc.setFill(barColor);
            gc.fillRect(barX, qy - 7, (int)(barWidth * progress), 6);
            
            idx++;
        }
    }
    
    private void drawStatusMessage(String message) {
        // Calcular largura baseada no tamanho do texto
        double charWidth = 8; // Aproximadamente 8 pixels por caractere
        double msgWidth = Math.max(300, message.length() * charWidth + 40);
        double msgHeight = 50;
        double msgX = (CANVAS_WIDTH - msgWidth) / 2; // Centralizado
        double msgY = 140; // Abaixo do HUD superior
        
        // Sombra
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(msgX + 2, msgY + 2, msgWidth, msgHeight, 10, 10);
        
        // Fundo da mensagem
        gc.setFill(Color.rgb(20, 20, 25, 0.9));
        gc.fillRoundRect(msgX, msgY, msgWidth, msgHeight, 8, 8);
        
        // Borda externa
        gc.setStroke(Color.rgb(100, 200, 255));
        gc.setLineWidth(2);
        gc.strokeRoundRect(msgX, msgY, msgWidth, msgHeight, 8, 8);
        
        // Brilho superior (removido para simplificar)
        gc.setFill(Color.rgb(100, 200, 255, 0.1));
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
     * Desenha um NPC no mapa
     */
    private void drawNPC(NPC npc, int offsetX, int offsetY, int playerX, int playerY) {
        int screenX = npc.getX() * TILE_SIZE + offsetX;
        int screenY = npc.getY() * TILE_SIZE + offsetY;
        
        // Verificar se está na tela
        if (screenX < -TILE_SIZE || screenX > CANVAS_WIDTH + TILE_SIZE ||
            screenY < -TILE_SIZE || screenY > CANVAS_HEIGHT + TILE_SIZE) {
            return;
        }
        
        NPC.NPCType type = npc.getType();
        
        // Tentar carregar sprite do NPC
        Image npcSprite = getNPCSprite(npc);
        
        if (npcSprite != null) {
            // Sombra
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillOval(screenX + 10, screenY + TILE_SIZE - 8, TILE_SIZE - 20, 8);
            
            // Desenhar sprite
            int spriteSize = (int)(TILE_SIZE * 1.1);
            int spriteOffsetX = (TILE_SIZE - spriteSize) / 2;
            int spriteOffsetY = (TILE_SIZE - spriteSize) / 2;
            gc.drawImage(npcSprite, screenX + spriteOffsetX, screenY + spriteOffsetY, spriteSize, spriteSize);
        } else {
            // Fallback: desenho geométrico
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillOval(screenX + 10, screenY + TILE_SIZE - 8, TILE_SIZE - 20, 8);
            
            Color bodyColor = Color.rgb(type.getRed(), type.getGreen(), type.getBlue());
            gc.setFill(bodyColor);
            
            int bodyWidth = TILE_SIZE - 20;
            int bodyHeight = (int)(TILE_SIZE * 0.7);
            int bodyX = screenX + 10;
            int bodyY = screenY + 8;
            
            gc.fillOval(bodyX, bodyY + bodyHeight/3, bodyWidth, bodyHeight);
            
            int headSize = (int)(bodyWidth * 0.8);
            int headX = bodyX + (bodyWidth - headSize) / 2;
            gc.setFill(Color.rgb(255, 220, 180));
            gc.fillOval(headX, bodyY, headSize, headSize);
            
            gc.setFill(Color.BLACK);
            gc.fillOval(headX + headSize/4, bodyY + headSize/3, 4, 4);
            gc.fillOval(headX + headSize*3/4 - 4, bodyY + headSize/3, 4, 4);
        }
        
        // Indicador de comerciante
        if (type == NPC.NPCType.MERCHANT) {
            gc.setFill(Color.GOLD);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.fillText("$", screenX + TILE_SIZE - 10, screenY + 15);
        }
        
        // Nome e tipo acima do NPC (se próximo do jogador)
        if (npc.isNearPlayer(playerX, playerY)) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(Color.WHITE);
            gc.fillText(npc.getName(), screenX + TILE_SIZE/2, screenY - 10);
            gc.setFont(Font.font("Arial", 8));
            gc.setFill(Color.rgb(200, 200, 200));
            gc.fillText("[" + type.getTitle() + "]", screenX + TILE_SIZE/2, screenY);
            
            // Indicador de interação
            gc.setFill(Color.rgb(255, 215, 0, 0.8));
            gc.fillText("F para conversar", screenX + TILE_SIZE/2, screenY + TILE_SIZE + 15);
        }
    }
    
    /**
     * Obtém sprite do NPC baseado no tipo e direção
     */
    private Image getNPCSprite(NPC npc) {
        String folder;
        NPC.NPCType type = npc.getType();
        
        // Determinar pasta baseado no tipo
        switch (type) {
            case MERCHANT:
                folder = "npcs/merchant";
                break;
            case VILLAGER:
            case FARMER:
            case WANDERER:
                folder = "npcs/villager";
                break;
            default:
                folder = "npcs/villager";
        }
        
        // Determinar direção (0=down, 1=up, 2=left, 3=right)
        int direction;
        switch (npc.getFacing()) {
            case DOWN: direction = 0; break;
            case UP: direction = 1; break;
            case LEFT: direction = 2; break;
            case RIGHT: direction = 3; break;
            default: direction = 0;
        }
        
        int frame = npc.getAnimationFrame() % 6;
        String spriteName = folder + "/frame_" + direction + "_" + frame + ".png";
        
        return SpriteLoader.loadSprite(spriteName);
    }
    
    /**
     * Desenha um inimigo no mapa
     */
    private void drawEnemy(Enemy enemy, int offsetX, int offsetY, int playerX, int playerY) {
        int screenX = enemy.getX() * TILE_SIZE + offsetX;
        int screenY = enemy.getY() * TILE_SIZE + offsetY;
        
        // Verificar se está na tela
        if (screenX < -TILE_SIZE || screenX > CANVAS_WIDTH + TILE_SIZE ||
            screenY < -TILE_SIZE || screenY > CANVAS_HEIGHT + TILE_SIZE) {
            return;
        }
        
        Enemy.EnemyType type = enemy.getType();
        
        // Tentar carregar sprite do inimigo
        Image enemySprite = getEnemySprite(enemy);
        
        if (enemySprite != null) {
            // Sombra
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            gc.fillOval(screenX + 8, screenY + TILE_SIZE - 10, TILE_SIZE - 16, 10);
            
            // Desenhar sprite
            int spriteSize = (int)(TILE_SIZE * 1.1);
            int spriteOffsetX = (TILE_SIZE - spriteSize) / 2;
            int spriteOffsetY = (TILE_SIZE - spriteSize) / 2;
            gc.drawImage(enemySprite, screenX + spriteOffsetX, screenY + spriteOffsetY, spriteSize, spriteSize);
            
            // Indicador de agressivo
            if (enemy.isAggressive()) {
                gc.setFill(Color.RED);
                gc.setGlobalAlpha(0.2 + 0.1 * Math.sin(System.currentTimeMillis() / 100.0));
                gc.fillOval(screenX, screenY, TILE_SIZE, TILE_SIZE);
                gc.setGlobalAlpha(1.0);
            }
        } else {
            // Fallback: desenho geométrico
            Color baseColor = Color.rgb(type.getRed(), type.getGreen(), type.getBlue());
            Color bodyColor = enemy.isAggressive() ? baseColor.brighter() : baseColor;
            
            gc.setFill(Color.rgb(0, 0, 0, 0.4));
            gc.fillOval(screenX + 8, screenY + TILE_SIZE - 10, TILE_SIZE - 16, 10);
            
            switch (type) {
                case SLIME:
                    drawSlime(screenX, screenY, bodyColor, enemy.getAnimationFrame());
                    break;
                case GOBLIN:
                    drawGoblin(screenX, screenY, bodyColor);
                    break;
                case SKELETON:
                    drawSkeleton(screenX, screenY);
                    break;
            }
            
            if (enemy.isAggressive()) {
                gc.setFill(Color.RED);
                gc.setGlobalAlpha(0.3 + 0.2 * Math.sin(System.currentTimeMillis() / 100.0));
                gc.fillOval(screenX, screenY, TILE_SIZE, TILE_SIZE);
                gc.setGlobalAlpha(1.0);
            }
        }
        
        // Barra de vida sempre visível
        int barY = screenY - 8;
        drawEnemyHealthBar(screenX, barY, TILE_SIZE, enemy.getHealth(), enemy.getMaxHealth());
        
        // Nome se próximo
        if (enemy.isAdjacentTo(playerX, playerY)) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(Color.rgb(255, 100, 100));
            gc.fillText(type.getName(), screenX + TILE_SIZE/2, screenY - 15);
            
            // Indicador de ataque
            gc.setFill(Color.rgb(255, 100, 100, 0.8));
            gc.fillText("Q para atacar", screenX + TILE_SIZE/2, screenY + TILE_SIZE + 15);
        }
    }
    
    /**
     * Obtém sprite do inimigo baseado no tipo e direção
     */
    private Image getEnemySprite(Enemy enemy) {
        // Usar o sprite do orc para todos os inimigos
        String folder = "enemies/orc";
        
        // Determinar direção (0=down, 1=up, 2=left, 3=right)
        int direction;
        switch (enemy.getFacing()) {
            case DOWN: direction = 0; break;
            case UP: direction = 1; break;
            case LEFT: direction = 2; break;
            case RIGHT: direction = 3; break;
            default: direction = 0;
        }
        
        int frame = enemy.getAnimationFrame() % 6;
        String spriteName = folder + "/frame_" + direction + "_" + frame + ".png";
        
        return SpriteLoader.loadSprite(spriteName);
    }
    
    private void drawSlime(int x, int y, Color color, int frame) {
        // Slime pula suavemente
        int bounceOffset = (int)(Math.sin(frame * Math.PI / 2) * 5);
        
        gc.setFill(color);
        gc.fillOval(x + 10, y + 15 - bounceOffset, TILE_SIZE - 20, TILE_SIZE - 25);
        
        // Brilho
        gc.setFill(Color.rgb(255, 255, 255, 0.4));
        gc.fillOval(x + 15, y + 20 - bounceOffset, 10, 8);
        
        // Olhos
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 18, y + 25 - bounceOffset, 8, 8);
        gc.fillOval(x + 32, y + 25 - bounceOffset, 8, 8);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 20, y + 27 - bounceOffset, 4, 4);
        gc.fillOval(x + 34, y + 27 - bounceOffset, 4, 4);
    }
    
    private void drawGoblin(int x, int y, Color color) {
        // Corpo
        gc.setFill(color);
        gc.fillOval(x + 15, y + 25, TILE_SIZE - 30, TILE_SIZE - 30);
        
        // Cabeça grande
        gc.fillOval(x + 10, y + 5, TILE_SIZE - 20, 30);
        
        // Orelhas pontudas
        gc.fillPolygon(
            new double[]{x + 8, x + 15, x + 15},
            new double[]{y + 10, y + 5, y + 20},
            3
        );
        gc.fillPolygon(
            new double[]{x + TILE_SIZE - 8, x + TILE_SIZE - 15, x + TILE_SIZE - 15},
            new double[]{y + 10, y + 5, y + 20},
            3
        );
        
        // Olhos amarelos
        gc.setFill(Color.YELLOW);
        gc.fillOval(x + 18, y + 12, 8, 8);
        gc.fillOval(x + 34, y + 12, 8, 8);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 20, y + 14, 4, 4);
        gc.fillOval(x + 36, y + 14, 4, 4);
    }
    
    private void drawSkeleton(int x, int y) {
        // Crânio
        gc.setFill(Color.rgb(220, 220, 220));
        gc.fillOval(x + 15, y + 5, TILE_SIZE - 30, 25);
        
        // Olhos vazios
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 20, y + 12, 8, 10);
        gc.fillOval(x + 32, y + 12, 8, 10);
        
        // Nariz
        gc.fillPolygon(
            new double[]{x + 30, x + 27, x + 33},
            new double[]{y + 20, y + 28, y + 28},
            3
        );
        
        // Corpo (costelas)
        gc.setFill(Color.rgb(200, 200, 200));
        for (int i = 0; i < 4; i++) {
            gc.fillRect(x + 20, y + 32 + i * 6, TILE_SIZE - 40, 3);
        }
        
        // Braços de osso
        gc.fillRect(x + 10, y + 35, 8, 20);
        gc.fillRect(x + TILE_SIZE - 18, y + 35, 8, 20);
    }
    
    
    /**
     * Desenha barra de vida de inimigo
     */
    private void drawEnemyHealthBar(int x, int y, int width, int current, int max) {
        int barWidth = width - 10;
        int barHeight = 5;
        int barX = x + 5;
        
        // Fundo
        gc.setFill(Color.rgb(50, 0, 0, 0.8));
        gc.fillRoundRect(barX, y, barWidth, barHeight, 2, 2);
        
        // Vida
        double progress = (double)current / max;
        Color barColor = progress > 0.5 ? Color.rgb(76, 175, 80) : 
                        progress > 0.25 ? Color.rgb(255, 193, 7) : 
                        Color.rgb(244, 67, 54);
        
        gc.setFill(barColor);
        gc.fillRoundRect(barX, y, (int)(barWidth * progress), barHeight, 2, 2);
        
        // Borda
        gc.setStroke(Color.rgb(0, 0, 0, 0.5));
        gc.setLineWidth(1);
        gc.strokeRoundRect(barX, y, barWidth, barHeight, 2, 2);
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
            
            // Determinar qual sprite usar baseado no progresso
            String spriteName;
            if (crop.isFullyGrown()) {
                // Planta madura - usar sprite específico
                if (cropType == CropType.CORN) {
                    spriteName = "icons/corn_3.png";
                } else if (cropType == CropType.TOMATO) {
                    spriteName = "icons/tomato_3.png";
                } else {
                    spriteName = "icons/plant2.png"; // fallback
                }
            } else if (progress > 0.5) {
                // Estágio 2
                spriteName = "icons/plant2.png";
            } else {
                // Estágio 1
                spriteName = "icons/plant1.png";
            }
            
            Image plantSprite = SpriteLoader.loadSprite(spriteName);
            
            if (plantSprite != null) {
                // Tamanho aumenta com progresso
                int spriteSize = (int)(TILE_SIZE * (0.5 + progress * 0.5));
                int offsetX = (TILE_SIZE - spriteSize) / 2;
                int offsetY = TILE_SIZE - spriteSize;
                
                gc.drawImage(plantSprite, x + offsetX, y + offsetY, spriteSize, spriteSize);
            } else {
                // Fallback: desenho geométrico
                Color plantColor = Color.rgb(cropType.getRed(), cropType.getGreen(), cropType.getBlue());
                int plantSize = (int)(TILE_SIZE * 0.5 * progress);
                int plantOffset = (TILE_SIZE - plantSize) / 2;
                gc.setFill(plantColor);
                gc.fillOval(x + plantOffset, y + plantOffset, plantSize, plantSize);
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
        systems.LevelSystem level = engine.getLevelSystem();
        systems.GameStats stats = engine.getGameStats();
        
        // Container horizontal para informações principais
        javafx.scene.layout.HBox infoBox = new javafx.scene.layout.HBox(10);
        infoBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Título com nível
        javafx.scene.text.Text title = new javafx.scene.text.Text("🌾 FARM VALLEY 🌾");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setFill(Color.rgb(255, 215, 0));
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 3, 0, 0, 2);");
        
        // Card de Nível/XP
        String levelStr = "Nv." + level.getLevel() + " (" + (int)(level.getLevelProgress() * 100) + "%)";
        javafx.scene.layout.VBox levelCard = createInfoCard("⭐", "NÍVEL", levelStr, Color.rgb(255, 165, 0));
        
        // Card de Hora/Data
        String timeStr = time.getTimeString() + " | " + time.getSeason().substring(0, 3) + " " + time.getDayOfSeason();
        javafx.scene.layout.VBox dateCard = createInfoCard("🕐", "HORA", timeStr, Color.rgb(70, 130, 180));
        
        // Card de Dinheiro
        javafx.scene.layout.VBox moneyCard = createInfoCard("💰", "DINHEIRO", "$" + player.getMoney(), Color.rgb(255, 215, 0));
        
        // Card de Sementes
        javafx.scene.layout.VBox seedCard = createInfoCard("🌱", "SEMENTE", 
            player.getSelectedSeed().getName() + " ($" + player.getSelectedSeed().getSeedCost() + ")", 
            Color.rgb(34, 139, 34));
        
        // Barras de vida e energia
        javafx.scene.canvas.Canvas statusBars = createStatusBars(player);
        
        // Card de clima
        systems.WeatherSystem weather = engine.getWeatherSystem();
        javafx.scene.layout.VBox weatherCard = createInfoCard(
            weather.getWeatherIcon(), "CLIMA", weather.getWeatherName(), 
            Color.rgb(100, 150, 200));
        
        infoBox.getChildren().addAll(levelCard, dateCard, moneyCard, weatherCard, seedCard, statusBars);
        
        // Controles atualizados (compactos)
        javafx.scene.text.Text controls = new javafx.scene.text.Text(
            "⌨️ WASD:Mover | T:Arar | P:Plantar | H:Colher | E:Coletar | F:Falar/Vender | Q:Atacar | R:Poção | Z:Dormir | I:Inventário"
        );
        controls.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        controls.setFill(Color.rgb(150, 150, 150));
        controls.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 1, 0, 0, 1);");
        
        // Adicionar tudo ao HUD
        hudBox.getChildren().addAll(title, infoBox, controls);
    }
    
    /**
     * Cria barras de vida e energia combinadas
     */
    private javafx.scene.canvas.Canvas createStatusBars(Player player) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(100, 50);
        javafx.scene.canvas.GraphicsContext g = canvas.getGraphicsContext2D();
        
        // Fundo
        g.setFill(Color.rgb(30, 30, 30, 0.9));
        g.fillRoundRect(0, 0, 100, 50, 8, 8);
        g.setStroke(Color.rgb(80, 80, 80));
        g.setLineWidth(2);
        g.strokeRoundRect(0, 0, 100, 50, 8, 8);
        
        // Labels
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        
        // Barra de vida
        g.fillText("❤️ VIDA", 5, 14);
        double healthPercent = player.getHealthPercent();
        g.setFill(Color.rgb(40, 40, 40));
        g.fillRoundRect(5, 18, 90, 8, 3, 3);
        
        // Cor baseada na vida
        Color healthColor = healthPercent > 0.5 ? Color.rgb(76, 175, 80) : 
                           healthPercent > 0.25 ? Color.rgb(255, 193, 7) : 
                           Color.rgb(244, 67, 54);
        g.setFill(healthColor);
        g.fillRoundRect(5, 18, (int)(90 * healthPercent), 8, 3, 3);
        
        // Texto da vida
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 8));
        g.fillText(player.getHealth() + "/" + player.getMaxHealth(), 40, 25);
        
        // Barra de energia
        g.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        g.fillText("⚡ ENERGIA", 5, 36);
        double energyPercent = player.getEnergyPercent();
        g.setFill(Color.rgb(40, 40, 40));
        g.fillRoundRect(5, 40, 90, 8, 3, 3);
        g.setFill(Color.rgb(255, 193, 7));
        g.fillRoundRect(5, 40, (int)(90 * energyPercent), 8, 3, 3);
        
        // Texto da energia
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 8));
        g.fillText(player.getEnergy() + "/" + player.getMaxEnergy(), 40, 47);
        
        return canvas;
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

