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
    
    // Sprites do jogo
    public static class Sprites {
        // Personagem
        public static final String PLAYER = "player/player.png";
        public static final String PLAYER_UP = "player/player_up_1.png";
        public static final String PLAYER_DOWN = "player/player_down_1.png";
        public static final String PLAYER_LEFT = "player/player_left_1.png";
        public static final String PLAYER_RIGHT = "player/player_right_1.png";
        
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

