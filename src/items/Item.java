package items;

import javafx.scene.image.Image;
import utils.SpriteLoader;

/**
 * Representa um item no inventário
 * Pode ser empilhável ou não
 */
public class Item {
    private ItemType type;
    private int quantity;
    
    public Item(ItemType type, int quantity) {
        this.type = type;
        this.quantity = Math.min(quantity, type.getMaxStack());
    }
    
    public Item(ItemType type) {
        this(type, 1);
    }
    
    // ===== GETTERS E SETTERS =====
    
    public ItemType getType() {
        return type;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, Math.min(quantity, type.getMaxStack()));
    }
    
    public int getMaxStack() {
        return type.getMaxStack();
    }
    
    public boolean isStackable() {
        return type.isStackable();
    }
    
    public String getDisplayName() {
        return type.getDisplayName();
    }
    
    public String getDescription() {
        return type.getDescription();
    }
    
    public int getValue() {
        return type.getValue();
    }
    
    public int getTotalValue() {
        return type.getValue() * quantity;
    }
    
    // ===== MÉTODOS DE MANIPULAÇÃO =====
    
    /**
     * Adiciona quantidade ao item
     * @return Quantidade que não coube (overflow)
     */
    public int add(int amount) {
        int newQuantity = quantity + amount;
        int overflow = Math.max(0, newQuantity - type.getMaxStack());
        quantity = Math.min(newQuantity, type.getMaxStack());
        return overflow;
    }
    
    /**
     * Remove quantidade do item
     * @return True se conseguiu remover, false se não tinha quantidade suficiente
     */
    public boolean remove(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Verifica se pode empilhar com outro item
     */
    public boolean canStackWith(Item other) {
        return other != null && 
               this.type == other.type && 
               this.type.isStackable() &&
               this.quantity < this.type.getMaxStack();
    }
    
    /**
     * Empilha com outro item
     * @return Quantidade que não coube (overflow)
     */
    public int stackWith(Item other) {
        if (!canStackWith(other)) {
            return other.quantity;
        }
        return add(other.quantity);
    }
    
    /**
     * Verifica se o item está vazio (quantidade 0)
     */
    public boolean isEmpty() {
        return quantity <= 0;
    }
    
    /**
     * Verifica se o item está cheio (quantidade máxima)
     */
    public boolean isFull() {
        return quantity >= type.getMaxStack();
    }
    
    /**
     * Carrega o ícone do item
     */
    public Image getIcon() {
        return SpriteLoader.loadSprite(type.getIconFile());
    }
    
    /**
     * Clona o item
     */
    public Item clone() {
        return new Item(this.type, this.quantity);
    }
    
    @Override
    public String toString() {
        if (type.isStackable() && quantity > 1) {
            return type.getDisplayName() + " x" + quantity;
        }
        return type.getDisplayName();
    }
    
    /**
     * Descrição completa para tooltips
     */
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.getDisplayName());
        if (type.isStackable() && quantity > 1) {
            sb.append(" x").append(quantity);
        }
        sb.append("\n").append(type.getDescription());
        sb.append("\nValor: ").append(getTotalValue()).append(" moedas");
        return sb.toString();
    }
}

