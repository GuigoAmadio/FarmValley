package systems;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;

import items.Item;
import items.ItemType;
import utils.SpriteLoader;

/**
 * Gerenciador de interface do usuário
 * Renderiza inventário, tooltips, menus, etc
 */
public class UIManager {
    private boolean inventoryOpen;
    private int hoveredSlot;
    private Image inventoryBackground;
    private Image slotFrame;
    
    // Dimensões do inventário
    private static final int SLOT_SIZE = 64;
    private static final int SLOT_PADDING = 8;
    private static final int INVENTORY_COLS = 6;
    private static final int INVENTORY_ROWS = 4;
    private static final int INVENTORY_WIDTH = INVENTORY_COLS * (SLOT_SIZE + SLOT_PADDING) + SLOT_PADDING * 2;
    private static final int INVENTORY_HEIGHT = INVENTORY_ROWS * (SLOT_SIZE + SLOT_PADDING) + SLOT_PADDING * 3 + 60;
    
    public UIManager() {
        this.inventoryOpen = false;
        this.hoveredSlot = -1;
        loadUISprites();
    }
    
    private void loadUISprites() {
        // Tentar carregar sprites de UI
        inventoryBackground = SpriteLoader.loadSprite("ui/inventory_bg.png");
        slotFrame = SpriteLoader.loadSprite("ui/slot.png");
    }
    
    // ===== GETTERS E SETTERS =====
    
