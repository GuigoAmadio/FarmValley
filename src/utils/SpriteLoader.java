package utils;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {
    private static final String SPRITES_FOLDER = "assets/sprites/";
    private static Map<String, Image> spriteCache = new HashMap<>();
    
    // Carregar sprite do arquivo
    public static Image loadSprite(String filename) {
        // Verificar se já está em cache
        if (spriteCache.containsKey(filename)) {
            return spriteCache.get(filename);
        }
        
        try {
            // Tentar carregar do arquivo
            Image image = new Image(new FileInputStream(SPRITES_FOLDER + filename));
            spriteCache.put(filename, image);
            return image;
        } catch (FileNotFoundException e) {
            // Sprite não encontrado - usando fallback silenciosamente
            return null;
        }
    }
    
    // Verificar se sprite existe
    public static boolean spriteExists(String filename) {
        if (spriteCache.containsKey(filename)) {
            return true;
        }
        try {
            new FileInputStream(SPRITES_FOLDER + filename);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
    
    // Limpar cache (útil para recarregar sprites)
    public static void clearCache() {
        spriteCache.clear();
    }
    
    // ===== MÉTODOS AUXILIARES PARA SPRITES DE PERSONAGENS =====
    
    /**
     * Carrega sprite de NPC baseado em tipo, direção e frame
     * @param npcType Tipo do NPC (MERCHANT, FARMER, VILLAGER, WANDERER)
     * @param direction Direção (0=DOWN, 1=UP, 2=LEFT, 3=RIGHT)
     * @param frame Frame de animação (0-5)
     * @return Image ou null se não encontrado
     */
    public static Image loadNPCSprite(String npcType, int direction, int frame) {
        String filename = String.format("npcs/%s/frame_%d_%d.png", npcType.toLowerCase(), direction, frame);
        return loadSprite(filename);
    }
    
    /**
     * Carrega sprite de inimigo baseado em tipo, direção e frame
     * @param enemyType Tipo do inimigo (SLIME, GOBLIN, SKELETON)
     * @param direction Direção (0=DOWN, 1=UP, 2=LEFT, 3=RIGHT)
     * @param frame Frame de animação (0-5)
     * @return Image ou null se não encontrado
     */
    public static Image loadEnemySprite(String enemyType, int direction, int frame) {
        String filename = String.format("enemies/%s/frame_%d_%d.png", enemyType.toLowerCase(), direction, frame);
        return loadSprite(filename);
    }
    
    /**
     * Carrega sprite de cultura baseado em tipo e estágio
     * @param cropType Tipo da cultura (WHEAT, TOMATO, CORN, CARROT)
     * @param stage Estágio de crescimento (0-3, onde 3 = maduro)
     * @return Image ou null se não encontrado
     */
    public static Image loadCropSprite(String cropType, int stage) {
        String filename;
        if (stage >= 3) {
            filename = String.format("crops/%s_mature.png", cropType.toLowerCase());
        } else {
            filename = String.format("crops/%s_%d.png", cropType.toLowerCase(), stage + 1);
        }
        return loadSprite(filename);
    }
    
    // Sprites do jogo
    public static class Sprites {
        // Personagem principal (usar player.png como principal)
        public static final String PLAYER = "player/player.png";
        public static final String PLAYER_ALT = "player/frame_0_1.png";
        public static final String PLAYER_WARRIOR = "player/frame_0_2.png";
        
        // Animações direcionais
        public static final String PLAYER_UP = "player/player_up_1.png";
        public static final String PLAYER_UP_0 = "player/player_up_0.png";
        public static final String PLAYER_UP_2 = "player/player_up_2.png";
        
        public static final String PLAYER_DOWN = "player/player_down_1.png";
        public static final String PLAYER_DOWN_0 = "player/player_down_0.png";
        public static final String PLAYER_DOWN_2 = "player/player_down_2.png";
        
        public static final String PLAYER_LEFT = "player/player_left_1.png";
        public static final String PLAYER_LEFT_0 = "player/player_left_0.png";
        public static final String PLAYER_LEFT_2 = "player/player_left_2.png";
        
        public static final String PLAYER_RIGHT = "player/player_right_1.png";
        public static final String PLAYER_RIGHT_0 = "player/player_right_0.png";
        public static final String PLAYER_RIGHT_2 = "player/player_right_2.png";
        
        // Tiles
        public static final String GRASS = "tiles/grass.png";
        public static final String DIRT = "tiles/dirt.png";
        public static final String WATER = "tiles/water.png";
        public static final String STONE = "tiles/stone.png";
        
        // Plantas
        public static final String WHEAT_1 = "wheat_1.png";
        public static final String WHEAT_2 = "wheat_2.png";
        public static final String WHEAT_3 = "wheat_3.png";
        public static final String WHEAT_MATURE = "wheat_mature.png";
        
        public static final String TOMATO_1 = "tomato_1.png";
        public static final String TOMATO_2 = "tomato_2.png";
        public static final String TOMATO_3 = "tomato_3.png";
        public static final String TOMATO_MATURE = "tomato_mature.png";
        
        public static final String CORN_1 = "corn_1.png";
        public static final String CORN_2 = "corn_2.png";
        public static final String CORN_3 = "corn_3.png";
        public static final String CORN_MATURE = "corn_mature.png";
        
        public static final String CARROT_1 = "carrot_1.png";
        public static final String CARROT_2 = "carrot_2.png";
        public static final String CARROT_3 = "carrot_3.png";
        public static final String CARROT_MATURE = "carrot_mature.png";
    }
}

