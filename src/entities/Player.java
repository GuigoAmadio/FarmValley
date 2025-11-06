package entities;

import systems.Inventory;
import items.ItemType;
import types.CropType;

public class Player {
    private int x;
    private int y;
    private int money;
    private int energy;
    private int maxEnergy;
    private CropType selectedSeed;
    private Inventory inventory;
    
    // Sistema de animação
    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }
    private Direction facing;
    private int animationFrame;
    private long lastFrameTime;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.money = 500;
        this.energy = 100;
        this.maxEnergy = 100;
        this.selectedSeed = CropType.WHEAT;
        this.facing = Direction.DOWN;
        this.animationFrame = 0;
        this.lastFrameTime = System.nanoTime();
        
        // Criar inventário com 24 slots (6x4 grid)
        this.inventory = new Inventory(24);
        
        // Itens iniciais
        inventory.addItem(ItemType.WHEAT_SEED, 10);
        inventory.addItem(ItemType.TOMATO_SEED, 5);
        inventory.addItem(ItemType.HOE);
        inventory.addItem(ItemType.AXE);
        inventory.addItem(ItemType.PICKAXE);
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public boolean canAfford(int cost) {
        return money >= cost;
    }

    public void spend(int amount) {
        money -= amount;
    }

    public void earn(int amount) {
        money += amount;
    }

    public boolean hasEnergy(int amount) {
        return energy >= amount;
    }

    public void useEnergy(int amount) {
        energy = Math.max(0, energy - amount);
    }

    public void restoreEnergy() {
        energy = maxEnergy;
    }

    public void sleep() {
        restoreEnergy();
    }

    // Atualizar direção baseado no movimento
    public void updateDirection(int dx, int dy) {
        if (dy < 0) facing = Direction.UP;
        else if (dy > 0) facing = Direction.DOWN;
        else if (dx < 0) facing = Direction.LEFT;
        else if (dx > 0) facing = Direction.RIGHT;
    }
    
    // Atualizar animação (alternar entre frames)
    public void updateAnimation() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastFrameTime;
        
        // Trocar frame a cada 100ms (10 frames por segundo) - animação mais fluida!
        if (elapsedTime > 100_000_000) {
            animationFrame = (animationFrame + 1) % 6; // Alterna entre 0 e 5 (6 frames!)
            lastFrameTime = currentTime;
        }
    }
    
    // Resetar animação quando parado
    public void resetAnimation() {
        animationFrame = 0;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getMoney() { return money; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public CropType getSelectedSeed() { return selectedSeed; }
    public Direction getFacing() { return facing; }
    public int getAnimationFrame() { return animationFrame; }
    public Inventory getInventory() { return inventory; }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSelectedSeed(CropType seed) { this.selectedSeed = seed; }
}