    public boolean isInventoryOpen() {
        return inventoryOpen;
    }
    
    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
    }
    
    public void openInventory() {
        inventoryOpen = true;
    }
    
    public void closeInventory() {
        inventoryOpen = false;
    }
    
    public void setHoveredSlot(int slot) {
        this.hoveredSlot = slot;
    }
    
    // ===== RENDERIZAÇÃO =====
    
    /**
     * Renderiza a Hot Bar (barra rápida) sempre visível com efeitos profissionais
     */
    public void renderHotBar(GraphicsContext gc, Inventory inventory, int canvasWidth, int canvasHeight) {
        int hotBarSlots = 6;
        int hotBarWidth = hotBarSlots * (SLOT_SIZE + SLOT_PADDING) + SLOT_PADDING;
        int hotBarHeight = SLOT_SIZE + SLOT_PADDING * 2;
        int hotBarX = (canvasWidth - hotBarWidth) / 2;
        int hotBarY = canvasHeight - hotBarHeight - 10;
        
        // Sombra da hot bar
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(hotBarX + 3, hotBarY + 3, hotBarWidth, hotBarHeight, 10, 10);
        
        // Fundo da hot bar com gradiente simulado
        gc.setFill(Color.rgb(25, 25, 30, 0.95));
        gc.fillRoundRect(hotBarX, hotBarY, hotBarWidth, hotBarHeight, 8, 8);
        
        // Borda externa
        gc.setStroke(Color.rgb(60, 60, 65));
        gc.setLineWidth(2);
        gc.strokeRoundRect(hotBarX, hotBarY, hotBarWidth, hotBarHeight, 8, 8);
        
        // Borda interna brilhante
        gc.setStroke(Color.rgb(120, 120, 125, 0.6));
        gc.setLineWidth(1);
        gc.strokeRoundRect(hotBarX + 1, hotBarY + 1, hotBarWidth - 2, hotBarHeight - 2, 7, 7);
        
        // Desenhar slots da hot bar
        for (int i = 0; i < hotBarSlots; i++) {
            int x = hotBarX + SLOT_PADDING + i * (SLOT_SIZE + SLOT_PADDING);
            int y = hotBarY + SLOT_PADDING;
            
            drawHotBarSlot(gc, inventory, i, x, y);
        }
    }
    
    /**
     * Desenha um slot da hot bar
     */
    private void drawHotBarSlot(GraphicsContext gc, Inventory inventory, int slotIndex, int x, int y) {
        boolean isSelected = (slotIndex == inventory.getSelectedSlot());
        
        // Fundo do slot
        if (isSelected) {
            gc.setFill(Color.rgb(255, 215, 0, 0.4));
            gc.fillRoundRect(x - 3, y - 3, SLOT_SIZE + 6, SLOT_SIZE + 6, 8, 8);
        }
        
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 5, 5);
        
        // Borda
        gc.setStroke(isSelected ? Color.rgb(255, 215, 0) : Color.rgb(80, 80, 80));
        gc.setLineWidth(isSelected ? 3 : 2);
        gc.strokeRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 5, 5);
        
        // Número do slot
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText(String.valueOf(slotIndex + 1), x + 4, y + 4);
        
        // Item no slot
        Item item = inventory.getItem(slotIndex);
        if (item != null) {
            Image icon = item.getIcon();
            
            if (icon != null) {
                int iconSize = SLOT_SIZE - 16;
                gc.drawImage(icon, x + 8, y + 8, iconSize, iconSize);
            } else {
                // Fallback
                gc.setFill(getItemColor(item.getType()));
                gc.fillRoundRect(x + 12, y + 12, SLOT_SIZE - 24, SLOT_SIZE - 24, 3, 3);
            }
            
            // Quantidade
            if (item.isStackable() && item.getQuantity() > 1) {
                gc.setFill(Color.WHITE);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.setTextBaseline(VPos.BOTTOM);
                
                String quantityText = String.valueOf(item.getQuantity());
                gc.strokeText(quantityText, x + SLOT_SIZE - 4, y + SLOT_SIZE - 4);
                gc.fillText(quantityText, x + SLOT_SIZE - 4, y + SLOT_SIZE - 4);
            }
        }
    }
    
    /**
     * Renderiza o inventário completo
     */
    public void renderInventory(GraphicsContext gc, Inventory inventory, int canvasWidth, int canvasHeight) {
        if (!inventoryOpen) return;
        
        int invX = (canvasWidth - INVENTORY_WIDTH) / 2;
        int invY = (canvasHeight - INVENTORY_HEIGHT) / 2;
        
        // Fundo escurecido
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // Painel do inventário
        drawInventoryPanel(gc, invX, invY);
        
        // Título
        drawInventoryTitle(gc, invX, invY);
        
        // Slots
        drawInventorySlots(gc, inventory, invX, invY + 60);
        
        // Tooltip (se houver slot hovered)
        if (hoveredSlot >= 0 && hoveredSlot < inventory.getSize()) {
            Item item = inventory.getItem(hoveredSlot);
            if (item != null) {
                drawTooltip(gc, item, canvasWidth, canvasHeight);
            }
        }
        
        // Instruções
        drawInventoryInstructions(gc, invX, invY + INVENTORY_HEIGHT - 40);
    }
    
    /**
     * Desenha o painel de fundo do inventário
     */
    private void drawInventoryPanel(GraphicsContext gc, int x, int y) {
        if (inventoryBackground != null) {
            gc.drawImage(inventoryBackground, x, y, INVENTORY_WIDTH, INVENTORY_HEIGHT);
        } else {
            // Fallback: painel gradiente
            gc.setFill(Color.rgb(40, 35, 30));
            gc.fillRoundRect(x, y, INVENTORY_WIDTH, INVENTORY_HEIGHT, 10, 10);
            
            // Borda
            gc.setStroke(Color.rgb(139, 90, 43));
            gc.setLineWidth(4);
            gc.strokeRoundRect(x, y, INVENTORY_WIDTH, INVENTORY_HEIGHT, 10, 10);
            
            // Borda interna brilhante
            gc.setStroke(Color.rgb(205, 133, 63));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x + 2, y + 2, INVENTORY_WIDTH - 4, INVENTORY_HEIGHT - 4, 8, 8);
        }
    }
    
    /**
     * Desenha o título do inventário
     */
    private void drawInventoryTitle(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.rgb(255, 215, 0));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("INVENTÁRIO", x + INVENTORY_WIDTH / 2, y + 15);
    }
    
    /**
     * Desenha os slots do inventário
     */
    private void drawInventorySlots(GraphicsContext gc, Inventory inventory, int startX, int startY) {
        for (int row = 0; row < INVENTORY_ROWS; row++) {
            for (int col = 0; col < INVENTORY_COLS; col++) {
                int slotIndex = row * INVENTORY_COLS + col;
                int x = startX + SLOT_PADDING + col * (SLOT_SIZE + SLOT_PADDING);
                int y = startY + SLOT_PADDING + row * (SLOT_SIZE + SLOT_PADDING);
                
                drawSlot(gc, inventory, slotIndex, x, y);
            }
        }
    }
    
    /**
     * Desenha um slot individual
     */
    private void drawSlot(GraphicsContext gc, Inventory inventory, int slotIndex, int x, int y) {
        boolean isSelected = (slotIndex == inventory.getSelectedSlot());
        boolean isHovered = (slotIndex == hoveredSlot);
        
        // Fundo do slot
        if (isSelected) {
            gc.setFill(Color.rgb(255, 215, 0, 0.3));
            gc.fillRoundRect(x - 2, y - 2, SLOT_SIZE + 4, SLOT_SIZE + 4, 5, 5);
        }
        
        if (slotFrame != null) {
            gc.drawImage(slotFrame, x, y, SLOT_SIZE, SLOT_SIZE);
        } else {
            // Fallback: desenhar slot
            gc.setFill(Color.rgb(20, 20, 20, 0.8));
            gc.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 5, 5);
            
            gc.setStroke(isSelected ? Color.rgb(255, 215, 0) : Color.rgb(100, 100, 100));
            gc.setLineWidth(isSelected ? 3 : 2);
            gc.strokeRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 5, 5);
        }
        
        // Item no slot
        Item item = inventory.getItem(slotIndex);
        if (item != null) {
            Image icon = item.getIcon();
            
            if (icon != null) {
                // Desenhar ícone
                int iconSize = SLOT_SIZE - 12;
                gc.drawImage(icon, x + 6, y + 6, iconSize, iconSize);
            } else {
                // Fallback: desenhar quadrado colorido
                gc.setFill(getItemColor(item.getType()));
                gc.fillRoundRect(x + 10, y + 10, SLOT_SIZE - 20, SLOT_SIZE - 20, 3, 3);
            }
            
            // Quantidade (se empilhável e > 1)
            if (item.isStackable() && item.getQuantity() > 1) {
                gc.setFill(Color.WHITE);
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.setTextBaseline(VPos.BOTTOM);
                
                String quantityText = String.valueOf(item.getQuantity());
                gc.strokeText(quantityText, x + SLOT_SIZE - 6, y + SLOT_SIZE - 4);
                gc.fillText(quantityText, x + SLOT_SIZE - 6, y + SLOT_SIZE - 4);
            }
        }
        
        // Destaque ao passar o mouse
        if (isHovered) {
            gc.setStroke(Color.rgb(255, 255, 255, 0.5));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 5, 5);
        }
    }
    
    /**
     * Desenha tooltip de um item
     */
    private void drawTooltip(GraphicsContext gc, Item item, int canvasWidth, int canvasHeight) {
        String[] lines = item.getFullDescription().split("\n");
        
        int tooltipWidth = 250;
        int lineHeight = 20;
        int tooltipHeight = lines.length * lineHeight + 20;
        int tooltipX = (canvasWidth - tooltipWidth) / 2;
        int tooltipY = canvasHeight - tooltipHeight - 60;
        
        // Fundo
        gc.setFill(Color.rgb(30, 30, 30, 0.95));
        gc.fillRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 5, 5);
        
        // Borda
        gc.setStroke(Color.rgb(200, 200, 200));
        gc.setLineWidth(2);
        gc.strokeRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 5, 5);
        
        // Texto
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        
        for (int i = 0; i < lines.length; i++) {
            Color lineColor = (i == 0) ? Color.rgb(255, 215, 0) : Color.WHITE;
            gc.setFill(lineColor);
            gc.fillText(lines[i], tooltipX + 10, tooltipY + 10 + i * lineHeight);
        }
    }
    
    /**
     * Desenha instruções de uso
     */
    private void drawInventoryInstructions(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.rgb(200, 200, 200));
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText("[I] Fechar  |  [1-6] Selecionar Slot", x + INVENTORY_WIDTH / 2, y);
    }
    
    /**
     * Obtém cor fallback para um tipo de item
     */
    private Color getItemColor(ItemType type) {
        if (type.isSeed()) return Color.rgb(139, 98, 57);
        if (type.isCrop()) return Color.rgb(144, 238, 144);
        if (type.isTool()) return Color.rgb(192, 192, 192);
        if (type.isResource()) return Color.rgb(160, 82, 45);
        return Color.rgb(100, 100, 100);
    }
    
    /**
     * Calcula qual slot está sob o mouse (retorna -1 se nenhum)
     */
    public int getSlotAtPosition(double mouseX, double mouseY, int canvasWidth, int canvasHeight) {
        if (!inventoryOpen) return -1;
        
        int invX = (canvasWidth - INVENTORY_WIDTH) / 2;
        int invY = (canvasHeight - INVENTORY_HEIGHT) / 2 + 60;
        
        for (int row = 0; row < INVENTORY_ROWS; row++) {
            for (int col = 0; col < INVENTORY_COLS; col++) {
                int slotIndex = row * INVENTORY_COLS + col;
                int x = invX + SLOT_PADDING + col * (SLOT_SIZE + SLOT_PADDING);
                int y = invY + SLOT_PADDING + row * (SLOT_SIZE + SLOT_PADDING);
                
                if (mouseX >= x && mouseX <= x + SLOT_SIZE &&
                    mouseY >= y && mouseY <= y + SLOT_SIZE) {
                    return slotIndex;
                }
            }
        }
        
        return -1;
    }
}

