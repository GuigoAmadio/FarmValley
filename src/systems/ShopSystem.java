package systems;

import java.util.ArrayList;
import java.util.List;

import entities.Player;
import types.CropType;
import items.ItemType;

/**
 * Sistema de loja para compra e venda de itens
 */
public class ShopSystem {
    private List<ShopItem> itemsForSale;
    private boolean isOpen;
    private Player player;
    
    public ShopSystem(Player player) {
        this.player = player;
        this.itemsForSale = new ArrayList<>();
        this.isOpen = false;
        
        initializeShop();
    }
    
    private void initializeShop() {
        // Sementes
        itemsForSale.add(new ShopItem("Semente de Trigo", ItemType.WHEAT_SEED, 10, 5, ShopCategory.SEEDS));
        itemsForSale.add(new ShopItem("Semente de Tomate", ItemType.TOMATO_SEED, 25, 12, ShopCategory.SEEDS));
        itemsForSale.add(new ShopItem("Semente de Milho", ItemType.CORN_SEED, 40, 20, ShopCategory.SEEDS));
        itemsForSale.add(new ShopItem("Semente de Cenoura", ItemType.CARROT_SEED, 30, 15, ShopCategory.SEEDS));
        
        // Ferramentas (futuro)
        itemsForSale.add(new ShopItem("Enxada Básica", ItemType.HOE, 100, 50, ShopCategory.TOOLS));
        itemsForSale.add(new ShopItem("Machado Básico", ItemType.AXE, 150, 75, ShopCategory.TOOLS));
        itemsForSale.add(new ShopItem("Picareta Básica", ItemType.PICKAXE, 150, 75, ShopCategory.TOOLS));
        
        // Consumíveis
        itemsForSale.add(new ShopItem("Poção de Energia", ItemType.ENERGY_POTION, 50, 25, ShopCategory.CONSUMABLES));
        itemsForSale.add(new ShopItem("Poção de Vida", ItemType.HEALTH_POTION, 75, 35, ShopCategory.CONSUMABLES));
    }
    
    /**
     * Abre a loja
     */
    public void open() {
        isOpen = true;
    }
    
    /**
     * Fecha a loja
     */
    public void close() {
        isOpen = false;
    }
    
    /**
     * Tenta comprar um item
     */
    public BuyResult buyItem(int itemIndex) {
        if (itemIndex < 0 || itemIndex >= itemsForSale.size()) {
            return new BuyResult(false, "Item inválido!");
        }
        
        ShopItem item = itemsForSale.get(itemIndex);
        
        if (player.getMoney() < item.getBuyPrice()) {
            return new BuyResult(false, "Dinheiro insuficiente! Precisa de $" + item.getBuyPrice());
        }
        
        // Verificar se tem espaço no inventário
        if (!player.getInventory().canAddItem(item.getItemType(), 1)) {
            return new BuyResult(false, "Inventário cheio!");
        }
        
        // Realizar compra
        player.spend(item.getBuyPrice());
        player.getInventory().addItem(item.getItemType(), 1);
        
        return new BuyResult(true, "Comprou " + item.getName() + " por $" + item.getBuyPrice() + "!");
    }
    
    /**
     * Vende itens do inventário
     */
    public SellResult sellItem(ItemType itemType, int quantity) {
        int currentQuantity = player.getInventory().getItemCount(itemType);
        
        if (currentQuantity < quantity) {
            return new SellResult(false, 0, "Você não tem " + quantity + " desse item!");
        }
        
        // Encontrar preço de venda
        int sellPrice = getSellPrice(itemType);
        if (sellPrice <= 0) {
            return new SellResult(false, 0, "Este item não pode ser vendido!");
        }
        
        int totalPrice = sellPrice * quantity;
        
        // Realizar venda
        player.getInventory().removeItem(itemType, quantity);
        player.earn(totalPrice);
        
        return new SellResult(true, totalPrice, "Vendeu " + quantity + "x por $" + totalPrice + "!");
    }
    
    /**
     * Obtém preço de venda de um item
     */
    public int getSellPrice(ItemType itemType) {
        // Procurar na lista da loja
        for (ShopItem item : itemsForSale) {
            if (item.getItemType() == itemType) {
                return item.getSellPrice();
            }
        }
        
        // Preços padrão para colheitas
        switch (itemType) {
            case WHEAT: return 25;
            case TOMATO: return 60;
            case CORN: return 100;
            case CARROT: return 75;
            case WOOD: return 10;
            case STONE: return 15;
            case FIBER: return 5;
            default: return 0;
        }
    }
    
    // Getters
    public List<ShopItem> getItemsForSale() { return itemsForSale; }
    public boolean isOpen() { return isOpen; }
    public void toggle() { isOpen = !isOpen; }
    
    // ===== Classes Internas =====
    
    public enum ShopCategory {
        SEEDS("Sementes"),
        TOOLS("Ferramentas"),
        CONSUMABLES("Consumíveis"),
        MATERIALS("Materiais");
        
        private final String name;
        ShopCategory(String name) { this.name = name; }
        public String getName() { return name; }
    }
    
    public static class ShopItem {
        private String name;
        private ItemType itemType;
        private int buyPrice;
        private int sellPrice;
        private ShopCategory category;
        
        public ShopItem(String name, ItemType itemType, int buyPrice, int sellPrice, ShopCategory category) {
            this.name = name;
            this.itemType = itemType;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.category = category;
        }
        
        public String getName() { return name; }
        public ItemType getItemType() { return itemType; }
        public int getBuyPrice() { return buyPrice; }
        public int getSellPrice() { return sellPrice; }
        public ShopCategory getCategory() { return category; }
    }
    
    public static class BuyResult {
        public final boolean success;
        public final String message;
        
        public BuyResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
    
    public static class SellResult {
        public final boolean success;
        public final int amount;
        public final String message;
        
        public SellResult(boolean success, int amount, String message) {
            this.success = success;
            this.amount = amount;
            this.message = message;
        }
    }
}

