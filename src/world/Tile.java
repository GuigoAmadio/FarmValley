package world;

import entities.Crop;
import items.ResourceType;
import items.ItemType;
import types.CropType;

public class Tile {
    private TileType type;
    private Crop crop;
    private int x;
    private int y;
    private ResourceType resource;
    private int resourceHealth;
    private boolean resourceDepleted;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.crop = null;
        
        // Tiles de pedra têm recursos
        if (type == TileType.STONE) {
            this.resource = ResourceType.STONE_COMMON;
            this.resourceHealth = 3;
            this.resourceDepleted = false;
        } else {
            this.resource = null;
            this.resourceHealth = 0;
            this.resourceDepleted = true;
        }
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean hasCrop() {
        return crop != null;
    }

    public Crop getCrop() {
        return crop;
    }

    public void plantCrop(CropType cropType) {
        if (type == TileType.DIRT && crop == null) {
            crop = new Crop(cropType);
            type = TileType.PLANTED;
        }
    }

    public Crop harvestCrop() {
        if (crop != null && crop.isFullyGrown()) {
            Crop harvested = crop;
            crop = null;
            type = TileType.DIRT;
            return harvested;
        }
        return null;
    }

    public void growCrop() {
        if (crop != null) {
            crop.grow();
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    
    // ===== SISTEMA DE RECURSOS =====
    
    /**
     * Tenta coletar recurso deste tile
     * @param tool Ferramenta sendo usada
     * @return ResourceType se coletou com sucesso, null caso contrário
     */
    public ResourceType harvestResource(ItemType tool) {
        if (resourceDepleted || resource == null) {
            return null;
        }
        
        // Verificar ferramenta correta
        if (resource.requiresTool() && tool != resource.getRequiredTool()) {
            return null;
        }
        
        // Diminuir saúde
        resourceHealth--;
        
        // Se chegou a 0, remover pedra e marcar como depletado
        if (resourceHealth <= 0) {
            resourceDepleted = true;
            if (type == TileType.STONE) {
                type = TileType.GRASS; // Pedra vira grama quando quebrada
            }
        }
        
        return resource;
    }
    
    public boolean hasResource() {
        return !resourceDepleted && resource != null;
    }
    
    public ResourceType getResource() {
        return resource;
    }
    
    public int getResourceHealth() {
        return resourceHealth;
    }
}

