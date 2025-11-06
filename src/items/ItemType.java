package items;

import types.CropType;

/**
 * Tipos de itens disponíveis no jogo
 * Cada tipo tem nome, descrição, ícone, empilhável, e valor
 */
public enum ItemType {
    // ===== SEMENTES =====
    WHEAT_SEED("Semente de Trigo", "Planta trigo (3 dias)", "icons/wheat_seed.png", true, 20, 99),
    TOMATO_SEED("Semente de Tomate", "Planta tomate (5 dias)", "icons/tomato_seed.png", true, 30, 99),
    CORN_SEED("Semente de Milho", "Planta milho (7 dias)", "icons/corn_seed.png", true, 50, 99),
    CARROT_SEED("Semente de Cenoura", "Planta cenoura (4 dias)", "icons/carrot_seed.png", true, 25, 99),
    
    // ===== COLHEITAS =====
    WHEAT("Trigo", "Trigo colhido", "icons/wheat.png", true, 50, 99),
    TOMATO("Tomate", "Tomate maduro", "icons/tomato.png", true, 80, 99),
    CORN("Milho", "Milho dourado", "icons/corn.png", true, 120, 99),
    CARROT("Cenoura", "Cenoura fresca", "icons/carrot.png", true, 60, 99),
    
    // ===== RECURSOS =====
    WOOD("Madeira", "Madeira de árvore", "icons/wood.png", true, 10, 99),
    STONE("Pedra", "Pedra bruta", "icons/stone.png", true, 5, 99),
    FIBER("Fibra", "Fibra vegetal de arbustos", "icons/fiber.png", true, 3, 99),
    
    // ===== FERRAMENTAS =====
    AXE("Machado", "Corta árvores", "icons/axe.png", false, 100, 1),
    PICKAXE("Picareta", "Quebra pedras", "icons/pickaxe.png", false, 100, 1),
    HOE("Enxada", "Ara a terra", "icons/hoe.png", false, 50, 1),
    WATERING_CAN("Regador", "Rega plantas", "icons/watering_can.png", false, 50, 1),
    
    // ===== CONSUMÍVEIS =====
    BREAD("Pão", "Restaura 20 de energia", "icons/bread.png", true, 30, 20),
    APPLE("Maçã", "Restaura 10 de energia", "icons/apple.png", true, 15, 50),
    
    // ===== ESPECIAIS =====
    GOLD_COIN("Moeda de Ouro", "Moeda do jogo", "icons/coin.png", true, 1, 9999);
    
    // ===== PROPRIEDADES =====
    
    private final String displayName;
    private final String description;
    private final String iconFile;
    private final boolean stackable;
    private final int value;  // Preço base
    private final int maxStack;  // Quantidade máxima por pilha
    
    ItemType(String displayName, String description, String iconFile, boolean stackable, int value, int maxStack) {
        this.displayName = displayName;
        this.description = description;
        this.iconFile = iconFile;
        this.stackable = stackable;
        this.value = value;
        this.maxStack = maxStack;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getIconFile() {
        return iconFile;
    }
    
    public boolean isStackable() {
        return stackable;
    }
    
    public int getValue() {
        return value;
    }
    
    public int getMaxStack() {
        return maxStack;
    }
    
    public boolean isSeed() {
        return this == WHEAT_SEED || this == TOMATO_SEED || this == CORN_SEED || this == CARROT_SEED;
    }
    
    public boolean isCrop() {
        return this == WHEAT || this == TOMATO || this == CORN || this == CARROT;
    }
    
    public boolean isTool() {
        return this == AXE || this == PICKAXE || this == HOE || this == WATERING_CAN;
    }
    
    public boolean isResource() {
        return this == WOOD || this == STONE || this == FIBER;
    }
    
    /**
     * Converte CropType para ItemType da semente
     */
    public static ItemType fromCropTypeToSeed(CropType cropType) {
        switch (cropType) {
            case WHEAT: return WHEAT_SEED;
            case TOMATO: return TOMATO_SEED;
            case CORN: return CORN_SEED;
            case CARROT: return CARROT_SEED;
            default: return null;
        }
    }
    
    /**
     * Converte CropType para ItemType da colheita
     */
    public static ItemType fromCropTypeToCrop(CropType cropType) {
        switch (cropType) {
            case WHEAT: return WHEAT;
            case TOMATO: return TOMATO;
            case CORN: return CORN;
            case CARROT: return CARROT;
            default: return null;
        }
    }
    
    /**
     * Converte ItemType de semente para CropType
     */
    public CropType toCropType() {
        switch (this) {
            case WHEAT_SEED: return CropType.WHEAT;
            case TOMATO_SEED: return CropType.TOMATO;
            case CORN_SEED: return CropType.CORN;
            case CARROT_SEED: return CropType.CARROT;
            default: return null;
        }
    }
}

