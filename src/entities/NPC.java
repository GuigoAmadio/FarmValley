package entities;

import java.util.Random;
import systems.DialogueSystem;

/**
 * Classe base para NPCs (Non-Player Characters)
 * Inclui comerciantes, fazendeiros, aldeões, etc.
 */
public class NPC {
    protected int x, y;
    protected int targetX, targetY;
    protected NPCType type;
    protected String name;
    protected Direction facing;
    protected int animationFrame;
    
    // Movimento
    protected long lastMoveTime;
    protected int moveSpeed; // Delay em ms entre movimentos
    protected boolean isMoving;
    protected Random random;
    
    // Interação
    protected int interactionCount;
    protected boolean canTrade;
    
    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }
    
    public enum NPCType {
        MERCHANT("Comerciante", true, 200, 150, 50),    // Azul
        FARMER("Fazendeiro", false, 139, 90, 43),       // Marrom
        VILLAGER("Aldeão", false, 100, 200, 100),       // Verde claro
        WANDERER("Viajante", false, 150, 100, 150);     // Roxo
        
        private final String title;
        private final boolean trades;
        private final int red, green, blue;
        
        NPCType(String title, boolean trades, int red, int green, int blue) {
            this.title = title;
            this.trades = trades;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
        
        public String getTitle() { return title; }
        public boolean canTrade() { return trades; }
        public int getRed() { return red; }
        public int getGreen() { return green; }
        public int getBlue() { return blue; }
    }
    
    public NPC(int x, int y, NPCType type, String name) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.type = type;
        this.name = name;
        this.facing = Direction.DOWN;
        this.animationFrame = 0;
        this.lastMoveTime = System.currentTimeMillis();
        this.moveSpeed = 1500 + new Random().nextInt(1000); // 1500-2500ms - movimento mais lento
        this.isMoving = false;
        this.random = new Random();
        this.canTrade = type.canTrade();
        this.interactionCount = 0;
    }
    
    /**
     * Atualiza o NPC (movimento, IA, etc.)
     */
    public void update(int mapWidth, int mapHeight, java.util.function.BiPredicate<Integer, Integer> isWalkable) {
        long currentTime = System.currentTimeMillis();
        
        // Verificar se é hora de mover
        if (currentTime - lastMoveTime >= moveSpeed) {
            // Decidir novo movimento aleatório
            if (random.nextDouble() < 0.7) { // 70% chance de mover
                int dx = random.nextInt(3) - 1; // -1, 0, ou 1
                int dy = random.nextInt(3) - 1;
                
                int newX = x + dx;
                int newY = y + dy;
                
                // Verificar limites do mapa e se pode andar
                if (newX >= 2 && newX < mapWidth - 2 && 
                    newY >= 2 && newY < mapHeight - 2 &&
                    isWalkable.test(newX, newY)) {
                    
                    // Atualizar direção
                    if (dy < 0) facing = Direction.UP;
                    else if (dy > 0) facing = Direction.DOWN;
                    else if (dx < 0) facing = Direction.LEFT;
                    else if (dx > 0) facing = Direction.RIGHT;
                    
                    x = newX;
                    y = newY;
                    isMoving = true;
                    animationFrame = (animationFrame + 1) % 4;
                }
            } else {
                isMoving = false;
            }
            
            lastMoveTime = currentTime;
        }
    }
    
    /**
     * Retorna próximo diálogo usando o sistema de diálogos
     */
    public String getNextDialogue() {
        interactionCount++;
        DialogueSystem dialogueSystem = DialogueSystem.getInstance();
        
        String npcTypeStr = type.name().toLowerCase();
        
        // Primeira interação = saudação
        if (interactionCount == 1) {
            return dialogueSystem.getGreeting(npcTypeStr);
        }
        
        // Diálogos especiais por tipo
        switch (type) {
            case FARMER:
                // Chance de dar dica
                if (random.nextDouble() < 0.4) {
                    return dialogueSystem.getTip();
                }
                break;
            case VILLAGER:
                // Chance de fofoca
                if (random.nextDouble() < 0.3) {
                    return dialogueSystem.getGossip();
                }
                break;
            case WANDERER:
                // Chance de história ou aviso
                if (random.nextDouble() < 0.5) {
                    return random.nextBoolean() ? 
                        dialogueSystem.getStory() : 
                        dialogueSystem.getWarning();
                }
                break;
            default:
                break;
        }
        
        // Diálogo aleatório padrão
        return dialogueSystem.getRandomDialogue(npcTypeStr);
    }
    
    /**
     * Verifica se o jogador está próximo
     */
    public boolean isNearPlayer(int playerX, int playerY) {
        return Math.abs(x - playerX) <= 1 && Math.abs(y - playerY) <= 1;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public NPCType getType() { return type; }
    public String getName() { return name; }
    public Direction getFacing() { return facing; }
    public int getAnimationFrame() { return animationFrame; }
    public boolean canTrade() { return canTrade; }
    public boolean isMoving() { return isMoving; }
    
    public String getDisplayName() {
        return name + " (" + type.getTitle() + ")";
    }
}

