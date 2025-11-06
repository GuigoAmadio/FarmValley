package core;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

import entities.Player;
import entities.Crop;
import world.Farm;
import world.Tile;
import world.TileType;
import world.TimeSystem;
import types.CropType;
import systems.UIManager;
import systems.HarvestSystem;
import systems.Inventory;
import items.ItemType;

public class GameEngine {
    private Farm farm;
    private Player player;
    private TimeSystem timeSystem;
    private UIManager uiManager;
    private HarvestSystem harvestSystem;
    private Set<KeyCode> pressedKeys;
    private boolean[] actionFlags;
    private String statusMessage;
    private long messageTime;
    
    // Movimento tile-por-tile mais fluido
    private long lastMoveTime;
    private static final long MOVE_DELAY = 100_000_000; // 100ms entre movimentos (mais rápido!)

    private static final int ACTION_TILL = 0;
    private static final int ACTION_PLANT = 1;
    private static final int ACTION_HARVEST = 2;
    private static final int ACTION_SLEEP = 3;
    private static final int ACTION_CHANGE_SEED = 4;
    private static final int ACTION_COLLECT_RESOURCE = 5;

    public GameEngine() {
        // Mapa pode ser facilmente expandido agora!
        // Pequeno: 50x50, Médio: 100x100, Grande: 200x200, Muito Grande: 500x500
        int mapWidth = 100;  // Mude aqui para testar diferentes tamanhos
        int mapHeight = 100;
        
        farm = new Farm(mapWidth, mapHeight);
        
        // Spawn no centro do mapa
        player = new Player(mapWidth / 2, mapHeight / 2);
        timeSystem = new TimeSystem();
        uiManager = new UIManager();
        harvestSystem = new HarvestSystem(farm, player);
        pressedKeys = new HashSet<>();
        actionFlags = new boolean[6];  // Aumentado para 6 ações
        statusMessage = "";
        messageTime = 0;
        lastMoveTime = 0;
    }

    public void update() {
        handleMovement();
        handleActions();
    }

