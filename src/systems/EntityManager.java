package systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import entities.NPC;
import entities.NPC.NPCType;
import entities.Enemy;
import entities.Enemy.EnemyType;
import world.Farm;

/**
 * Gerencia todos os NPCs e inimigos do jogo
 */
public class EntityManager {
    private List<NPC> npcs;
    private List<Enemy> enemies;
    private Random random;
    private Farm farm;
    
    // Nomes aleatórios para NPCs
    private static final String[] MERCHANT_NAMES = {"Pedro", "Maria", "João", "Ana", "Carlos"};
    private static final String[] FARMER_NAMES = {"José", "Antônio", "Manuel", "Francisco", "Paulo"};
    private static final String[] VILLAGER_NAMES = {"Lucas", "Julia", "Gabriel", "Sofia", "Miguel"};
    private static final String[] WANDERER_NAMES = {"Wanderley", "Aurora", "Dante", "Luna", "Felix"};
    
    public EntityManager(Farm farm) {
        this.farm = farm;
        this.npcs = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Gera NPCs e inimigos baseado no tamanho do mapa
     */
    public void generateEntities() {
        int width = farm.getWidth();
        int height = farm.getHeight();
        int mapArea = width * height;
        
        // Calcular quantidade baseada no tamanho do mapa - AUMENTADO!
        int npcCount = Math.max(12, mapArea / 1500);      // ~15 NPCs para 150x150
        int enemyCount = Math.max(20, mapArea / 1000);    // ~22 inimigos para 150x150
        
        System.out.println("👥 Gerando entidades...");
        System.out.println("   - NPCs: " + npcCount);
        System.out.println("   - Inimigos: " + enemyCount);
        
        // Gerar NPCs próximos ao spawn primeiro
        generateNPCsNearSpawn(width, height, 3); // 3 NPCs bem perto do spawn
        
        // Gerar NPCs espalhados
        generateNPCs(width, height, npcCount);
        
        // Gerar inimigos (mais longe do spawn)
        generateEnemies(width, height, enemyCount);
        
        System.out.println("✅ Entidades geradas: " + npcs.size() + " NPCs, " + enemies.size() + " inimigos");
    }
    
    /**
     * Gera alguns NPCs bem perto do spawn do jogador
     */
    private void generateNPCsNearSpawn(int width, int height, int count) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Posições fixas perto do spawn
        int[][] nearbyPositions = {
            {centerX + 3, centerY},
            {centerX - 3, centerY},
            {centerX, centerY + 3},
            {centerX + 2, centerY + 2},
            {centerX - 2, centerY - 2}
        };
        
        for (int i = 0; i < Math.min(count, nearbyPositions.length); i++) {
            int x = nearbyPositions[i][0];
            int y = nearbyPositions[i][1];
            
            if (x >= 2 && x < width - 2 && y >= 2 && y < height - 2 &&
                farm.isWalkable(x, y) && !isPositionOccupied(x, y)) {
                
                // Comerciante sempre perto do spawn
                NPCType type = (i == 0) ? NPCType.MERCHANT : getRandomNPCType();
                String name = getRandomName(type);
                
                npcs.add(new NPC(x, y, type, name));
                System.out.println("   📍 " + type.getTitle() + " " + name + " gerado perto do spawn em (" + x + ", " + y + ")");
            }
        }
    }
    
