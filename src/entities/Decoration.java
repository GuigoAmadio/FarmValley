package entities;

import javafx.scene.image.Image;
import types.DecorationType;
import items.ItemType;
import items.ResourceType;
import utils.SpriteLoader;

/**
 * Representa uma decoração no mapa (árvores, arbustos, ruínas, etc)
 */
public class Decoration {
    private int x, y;  // Posição no grid
    private DecorationType type;
    private Image sprite;
    private ResourceType resource;
    private int health;  // Quantas vezes precisa coletar
    private int maxHealth;
    private boolean depleted;  // Se já foi completamente coletado
    
    public Decoration(int x, int y, DecorationType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sprite = loadSprite();
        this.resource = getResourceForType(type);
        this.maxHealth = getMaxHealthForType(type);
        this.health = maxHealth;
        this.depleted = false;
    }
    
    /**
     * Carrega o sprite da decoração
     */
    private Image loadSprite() {
        try {
            return SpriteLoader.loadSprite(type.getSpriteFile());
        } catch (Exception e) {
            System.out.println("Aviso: Sprite não encontrado para " + type.getSpriteFile());
            return null;
        }
    }
    
    // ===== GETTERS =====
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public DecorationType getType() {
        return type;
    }
    
    public Image getSprite() {
        return sprite;
    }
    
    public boolean isWalkable() {
        return type.isWalkable();
    }
    
    public int getLayer() {
        return type.getLayer();
    }
    
    public int getWidth() {
        return type.getWidth();
    }
    
    public int getHeight() {
        return type.getHeight();
    }
    
    /**
     * Verifica se uma posição colide com esta decoração
     * Considera o tamanho da decoração para ocupar múltiplos tiles
     */
    public boolean collidesWith(int posX, int posY) {
        // Tamanho do tile em pixels (deve corresponder ao TILE_SIZE do GameWindow)
        final int TILE_SIZE = 60;
        
        // Converter largura e altura da decoração (em pixels) para tiles
        // Usar Math.ceil para arredondar para cima, garantindo que decorações grandes ocupem tiles adicionais
        int widthInTiles = (int) Math.ceil((double) getWidth() / TILE_SIZE);
        int heightInTiles = (int) Math.ceil((double) getHeight() / TILE_SIZE);
        
        // Verificar se a posição está dentro da área ocupada pela decoração
        // x e y são a posição do canto superior esquerdo da decoração
        return posX >= x && posX < x + widthInTiles &&
               posY >= y && posY < y + heightInTiles;
    }
    
    // ===== SISTEMA DE RECURSOS =====
    
    /**
     * Determina qual recurso esta decoração dropa
     */
    private ResourceType getResourceForType(DecorationType type) {
        String name = type.name();
        
        // Árvores
        if (name.contains("TREE")) {
            if (name.contains("FRUIT")) return ResourceType.WOOD_FRUIT;
            if (name.contains("PALM")) return ResourceType.WOOD_PALM;
            return ResourceType.WOOD_COMMON;
        }
        
        // Arbustos
        if (name.contains("BUSH") || name.contains("FERN")) {
            if (name.contains("FLOWERS")) return ResourceType.FIBER_FLOWER;
            return ResourceType.FIBER_GREEN;
        }
        
        // Ruínas
        if (name.contains("RUINS")) {
            return ResourceType.STONE_RUIN;
        }
        
        return null;
    }
    
    /**
     * Determina a saúde máxima baseado no tipo
     */
    private int getMaxHealthForType(DecorationType type) {
        if (type.name().contains("TREE")) return 3;  // Árvores precisam de 3 hits
        if (type.name().contains("BUSH")) return 1;  // Arbustos 1 hit
        if (type.name().contains("RUINS")) return 5; // Ruínas 5 hits
        return 1;
    }
    
    /**
     * Tenta coletar recurso desta decoração
     * @param tool Ferramenta sendo usada (ou null para mãos vazias)
     * @return ResourceType se coletou com sucesso, null caso contrário
     */
    public ResourceType harvest(ItemType tool) {
        if (depleted || resource == null) {
            return null;
        }
        
        // Verificar se tem a ferramenta correta
        if (resource.requiresTool() && tool != resource.getRequiredTool()) {
            return null;  // Ferramenta errada ou faltando
        }
        
        // Diminuir saúde
        health--;
        
        // Se chegou a 0, marcar como depletado
        if (health <= 0) {
            depleted = true;
        }
        
        // Retornar o recurso coletado
        return resource;
    }
    
    public boolean isDepleted() {
        return depleted;
    }
    
    public ResourceType getResource() {
        return resource;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * Retorna progresso de coleta (0.0 a 1.0)
     */
    public double getHarvestProgress() {
        if (maxHealth == 0) return 1.0;
        return 1.0 - ((double)health / maxHealth);
    }
    
    public boolean canHarvest() {
        return !depleted && resource != null;
    }
}

