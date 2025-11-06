package systems;

import world.Farm;
import world.Tile;
import entities.Player;
import entities.Decoration;
import items.Item;
import items.ItemType;
import items.ResourceType;

/**
 * Sistema de coleta de recursos
 * Gerencia a coleta de decorações e tiles com recursos
 */
public class HarvestSystem {
    private Farm farm;
    private Player player;
    
    public HarvestSystem(Farm farm, Player player) {
        this.farm = farm;
        this.player = player;
    }
    
    /**
     * Tenta coletar recursos na posição do jogador OU no tile adjacente
     * @return Mensagem de resultado da coleta
     */
    public String attemptHarvest() {
        int playerX = player.getX();
        int playerY = player.getY();
        
        // Determinar ferramenta equipada (slot selecionado)
        Item selectedItem = player.getInventory().getSelectedItem();
        ItemType tool = (selectedItem != null && selectedItem.getType().isTool()) 
                        ? selectedItem.getType() 
                        : null;
        
        // Tentar coletar no tile do player primeiro
        String result = tryHarvestAt(playerX, playerY, tool);
        if (result != null) {
            return result;
        }
        
        // Tentar coletar no tile adjacente (baseado na direção)
        int targetX = playerX;
        int targetY = playerY;
        
        switch (player.getFacing()) {
            case UP:    targetY--; break;
            case DOWN:  targetY++; break;
            case LEFT:  targetX--; break;
            case RIGHT: targetX++; break;
        }
        
        result = tryHarvestAt(targetX, targetY, tool);
        if (result != null) {
            return result;
        }
        
        return "Nada para coletar aqui!";
    }
    
    /**
     * Tenta coletar em uma posição específica
     */
    private String tryHarvestAt(int x, int y, ItemType tool) {
        // Tentar coletar de decoração primeiro
        String decorResult = harvestDecoration(x, y, tool);
        if (decorResult != null) {
            return decorResult;
        }
        
        // Tentar coletar de tile
        String tileResult = harvestTile(x, y, tool);
        if (tileResult != null) {
            return tileResult;
        }
        
        return null;
    }
    
    /**
     * Tenta coletar de decoração
     */
    private String harvestDecoration(int x, int y, ItemType tool) {
        DecorationManager decorManager = farm.getDecorationManager();
        
        for (Decoration deco : decorManager.getAllDecorations()) {
            if (deco.collidesWith(x, y) && deco.canHarvest()) {
                // Verificar energia antes de coletar
                int energyCost = getEnergyCost(deco.getResource(), tool);
                if (!player.hasEnergy(energyCost)) {
                    return "Sem energia! Descanse [Z]";
                }
                
                ResourceType resource = deco.harvest(tool);
                
                if (resource == null) {
                    // Ferramenta errada
                    ItemType requiredTool = deco.getResource().getRequiredTool();
                    if (requiredTool != null) {
                        return "Você precisa de: " + requiredTool.getDisplayName();
                    }
                    return "Não pode coletar isso!";
                }
                
                // Consumir energia
                player.useEnergy(energyCost);
                
                // Coletar recursos
                int quantity = resource.getRandomQuantity();
                boolean added = player.getInventory().addItem(resource.getItemDropped(), quantity);
                
                if (!added) {
                    return "Inventário cheio!";
                }
                
                // Mensagem de sucesso
                String itemName = resource.getItemDropped().getDisplayName();
                String statusMsg = "+" + quantity + " " + itemName;
                
                if (deco.isDepleted()) {
                    statusMsg += " (Coletado!) -" + energyCost + " ⚡";
                } else {
                    statusMsg += " (" + deco.getHealth() + "/" + deco.getMaxHealth() + ") -" + energyCost + " ⚡";
                }
                
                return statusMsg;
            }
        }
        
        return null;
    }
    
    /**
     * Tenta coletar de tile
     */
    private String harvestTile(int x, int y, ItemType tool) {
        Tile tile = farm.getTile(x, y);
        
        if (tile != null && tile.hasResource()) {
            // Verificar energia antes de coletar
            int energyCost = getEnergyCost(tile.getResource(), tool);
            if (!player.hasEnergy(energyCost)) {
                return "Sem energia! Descanse [Z]";
            }
            
            ResourceType resource = tile.harvestResource(tool);
            
            if (resource == null) {
                // Ferramenta errada
                ItemType requiredTool = tile.getResource().getRequiredTool();
                if (requiredTool != null) {
                    return "Você precisa de: " + requiredTool.getDisplayName();
                }
                return "Não pode coletar isso!";
            }
            
            // Consumir energia
            player.useEnergy(energyCost);
            
            // Coletar recursos
            int quantity = resource.getRandomQuantity();
            boolean added = player.getInventory().addItem(resource.getItemDropped(), quantity);
            
            if (!added) {
                return "Inventário cheio!";
            }
            
            // Mensagem de sucesso
            String itemName = resource.getItemDropped().getDisplayName();
            String statusMsg = "+" + quantity + " " + itemName;
            
            if (tile.getResourceHealth() > 0) {
                statusMsg += " (" + tile.getResourceHealth() + " restante) -" + energyCost + " ⚡";
            } else {
                statusMsg += " (Quebrado!) -" + energyCost + " ⚡";
            }
            
            return statusMsg;
        }
        
        return null;
    }
    
    /**
     * Calcula custo de energia baseado no recurso e ferramenta
     */
    private int getEnergyCost(ResourceType resource, ItemType tool) {
        String resourceName = resource.name();
        
        // Madeira (árvores)
        if (resourceName.contains("WOOD")) {
            return tool == ItemType.AXE ? 4 : 8; // Menos energia com ferramenta certa
        }
        
        // Pedra
        if (resourceName.contains("STONE")) {
            return tool == ItemType.PICKAXE ? 5 : 10;
        }
        
        // Fibra (arbustos) - pouca energia
        if (resourceName.contains("FIBER")) {
            return 2;
        }
        
        // Forrageamento - muito pouco
        if (resourceName.contains("FORAGE")) {
            return 1;
        }
        
        return 3; // Padrão
    }
    
    /**
     * Verifica se há algo coletável na posição
     */
    public boolean hasHarvestableAt(int x, int y) {
        // Verificar decoração
        DecorationManager decorManager = farm.getDecorationManager();
        for (Decoration deco : decorManager.getAllDecorations()) {
            if (deco.collidesWith(x, y) && deco.canHarvest()) {
                return true;
            }
        }
        
        // Verificar tile
        Tile tile = farm.getTile(x, y);
        if (tile != null && tile.hasResource()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Retorna informação sobre o recurso na posição
     */
    public String getResourceInfoAt(int x, int y) {
        // Verificar decoração
        DecorationManager decorManager = farm.getDecorationManager();
        for (Decoration deco : decorManager.getAllDecorations()) {
            if (deco.collidesWith(x, y) && deco.canHarvest()) {
                ResourceType res = deco.getResource();
                ItemType tool = res.getRequiredTool();
                String toolName = (tool != null) ? tool.getDisplayName() : "Mãos";
                return res.getDisplayName() + " (Ferramenta: " + toolName + ")";
            }
        }
        
        // Verificar tile
        Tile tile = farm.getTile(x, y);
        if (tile != null && tile.hasResource()) {
            ResourceType res = tile.getResource();
            ItemType tool = res.getRequiredTool();
            String toolName = (tool != null) ? tool.getDisplayName() : "Mãos";
            return res.getDisplayName() + " (Ferramenta: " + toolName + ")";
        }
        
        return null;
    }
}