    /**
     * Gera NPCs variados espalhados pelo mapa
     */
    private void generateNPCs(int width, int height, int count) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        for (int i = 0; i < count; i++) {
            int attempts = 0;
            while (attempts < 100) {
                // NPCs em área maior ao redor do centro
                int range = 30 + random.nextInt(20); // 30-50 tiles do centro
                int x = centerX + random.nextInt(range * 2) - range;
                int y = centerY + random.nextInt(range * 2) - range;
                
                // Verificar posição válida
                if (x >= 2 && x < width - 2 && y >= 2 && y < height - 2 &&
                    farm.isWalkable(x, y) && !isPositionOccupied(x, y)) {
                    
                    // Escolher tipo aleatório
                    NPCType type = getRandomNPCType();
                    String name = getRandomName(type);
                    
                    npcs.add(new NPC(x, y, type, name));
                    break;
                }
                attempts++;
            }
        }
    }
    
    /**
     * Gera inimigos (mais longe do spawn, mas alguns mais perto)
     */
    private void generateEnemies(int width, int height, int count) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        for (int i = 0; i < count; i++) {
            int attempts = 0;
            while (attempts < 100) {
                int x, y;
                
                // 30% dos inimigos ficam mais perto (mas não muito)
                if (i < count * 0.3) {
                    // Inimigos médios - 10-20 tiles do centro
                    int dist = 10 + random.nextInt(10);
                    double angle = random.nextDouble() * Math.PI * 2;
                    x = centerX + (int)(Math.cos(angle) * dist);
                    y = centerY + (int)(Math.sin(angle) * dist);
                } else {
                    // Inimigos longe do centro
                    do {
                        x = random.nextInt(width - 4) + 2;
                        y = random.nextInt(height - 4) + 2;
                    } while (Math.abs(x - centerX) < 12 && Math.abs(y - centerY) < 12);
                }
                
                // Verificar limites e posição válida
                if (x >= 2 && x < width - 2 && y >= 2 && y < height - 2 &&
                    farm.isWalkable(x, y) && !isPositionOccupied(x, y)) {
                    EnemyType type = getRandomEnemyType();
                    enemies.add(new Enemy(x, y, type));
                    break;
                }
                attempts++;
            }
        }
    }
    
    private NPCType getRandomNPCType() {
        NPCType[] types = NPCType.values();
        // Mais chance de fazendeiros e aldeões
        double roll = random.nextDouble();
        if (roll < 0.15) return NPCType.MERCHANT;
        if (roll < 0.45) return NPCType.FARMER;
        if (roll < 0.80) return NPCType.VILLAGER;
        return NPCType.WANDERER;
    }
    
    private EnemyType getRandomEnemyType() {
        // Distribuição: 50% Slime, 30% Goblin, 20% Skeleton
        double roll = random.nextDouble();
        if (roll < 0.5) return EnemyType.SLIME;
        if (roll < 0.8) return EnemyType.GOBLIN;
        return EnemyType.SKELETON;
    }
    
    private String getRandomName(NPCType type) {
        switch (type) {
            case MERCHANT:
                return MERCHANT_NAMES[random.nextInt(MERCHANT_NAMES.length)];
            case FARMER:
                return FARMER_NAMES[random.nextInt(FARMER_NAMES.length)];
            case VILLAGER:
                return VILLAGER_NAMES[random.nextInt(VILLAGER_NAMES.length)];
            case WANDERER:
                return WANDERER_NAMES[random.nextInt(WANDERER_NAMES.length)];
            default:
                return "???";
        }
    }
    
    private boolean isPositionOccupied(int x, int y) {
        for (NPC npc : npcs) {
            if (npc.getX() == x && npc.getY() == y) return true;
        }
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) return true;
        }
        return false;
    }
    
    /**
     * Atualiza todos os NPCs e inimigos
     */
    public void update(int playerX, int playerY) {
        int width = farm.getWidth();
        int height = farm.getHeight();
        
        // Atualizar NPCs
        for (NPC npc : npcs) {
            npc.update(width, height, (x, y) -> farm.isWalkable(x, y) && !isPositionOccupied(x, y));
        }
        
        // Atualizar inimigos
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.update(playerX, playerY, width, height, 
                    (x, y) -> farm.isWalkable(x, y) && !isPositionOccupied(x, y));
            }
        }
        
        // Remover inimigos mortos (após um tempo)
        // enemies.removeIf(e -> e.isDead());
    }
    
    /**
     * Verifica se há inimigo adjacente ao jogador
     */
    public Enemy getAdjacentEnemy(int playerX, int playerY) {
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && enemy.isAdjacentTo(playerX, playerY)) {
                return enemy;
            }
        }
        return null;
    }
    
    /**
     * Verifica se há NPC adjacente ao jogador
     */
    public NPC getAdjacentNPC(int playerX, int playerY) {
        for (NPC npc : npcs) {
            if (npc.isNearPlayer(playerX, playerY)) {
                return npc;
            }
        }
        return null;
    }
    
    /**
     * Ataca inimigo específico
     */
    public boolean attackEnemy(Enemy enemy, int damage) {
        if (enemy != null && !enemy.isDead()) {
            enemy.takeDamage(damage);
            return true;
        }
        return false;
    }
    
    // Getters
    public List<NPC> getNPCs() { return npcs; }
    public List<Enemy> getEnemies() { return enemies; }
    public int getNPCCount() { return npcs.size(); }
    public int getEnemyCount() { return enemies.size(); }
    public int getAliveEnemyCount() { 
        return (int) enemies.stream().filter(e -> !e.isDead()).count(); 
    }
    
    /**
     * Verifica se há um inimigo vivo na posição especificada
     */
    public boolean hasEnemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (!enemy.isDead() && enemy.getX() == x && enemy.getY() == y) {
                return true;
            }
        }
        return false;
    }
}

