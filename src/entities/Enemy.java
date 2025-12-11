package entities;

import java.util.Random;

/**
 * Classe para inimigos
 * Criaturas hostis que podem atacar o jogador
 */
public class Enemy {
    private int x, y;
    private EnemyType type;
    private int health;
    private int maxHealth;
    private int damage;
    private Direction facing;
    private int animationFrame;
    
    // Movimento e IA
    private long lastMoveTime;
    private long lastAttackTime;
    private int moveSpeed;
    private int attackCooldown;
    private Random random;
    private boolean isAggressive;
    private int aggroRange; // Distância para detectar jogador
    
    // Estado
    private boolean isDead;
    private int targetX, targetY; // Posição do alvo (jogador)
    
    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }
    
    public enum EnemyType {
        SLIME("Slime", 30, 15, 1500, 3, 50, 200, 50),           // Verde - fraco
        GOBLIN("Goblin", 50, 20, 1000, 5, 100, 150, 100),       // Roxo - médio
        SKELETON("Esqueleto", 80, 25, 800, 6, 200, 200, 200);   // Branco - forte
        
        private final String name;
        private final int baseHealth;
        private final int baseDamage;
        private final int moveDelay;
        private final int aggroRange;
        private final int red, green, blue;
        
        EnemyType(String name, int baseHealth, int baseDamage, int moveDelay, int aggroRange, int r, int g, int b) {
            this.name = name;
            this.baseHealth = baseHealth;
            this.baseDamage = baseDamage;
            this.moveDelay = moveDelay;
            this.aggroRange = aggroRange;
            this.red = r;
            this.green = g;
            this.blue = b;
        }
        
        public String getName() { return name; }
        public int getBaseHealth() { return baseHealth; }
        public int getBaseDamage() { return baseDamage; }
        public int getMoveDelay() { return moveDelay; }
        public int getAggroRange() { return aggroRange; }
        public int getRed() { return red; }
        public int getGreen() { return green; }
        public int getBlue() { return blue; }
    }
    
    public Enemy(int x, int y, EnemyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.maxHealth = type.getBaseHealth();
        this.health = maxHealth;
        this.damage = type.getBaseDamage();
        this.moveSpeed = type.getMoveDelay();
        this.aggroRange = type.getAggroRange();
        this.attackCooldown = 2000; // 2 segundos entre ataques
        this.facing = Direction.DOWN;
        this.animationFrame = 0;
        this.lastMoveTime = System.currentTimeMillis();
        this.lastAttackTime = 0;
        this.random = new Random();
        this.isDead = false;
        this.isAggressive = false;
    }
    
    // Zona segura ao redor do spawn (inimigos não podem entrar)
    private static final int SAFE_ZONE_RADIUS = 8;
    
    /**
     * Verifica se uma posição está na zona segura (spawn)
     */
    private boolean isInSafeZone(int posX, int posY, int mapWidth, int mapHeight) {
        int spawnX = mapWidth / 2;
        int spawnY = mapHeight / 2;
        double distance = Math.sqrt(Math.pow(posX - spawnX, 2) + Math.pow(posY - spawnY, 2));
        return distance < SAFE_ZONE_RADIUS;
    }
    
    /**
     * Atualiza o inimigo (IA, movimento, ataque)
     */
    public void update(int playerX, int playerY, int mapWidth, int mapHeight, 
                       java.util.function.BiPredicate<Integer, Integer> isWalkable) {
        if (isDead) return;
        
        long currentTime = System.currentTimeMillis();
        
        // Se o inimigo está na zona segura, empurrá-lo para fora
        if (isInSafeZone(x, y, mapWidth, mapHeight)) {
            int spawnX = mapWidth / 2;
            int spawnY = mapHeight / 2;
            // Mover para longe do spawn
            if (x < spawnX) x--;
            else if (x > spawnX) x++;
            if (y < spawnY) y--;
            else if (y > spawnY) y++;
            return;
        }
        
        // Calcular distância do jogador
        double distanceToPlayer = Math.sqrt(Math.pow(x - playerX, 2) + Math.pow(y - playerY, 2));
        
        // Verificar se jogador está no range de aggro
        isAggressive = distanceToPlayer <= aggroRange;
        
        // Verificar se é hora de mover
        if (currentTime - lastMoveTime >= moveSpeed) {
            if (isAggressive) {
                // Perseguir jogador (mas não entrar na zona segura)
                moveTowardsTarget(playerX, playerY, mapWidth, mapHeight, isWalkable);
            } else {
                // Movimento aleatório
                moveRandomly(mapWidth, mapHeight, isWalkable);
            }
            lastMoveTime = currentTime;
        }
        
        // Atualizar animação
        if (isAggressive) {
            animationFrame = (animationFrame + 1) % 4;
        }
    }
    
    /**
     * Move em direção ao alvo
     */
    private void moveTowardsTarget(int targetX, int targetY, int mapWidth, int mapHeight,
                                   java.util.function.BiPredicate<Integer, Integer> isWalkable) {
        // Não mover se já estiver adjacente ao player
        if (isAdjacentTo(targetX, targetY)) {
            // Apenas virar para o player
            int dx = 0, dy = 0;
            if (targetX < x) dx = -1;
            else if (targetX > x) dx = 1;
            if (targetY < y) dy = -1;
            else if (targetY > y) dy = 1;
            updateFacing(dx, dy);
            return;
        }
        
        int dx = 0, dy = 0;
        
        if (targetX < x) dx = -1;
        else if (targetX > x) dx = 1;
        
        if (targetY < y) dy = -1;
        else if (targetY > y) dy = 1;
        
        // Tentar movimento diagonal primeiro
        int newX = x + dx;
        int newY = y + dy;
        
        // Não pode ir para a posição do player ou para a zona segura
        if (newX == targetX && newY == targetY) {
            return;
        }
        
        // Verificar se a nova posição está na zona segura
        if (isInSafeZone(newX, newY, mapWidth, mapHeight)) {
            return; // Não entrar na zona segura
        }
        
        if (newX >= 1 && newX < mapWidth - 1 && 
            newY >= 1 && newY < mapHeight - 1 &&
            isWalkable.test(newX, newY)) {
            updateFacing(dx, dy);
            x = newX;
            y = newY;
        }
        // Se não conseguir, tentar apenas X (verificando zona segura)
        else if (dx != 0 && (x + dx != targetX || y != targetY) && 
                 !isInSafeZone(x + dx, y, mapWidth, mapHeight) &&
                 isWalkable.test(x + dx, y)) {
            updateFacing(dx, 0);
            x += dx;
        }
        // Se não conseguir, tentar apenas Y (verificando zona segura)
        else if (dy != 0 && (x != targetX || y + dy != targetY) && 
                 !isInSafeZone(x, y + dy, mapWidth, mapHeight) &&
                 isWalkable.test(x, y + dy)) {
            updateFacing(0, dy);
            y += dy;
        }
    }
    
    /**
     * Movimento aleatório quando não agressivo
     */
    private void moveRandomly(int mapWidth, int mapHeight,
                              java.util.function.BiPredicate<Integer, Integer> isWalkable) {
        if (random.nextDouble() < 0.5) { // 50% chance de mover
            int dx = random.nextInt(3) - 1;
            int dy = random.nextInt(3) - 1;
            
            int newX = x + dx;
            int newY = y + dy;
            
            if (newX >= 2 && newX < mapWidth - 2 && 
                newY >= 2 && newY < mapHeight - 2 &&
                isWalkable.test(newX, newY)) {
                updateFacing(dx, dy);
                x = newX;
                y = newY;
            }
        }
    }
    
    private void updateFacing(int dx, int dy) {
        if (dy < 0) facing = Direction.UP;
        else if (dy > 0) facing = Direction.DOWN;
        else if (dx < 0) facing = Direction.LEFT;
        else if (dx > 0) facing = Direction.RIGHT;
    }
    
    /**
     * Verifica se pode atacar (cooldown)
     */
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= attackCooldown;
    }
    
    /**
     * Executa ataque e retorna dano
     */
    public int attack() {
        if (canAttack()) {
            lastAttackTime = System.currentTimeMillis();
            return damage;
        }
        return 0;
    }
    
    /**
     * Recebe dano
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }
    
    /**
     * Verifica se está adjacente ao jogador
     */
    public boolean isAdjacentTo(int playerX, int playerY) {
        return Math.abs(x - playerX) <= 1 && Math.abs(y - playerY) <= 1 && !(x == playerX && y == playerY);
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public EnemyType getType() { return type; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public Direction getFacing() { return facing; }
    public int getAnimationFrame() { return animationFrame; }
    public boolean isDead() { return isDead; }
    public boolean isAggressive() { return isAggressive; }
    
    public double getHealthPercentage() {
        return (double) health / maxHealth;
    }
}

