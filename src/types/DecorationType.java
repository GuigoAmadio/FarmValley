package types;

/**
 * Tipos de decorações disponíveis no jogo
 * Cada tipo define sprite, walkability e layer de renderização
 */
public enum DecorationType {
    // ===== ÁRVORES (Layer 3 - Sobre o player) =====
    
    // Árvores normais
    TREE_1("decorations/trees/Tree1.png", false, 3, 80, 96),
    TREE_2("decorations/trees/Tree2.png", false, 3, 80, 96),
    TREE_3("decorations/trees/Tree3.png", false, 3, 80, 96),
    
    // Árvores frutíferas
    FRUIT_TREE_1("decorations/trees/Fruit_tree1.png", false, 3, 80, 96),
    FRUIT_TREE_2("decorations/trees/Fruit_tree2.png", false, 3, 80, 96),
    FRUIT_TREE_3("decorations/trees/Fruit_tree3.png", false, 3, 80, 96),
    
    // Árvores de outono
    AUTUMN_TREE_1("decorations/trees/Autumn_tree1.png", false, 3, 80, 96),
    AUTUMN_TREE_2("decorations/trees/Autumn_tree2.png", false, 3, 80, 96),
    
    // Palmeiras
    PALM_TREE_1("decorations/trees/Palm_tree1_1.png", false, 3, 64, 96),
    PALM_TREE_2("decorations/trees/Palm_tree2_1.png", false, 3, 64, 96),
    
    // ===== ARBUSTOS (Layer 1 - Abaixo do player) =====
    
    // Arbustos simples
    BUSH_SIMPLE_1("decorations/bushes/Bush_simple1_1.png", true, 1, 48, 48),
    BUSH_SIMPLE_2("decorations/bushes/Bush_simple1_2.png", true, 1, 48, 48),
    BUSH_SIMPLE_3("decorations/bushes/Bush_simple2_1.png", true, 1, 48, 48),
    
    // Arbustos com flores
    BUSH_RED_FLOWERS("decorations/bushes/Bush_red_flowers1.png", true, 1, 48, 48),
    BUSH_BLUE_FLOWERS("decorations/bushes/Bush_blue_flowers1.png", true, 1, 48, 48),
    BUSH_PINK_FLOWERS("decorations/bushes/Bush_pink_flowers1.png", true, 1, 48, 48),
    BUSH_ORANGE_FLOWERS("decorations/bushes/Bush_orange_flowers1.png", true, 1, 48, 48),
    
    // Samambaias
    FERN_1("decorations/bushes/Fern1_1.png", true, 1, 48, 48),
    FERN_2("decorations/bushes/Fern2_1.png", true, 1, 48, 48),
    
    // ===== RUÍNAS (Layer 3 - Sobre o player, não walkable) =====
    
    // Ruínas marrons
    RUINS_BROWN_1("decorations/ruins/Brown_ruins1.png", false, 3, 64, 64),
    RUINS_BROWN_2("decorations/ruins/Brown_ruins2.png", false, 3, 64, 64),
    RUINS_BROWN_3("decorations/ruins/Brown_ruins3.png", false, 3, 64, 64),
    
    // Ruínas de areia
    RUINS_SAND_1("decorations/ruins/Sand_ruins1.png", false, 3, 64, 64),
    RUINS_SAND_2("decorations/ruins/Sand_ruins2.png", false, 3, 64, 64);
    
    
    // ===== PROPRIEDADES =====
    
    private final String spriteFile;
    private final boolean walkable;
    private final int layer;
    private final int width;
    private final int height;
    
    DecorationType(String spriteFile, boolean walkable, int layer, int width, int height) {
        this.spriteFile = spriteFile;
        this.walkable = walkable;
        this.layer = layer;
        this.width = width;
        this.height = height;
    }
    
    public String getSpriteFile() {
        return spriteFile;
    }
    
    public boolean isWalkable() {
        return walkable;
    }
    
    public int getLayer() {
        return layer;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    // ===== MÉTODOS UTILITÁRIOS =====
    
    /**
     * Retorna uma árvore aleatória
     */
    public static DecorationType getRandomTree() {
        DecorationType[] trees = {
            TREE_1, TREE_2, TREE_3,
            FRUIT_TREE_1, FRUIT_TREE_2, FRUIT_TREE_3,
            AUTUMN_TREE_1, AUTUMN_TREE_2,
            PALM_TREE_1, PALM_TREE_2
        };
        return trees[(int)(Math.random() * trees.length)];
    }
    
    /**
     * Retorna um arbusto aleatório
     */
    public static DecorationType getRandomBush() {
        DecorationType[] bushes = {
            BUSH_SIMPLE_1, BUSH_SIMPLE_2, BUSH_SIMPLE_3,
            BUSH_RED_FLOWERS, BUSH_BLUE_FLOWERS, BUSH_PINK_FLOWERS, BUSH_ORANGE_FLOWERS,
            FERN_1, FERN_2
        };
        return bushes[(int)(Math.random() * bushes.length)];
    }
    
    /**
     * Retorna uma ruína aleatória
     */
    public static DecorationType getRandomRuin() {
        DecorationType[] ruins = {
            RUINS_BROWN_1, RUINS_BROWN_2, RUINS_BROWN_3,
            RUINS_SAND_1, RUINS_SAND_2
        };
        return ruins[(int)(Math.random() * ruins.length)];
    }
}

