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
    private int health;
    private int maxHealth;
    private CropType selectedSeed;
    private Inventory inventory;
    
    // Sistema de animação
    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }
    private Direction facing;
    private int animationFrame;
    private long lastFrameTime;
    
    // Sistema de combate
    private boolean isDead;
    private int attackDamage;
    private long lastDamageTime;
    private static final long DAMAGE_COOLDOWN = 1000; // 1 segundo de invulnerabilidade

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.money = 500;
        this.energy = 100;
        this.maxEnergy = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.selectedSeed = CropType.TOMATO;
        this.facing = Direction.DOWN;
        this.animationFrame = 0;
        this.lastFrameTime = System.nanoTime();
        this.isDead = false;
        this.attackDamage = 15;
        this.lastDamageTime = 0;
        
        // Criar inventário com 24 slots (6x4 grid)
        this.inventory = new Inventory(24);
        
        // Itens iniciais
        inventory.addItem(ItemType.TOMATO_SEED, 10);
        inventory.addItem(ItemType.CORN_SEED, 5);
        inventory.addItem(ItemType.HOE);
        inventory.addItem(ItemType.AXE);
        inventory.addItem(ItemType.PICKAXE);
        inventory.addItem(ItemType.HEALTH_POTION, 2);
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
        // Dormir também recupera um pouco de vida
        heal(maxHealth / 4);
    }
    
    // ===== SISTEMA DE VIDA =====
    
    public void takeDamage(int amount) {
        long currentTime = System.currentTimeMillis();
        
        // Verificar invulnerabilidade temporária
        if (currentTime - lastDamageTime < DAMAGE_COOLDOWN) {
            return;
        }
        
        lastDamageTime = currentTime;
        health = Math.max(0, health - amount);
        
        if (health <= 0) {
            die();
        }
    }
    
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }
    
    public void restoreHealth() {
        health = maxHealth;
    }
    
    private void die() {
        isDead = true;
        // Perder parte do dinheiro ao morrer
        money = Math.max(0, money - money / 4);
    }
    
    public void respawn(int spawnX, int spawnY) {
        this.x = spawnX;
        this.y = spawnY;
        this.health = maxHealth / 2; // Renasce com metade da vida
        this.energy = maxEnergy / 2;  // E metade da energia
        this.isDead = false;
    }
    
    public boolean canTakeDamage() {
        return System.currentTimeMillis() - lastDamageTime >= DAMAGE_COOLDOWN;
    }
    
    public boolean isInvulnerable() {
        return System.currentTimeMillis() - lastDamageTime < DAMAGE_COOLDOWN;
    }
    
    // ===== CONSUMÍVEIS =====
    
    public boolean useHealthPotion() {
        if (inventory.hasItem(ItemType.HEALTH_POTION)) {
            inventory.removeItem(ItemType.HEALTH_POTION, 1);
            heal(30);
            return true;
        }
        return false;
    }
    
    public boolean useEnergyPotion() {
        if (inventory.hasItem(ItemType.ENERGY_POTION)) {
            inventory.removeItem(ItemType.ENERGY_POTION, 1);
            energy = Math.min(maxEnergy, energy + 50);
            return true;
        }
        return false;
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
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public CropType getSelectedSeed() { return selectedSeed; }
    public Direction getFacing() { return facing; }
    public int getAnimationFrame() { return animationFrame; }
    public Inventory getInventory() { return inventory; }
    public boolean isDead() { return isDead; }
    public int getAttackDamage() { return attackDamage; }
    
    public double getHealthPercent() {
        return (double) health / maxHealth;
    }
    
    public double getEnergyPercent() {
        return (double) energy / maxEnergy;
    }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSelectedSeed(CropType seed) { this.selectedSeed = seed; }
    public void setAttackDamage(int damage) { this.attackDamage = damage; }
    
    public void increaseMaxHealth(int amount) {
        this.maxHealth += amount;
        this.health += amount;
    }
    
    public void increaseMaxEnergy(int amount) {
        this.maxEnergy += amount;
        this.energy += amount;
    }
}