    private void handleMovement() {
        long currentTime = System.nanoTime();
        
        // Delay entre movimentos para controle melhor (mais rápido agora!)
        if (currentTime - lastMoveTime < MOVE_DELAY) {
            return;
        }
        
        int newX = player.getX();
        int newY = player.getY();
        int dx = 0, dy = 0;
        boolean moved = false;

        // MOVIMENTO DIAGONAL SUPORTADO! (removido else if)
        if (pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.UP)) {
            newY--;
            dy = -1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.S) || pressedKeys.contains(KeyCode.DOWN)) {
            newY++;
            dy = 1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.A) || pressedKeys.contains(KeyCode.LEFT)) {
            newX--;
            dx = -1;
            moved = true;
        }
        if (pressedKeys.contains(KeyCode.D) || pressedKeys.contains(KeyCode.RIGHT)) {
            newX++;
            dx = 1;
            moved = true;
        }

        if (moved) {
            // Atualizar direção (prioriza vertical para animação)
            player.updateDirection(dx, dy);
            
            // Tentar movimento diagonal ou simples
            if (farm.isWalkable(newX, newY)) {
                player.setX(newX);
                player.setY(newY);
                player.updateAnimation(); // Animar enquanto anda
                lastMoveTime = currentTime;
            } else if (dx != 0 && dy != 0) {
                // Se diagonal falhou, tentar movimento em X ou Y separadamente
                if (farm.isWalkable(newX, player.getY())) {
                    player.setX(newX);
                    player.updateAnimation();
                    lastMoveTime = currentTime;
                } else if (farm.isWalkable(player.getX(), newY)) {
                    player.setY(newY);
                    player.updateAnimation();
                    lastMoveTime = currentTime;
                }
            }
        } else {
            // Parado - resetar animação
            player.resetAnimation();
        }
    }

    private void handleActions() {
        int playerX = player.getX();
        int playerY = player.getY();

        // Arar terra (T)
        if (actionFlags[ACTION_TILL]) {
            if (player.hasEnergy(5)) {
                Tile tile = farm.getTile(playerX, playerY);
                if (tile != null && tile.getType() == TileType.GRASS) {
                    farm.tillSoil(playerX, playerY);
                    player.useEnergy(5);
                    showMessage("Terra arada!");
                } else {
                    showMessage("Não posso arar aqui!");
                }
            } else {
                showMessage("Sem energia! Durma (Z)");
            }
            actionFlags[ACTION_TILL] = false;
        }

        // Plantar (P)
        if (actionFlags[ACTION_PLANT]) {
            CropType seed = player.getSelectedSeed();
            if (!player.canAfford(seed.getSeedCost())) {
                showMessage("Dinheiro insuficiente! ($" + seed.getSeedCost() + ")");
            } else if (!player.hasEnergy(5)) {
                showMessage("Sem energia! Durma (Z)");
            } else {
                if (farm.plantCrop(playerX, playerY, seed)) {
                    player.spend(seed.getSeedCost());
                    player.useEnergy(5);
                    showMessage(seed.getName() + " plantado! (-$" + seed.getSeedCost() + ")");
                } else {
                    showMessage("Are a terra primeiro (T)!");
                }
            }
            actionFlags[ACTION_PLANT] = false;
        }

        // Colher (H)
        if (actionFlags[ACTION_HARVEST]) {
            Crop harvested = farm.harvestCrop(playerX, playerY);
            if (harvested != null) {
                int price = harvested.getType().getSellPrice();
                player.earn(price);
                player.useEnergy(3);
                showMessage(harvested.getType().getName() + " colhido! (+$" + price + ")");
            } else {
                Tile tile = farm.getTile(playerX, playerY);
                if (tile != null && tile.hasCrop()) {
                    showMessage("Planta ainda não está madura!");
                } else {
                    showMessage("Nada para colher aqui!");
                }
            }
            actionFlags[ACTION_HARVEST] = false;
        }

        // Dormir (Z)
        if (actionFlags[ACTION_SLEEP]) {
            sleep();
            actionFlags[ACTION_SLEEP] = false;
        }

        // Trocar semente (C)
        if (actionFlags[ACTION_CHANGE_SEED]) {
            changeSeed();
            actionFlags[ACTION_CHANGE_SEED] = false;
        }
        
        // Coletar recursos (E ou SPACE)
        if (actionFlags[ACTION_COLLECT_RESOURCE]) {
            collectResource();
            actionFlags[ACTION_COLLECT_RESOURCE] = false;
        }
    }

    private void sleep() {
        player.sleep();
        farm.growAllCrops();
        timeSystem.nextDay();
        showMessage("Bom dia! " + timeSystem.getDateString());
    }

    private void changeSeed() {
        CropType[] types = CropType.values();
        CropType current = player.getSelectedSeed();
        
        for (int i = 0; i < types.length; i++) {
            if (types[i] == current) {
                int nextIndex = (i + 1) % types.length;
                player.setSelectedSeed(types[nextIndex]);
                showMessage("Semente: " + types[nextIndex].getName());
                break;
            }
        }
    }
    
    private void collectResource() {
        String result = harvestSystem.attemptHarvest();
        showMessage(result);
    }
    
    private void showMessage(String message) {
        this.statusMessage = message;
        this.messageTime = System.currentTimeMillis();
    }
    
    public String getStatusMessage() {
        // Mensagem desaparece após 3 segundos
        if (System.currentTimeMillis() - messageTime > 3000) {
            return "";
        }
        return statusMessage;
    }

    public void keyPressed(KeyCode code) {
        pressedKeys.add(code);

        // UI - Inventário
        if (code == KeyCode.I) {
            uiManager.toggleInventory();
            return; // Não processar outras ações quando inventário está aberto
        }
        
        // Se inventário estiver aberto, não processar ações de jogo
        if (uiManager.isInventoryOpen()) {
            // Teclas numéricas para selecionar slots (1-6)
            if (code == KeyCode.DIGIT1) player.getInventory().setSelectedSlot(0);
            if (code == KeyCode.DIGIT2) player.getInventory().setSelectedSlot(1);
            if (code == KeyCode.DIGIT3) player.getInventory().setSelectedSlot(2);
            if (code == KeyCode.DIGIT4) player.getInventory().setSelectedSlot(3);
            if (code == KeyCode.DIGIT5) player.getInventory().setSelectedSlot(4);
            if (code == KeyCode.DIGIT6) player.getInventory().setSelectedSlot(5);
            return;
        }

        // Ações instantâneas de jogo
        if (code == KeyCode.T) actionFlags[ACTION_TILL] = true;
        if (code == KeyCode.P) actionFlags[ACTION_PLANT] = true;
        if (code == KeyCode.H) actionFlags[ACTION_HARVEST] = true;
        if (code == KeyCode.Z) actionFlags[ACTION_SLEEP] = true;
        if (code == KeyCode.C) actionFlags[ACTION_CHANGE_SEED] = true;
        if (code == KeyCode.E || code == KeyCode.SPACE) actionFlags[ACTION_COLLECT_RESOURCE] = true;
    }

    public void keyReleased(KeyCode code) {
        pressedKeys.remove(code);
    }

    public Farm getFarm() { return farm; }
    public Player getPlayer() { return player; }
    public TimeSystem getTimeSystem() { return timeSystem; }
    public UIManager getUIManager() { return uiManager; }
}

