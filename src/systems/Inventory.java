package systems;

import java.util.ArrayList;
import java.util.List;

import items.Item;
import items.ItemType;

/**
 * Sistema de inventário do jogador
 * Gerencia slots, empilhamento, e operações de itens
 */
public class Inventory {
    private Item[] slots;
    private int size;
    private int selectedSlot;
    
    public Inventory(int size) {
        this.size = size;
        this.slots = new Item[size];
        this.selectedSlot = 0;
    }
    
    // ===== GETTERS =====
    
    public int getSize() {
        return size;
    }
    
    public int getSelectedSlot() {
        return selectedSlot;
    }
    
    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < size) {
            this.selectedSlot = slot;
        }
    }
    
    public Item getItem(int slot) {
        if (slot >= 0 && slot < size) {
            return slots[slot];
        }
        return null;
    }
    
    public Item getSelectedItem() {
        return getItem(selectedSlot);
    }
    
    public Item[] getAllSlots() {
        return slots;
    }
    
    // ===== ADICIONAR ITENS =====
    
    /**
     * Adiciona um item ao inventário
     * Tenta empilhar primeiro, depois encontra slot vazio
     * @return True se conseguiu adicionar completamente
     */
    public boolean addItem(Item item) {
        if (item == null || item.isEmpty()) {
            return true;
        }
        
        int remainingQuantity = item.getQuantity();
        
        // Primeira passada: tentar empilhar em slots existentes
        if (item.isStackable()) {
            for (int i = 0; i < size; i++) {
                if (slots[i] != null && slots[i].canStackWith(item)) {
                    int overflow = slots[i].add(remainingQuantity);
                    remainingQuantity = overflow;
                    if (remainingQuantity <= 0) {
                        return true; // Tudo foi adicionado
                    }
                }
            }
        }
        
        // Segunda passada: colocar em slots vazios
        while (remainingQuantity > 0) {
            int emptySlot = findEmptySlot();
            if (emptySlot == -1) {
                return false; // Inventário cheio
            }
            
            int quantityToAdd = Math.min(remainingQuantity, item.getMaxStack());
            slots[emptySlot] = new Item(item.getType(), quantityToAdd);
            remainingQuantity -= quantityToAdd;
        }
        
        return true;
    }
    
    /**
     * Adiciona um item por tipo e quantidade
     */
    public boolean addItem(ItemType type, int quantity) {
        return addItem(new Item(type, quantity));
    }
    
    /**
     * Adiciona um item por tipo (quantidade 1)
     */
    public boolean addItem(ItemType type) {
        return addItem(type, 1);
    }
    
    // ===== REMOVER ITENS =====
    
    /**
     * Remove quantidade de um tipo de item do inventário
     * @return True se conseguiu remover
     */
    public boolean removeItem(ItemType type, int quantity) {
        int remainingToRemove = quantity;
        
        // Percorrer todos os slots
        for (int i = 0; i < size; i++) {
            if (slots[i] != null && slots[i].getType() == type) {
                int amountInSlot = slots[i].getQuantity();
                
                if (amountInSlot <= remainingToRemove) {
                    // Remover tudo deste slot
                    remainingToRemove -= amountInSlot;
                    slots[i] = null;
                } else {
                    // Remover parcialmente
                    slots[i].remove(remainingToRemove);
                    remainingToRemove = 0;
                }
                
                if (remainingToRemove <= 0) {
                    return true;
                }
            }
        }
        
        return remainingToRemove <= 0;
    }
    
    /**
     * Remove item de um slot específico
     */
    public Item removeItemFromSlot(int slot) {
        if (slot >= 0 && slot < size) {
            Item item = slots[slot];
            slots[slot] = null;
            return item;
        }
        return null;
    }
    
    /**
     * Remove 1 quantidade do item selecionado
     */
    public boolean removeOneFromSelected() {
        if (slots[selectedSlot] != null) {
            slots[selectedSlot].remove(1);
            if (slots[selectedSlot].isEmpty()) {
                slots[selectedSlot] = null;
            }
            return true;
        }
        return false;
    }
    
    // ===== CONSULTAS =====
    
    /**
     * Conta quantos itens de um tipo existem no inventário
     */
    public int countItem(ItemType type) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (slots[i] != null && slots[i].getType() == type) {
                count += slots[i].getQuantity();
            }
        }
        return count;
    }
    
    /**
     * Verifica se tem pelo menos X de um item
     */
    public boolean hasItem(ItemType type, int quantity) {
        return countItem(type) >= quantity;
    }
    
    /**
     * Verifica se tem um item (quantidade >= 1)
     */
    public boolean hasItem(ItemType type) {
        return hasItem(type, 1);
    }
    
    /**
     * Encontra o primeiro slot vazio
     */
    public int findEmptySlot() {
        for (int i = 0; i < size; i++) {
            if (slots[i] == null) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Verifica se o inventário está cheio
     */
    public boolean isFull() {
        return findEmptySlot() == -1;
    }
    
    /**
     * Verifica se o inventário está vazio
     */
    public boolean isEmpty() {
        for (int i = 0; i < size; i++) {
            if (slots[i] != null) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Conta slots ocupados
     */
    public int getUsedSlots() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (slots[i] != null) {
                count++;
            }
        }
        return count;
    }
    
    // ===== MANIPULAÇÃO DE SLOTS =====
    
    /**
     * Troca dois slots de posição
     */
    public void swapSlots(int slot1, int slot2) {
        if (slot1 >= 0 && slot1 < size && slot2 >= 0 && slot2 < size) {
            Item temp = slots[slot1];
            slots[slot1] = slots[slot2];
            slots[slot2] = temp;
        }
    }
    
    /**
     * Tenta empilhar slot1 em slot2
     */
    public void stackSlots(int slot1, int slot2) {
        if (slot1 == slot2) return;
        
        Item item1 = slots[slot1];
        Item item2 = slots[slot2];
        
        if (item1 == null) return;
        
        if (item2 == null) {
            // Slot 2 vazio, mover item1 para lá
            slots[slot2] = item1;
            slots[slot1] = null;
        } else if (item1.canStackWith(item2)) {
            // Tentar empilhar
            int overflow = item2.stackWith(item1);
            if (overflow <= 0) {
                slots[slot1] = null;
            } else {
                item1.setQuantity(overflow);
            }
        } else {
            // Itens diferentes, trocar
            swapSlots(slot1, slot2);
        }
    }
    
    /**
     * Limpa o inventário
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            slots[i] = null;
        }
    }
    
    /**
     * Retorna lista de todos os itens (para debug)
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (slots[i] != null) {
                items.add(slots[i]);
            }
        }
        return items;
    }
    
    @Override
    public String toString() {
        return "Inventário: " + getUsedSlots() + "/" + size + " slots usados";
    }
}

