package items;

/**
 * Tipos de recursos que podem ser coletados no jogo
 * Define o que cada decoração/tile dropa quando coletado
 */
public enum ResourceType {
    // Recursos de árvores
    WOOD_COMMON("Madeira Comum", ItemType.WOOD, 2, 3),
    WOOD_FRUIT("Madeira de Árvore Frutífera", ItemType.WOOD, 2, 4),
    WOOD_PALM("Madeira de Palmeira", ItemType.WOOD, 1, 2),
    
    // Recursos de arbustos
    FIBER_GREEN("Fibra Verde", ItemType.FIBER, 1, 2),
    FIBER_FLOWER("Fibra com Flores", ItemType.FIBER, 1, 3),
    
    // Recursos de pedras
    STONE_COMMON("Pedra Comum", ItemType.STONE, 2, 4),
    STONE_RUIN("Pedra de Ruína", ItemType.STONE, 3, 5),
    
    // Recursos especiais (forrageamento)
    FORAGE_APPLE("Maçã", ItemType.APPLE, 1, 1),
    FORAGE_FIBER("Fibra Selvagem", ItemType.FIBER, 1, 1);
    
    private final String displayName;
    private final ItemType itemDropped;
    private final int minQuantity;
    private final int maxQuantity;
    
    ResourceType(String displayName, ItemType itemDropped, int minQuantity, int maxQuantity) {
        this.displayName = displayName;
        this.itemDropped = itemDropped;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public ItemType getItemDropped() {
        return itemDropped;
    }
    
    /**
     * Calcula quantidade aleatória de drop
     */
    public int getRandomQuantity() {
        if (minQuantity == maxQuantity) {
            return minQuantity;
        }
        return minQuantity + (int)(Math.random() * (maxQuantity - minQuantity + 1));
    }
    
    /**
     * Retorna a ferramenta necessária para coletar este recurso
     */
    public ItemType getRequiredTool() {
        String name = this.name();
        if (name.startsWith("WOOD_")) return ItemType.AXE;
        if (name.startsWith("STONE_")) return ItemType.PICKAXE;
        if (name.startsWith("FIBER_")) return null; // Pode coletar com as mãos
        if (name.startsWith("FORAGE_")) return null; // Forrageamento manual
        return null;
    }
    
    /**
     * Verifica se precisa de ferramenta
     */
    public boolean requiresTool() {
        return getRequiredTool() != null;
    }
}

