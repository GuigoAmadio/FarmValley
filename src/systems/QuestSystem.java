package systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sistema de missões/quests do jogo
 */
public class QuestSystem {
    private List<Quest> availableQuests;
    private List<Quest> activeQuests;
    private List<Quest> completedQuests;
    private Random random;
    
    // Contadores para rastrear progresso
    private int cropsHarvested;
    private int enemiesKilled;
    private int woodCollected;
    private int stoneCollected;
    private int moneyEarned;
    private int daysPlayed;
    
    public QuestSystem() {
        this.availableQuests = new ArrayList<>();
        this.activeQuests = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.random = new Random();
        
        initializeQuests();
    }
    
    private void initializeQuests() {
        // Missões de Agricultura
        availableQuests.add(new Quest("Q001", "Primeiro Plantio", 
            "Colha 3 culturas de qualquer tipo", 
            QuestType.HARVEST, 3, 50, 25));
        
        availableQuests.add(new Quest("Q002", "Fazendeiro Iniciante", 
            "Colha 10 culturas", 
            QuestType.HARVEST, 10, 150, 75));
        
        availableQuests.add(new Quest("Q003", "Mestre das Colheitas", 
            "Colha 25 culturas", 
            QuestType.HARVEST, 25, 400, 200));
        
        // Missões de Combate
        availableQuests.add(new Quest("Q010", "Caçador de Slimes", 
            "Derrote 3 slimes", 
            QuestType.KILL_ENEMIES, 3, 75, 40));
        
        availableQuests.add(new Quest("Q011", "Protetor da Vila", 
            "Derrote 10 inimigos", 
            QuestType.KILL_ENEMIES, 10, 200, 100));
        
        availableQuests.add(new Quest("Q012", "Guerreiro Veterano", 
            "Derrote 25 inimigos", 
            QuestType.KILL_ENEMIES, 25, 500, 250));
        
        // Missões de Coleta
        availableQuests.add(new Quest("Q020", "Lenhador", 
            "Colete 10 madeiras", 
            QuestType.COLLECT_WOOD, 10, 60, 30));
        
        availableQuests.add(new Quest("Q021", "Minerador", 
            "Colete 10 pedras", 
            QuestType.COLLECT_STONE, 10, 80, 40));
        
        availableQuests.add(new Quest("Q022", "Coletor Mestre", 
            "Colete 20 madeiras e 20 pedras", 
            QuestType.COLLECT_BOTH, 40, 200, 100));
        
        // Missões de Economia
        availableQuests.add(new Quest("Q030", "Primeiro Lucro", 
            "Ganhe $100", 
            QuestType.EARN_MONEY, 100, 50, 25));
        
        availableQuests.add(new Quest("Q031", "Comerciante", 
            "Ganhe $500", 
            QuestType.EARN_MONEY, 500, 150, 75));
        
        availableQuests.add(new Quest("Q032", "Magnata", 
            "Ganhe $2000", 
            QuestType.EARN_MONEY, 2000, 500, 250));
        
        // Missões de Tempo
        availableQuests.add(new Quest("Q040", "Uma Semana", 
            "Sobreviva 7 dias", 
            QuestType.SURVIVE_DAYS, 7, 100, 50));
        
        availableQuests.add(new Quest("Q041", "Um Mês", 
            "Sobreviva 30 dias", 
            QuestType.SURVIVE_DAYS, 30, 300, 150));
        
        // Ativar algumas missões iniciais automaticamente
        acceptQuest("Q001"); // Primeiro Plantio
        acceptQuest("Q010"); // Caçador de Slimes
        acceptQuest("Q020"); // Lenhador
        acceptQuest("Q030"); // Primeiro Lucro
    }
    
    /**
     * Aceita uma missão
     */
    public boolean acceptQuest(String questId) {
        for (Quest quest : availableQuests) {
            if (quest.getId().equals(questId) && !quest.isAccepted()) {
                quest.accept();
                activeQuests.add(quest);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Atualiza progresso das missões
     */
    public List<Quest> updateProgress(QuestType type, int amount) {
        List<Quest> justCompleted = new ArrayList<>();
        
        for (Quest quest : activeQuests) {
            if (!quest.isCompleted() && quest.getType() == type) {
                quest.addProgress(amount);
                
                if (quest.isCompleted()) {
                    justCompleted.add(quest);
                }
            }
            
            // Missão especial: coletar ambos
            if (!quest.isCompleted() && quest.getType() == QuestType.COLLECT_BOTH) {
                if (type == QuestType.COLLECT_WOOD || type == QuestType.COLLECT_STONE) {
                    quest.addProgress(amount);
                    if (quest.isCompleted()) {
                        justCompleted.add(quest);
                    }
                }
            }
        }
        
        return justCompleted;
    }
    
    /**
     * Reivindica recompensa de uma missão
     */
    public QuestReward claimReward(String questId) {
        for (Quest quest : activeQuests) {
            if (quest.getId().equals(questId) && quest.isCompleted() && !quest.isRewardClaimed()) {
                quest.claimReward();
                completedQuests.add(quest);
                activeQuests.remove(quest);
                
                // Desbloquear próxima missão da mesma categoria
                unlockNextQuest(quest);
                
                return new QuestReward(quest.getMoneyReward(), quest.getXpReward(), quest.getName());
            }
        }
        return null;
    }
    
    private void unlockNextQuest(Quest completed) {
        // Lógica simples: desbloquear missões com IDs sequenciais
        String currentId = completed.getId();
        int num = Integer.parseInt(currentId.substring(1));
        String nextId = String.format("Q%03d", num + 1);
        
        for (Quest quest : availableQuests) {
            if (quest.getId().equals(nextId) && !quest.isAccepted()) {
                acceptQuest(nextId);
                break;
            }
        }
    }
    
    /**
     * Registra eventos do jogo para atualizar missões
     */
    public List<Quest> onCropHarvested() {
        cropsHarvested++;
        return updateProgress(QuestType.HARVEST, 1);
    }
    
    public List<Quest> onEnemyKilled() {
        enemiesKilled++;
        return updateProgress(QuestType.KILL_ENEMIES, 1);
    }
    
    public List<Quest> onWoodCollected(int amount) {
        woodCollected += amount;
        return updateProgress(QuestType.COLLECT_WOOD, amount);
    }
    
    public List<Quest> onStoneCollected(int amount) {
        stoneCollected += amount;
        return updateProgress(QuestType.COLLECT_STONE, amount);
    }
    
    public List<Quest> onMoneyEarned(int amount) {
        moneyEarned += amount;
        return updateProgress(QuestType.EARN_MONEY, amount);
    }
    
    public List<Quest> onDayPassed() {
        daysPlayed++;
        return updateProgress(QuestType.SURVIVE_DAYS, 1);
    }
    
    // Getters
    public List<Quest> getActiveQuests() { return activeQuests; }
    public List<Quest> getCompletedQuests() { return completedQuests; }
    public List<Quest> getAvailableQuests() { return availableQuests; }
    
    public int getActiveQuestCount() { return activeQuests.size(); }
    public int getCompletedQuestCount() { return completedQuests.size(); }
    
    public List<Quest> getQuestsReadyToClaim() {
        List<Quest> ready = new ArrayList<>();
        for (Quest quest : activeQuests) {
            if (quest.isCompleted() && !quest.isRewardClaimed()) {
                ready.add(quest);
            }
        }
        return ready;
    }
    
    // ===== Classes Internas =====
    
    public enum QuestType {
        HARVEST("Colher"),
        KILL_ENEMIES("Combate"),
        COLLECT_WOOD("Coletar Madeira"),
        COLLECT_STONE("Coletar Pedra"),
        COLLECT_BOTH("Coletar Recursos"),
        EARN_MONEY("Ganhar Dinheiro"),
        SURVIVE_DAYS("Sobreviver");
        
        private final String name;
        QuestType(String name) { this.name = name; }
        public String getName() { return name; }
    }
    
    public static class Quest {
        private String id;
        private String name;
        private String description;
        private QuestType type;
        private int targetAmount;
        private int currentProgress;
        private int moneyReward;
        private int xpReward;
        private boolean accepted;
        private boolean completed;
        private boolean rewardClaimed;
        
        public Quest(String id, String name, String description, QuestType type, 
                     int targetAmount, int moneyReward, int xpReward) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.targetAmount = targetAmount;
            this.currentProgress = 0;
            this.moneyReward = moneyReward;
            this.xpReward = xpReward;
            this.accepted = false;
            this.completed = false;
            this.rewardClaimed = false;
        }
        
        public void accept() { this.accepted = true; }
        
        public void addProgress(int amount) {
            this.currentProgress += amount;
            if (this.currentProgress >= this.targetAmount) {
                this.completed = true;
                this.currentProgress = this.targetAmount;
            }
        }
        
        public void claimReward() { this.rewardClaimed = true; }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public QuestType getType() { return type; }
        public int getTargetAmount() { return targetAmount; }
        public int getCurrentProgress() { return currentProgress; }
        public int getMoneyReward() { return moneyReward; }
        public int getXpReward() { return xpReward; }
        public boolean isAccepted() { return accepted; }
        public boolean isCompleted() { return completed; }
        public boolean isRewardClaimed() { return rewardClaimed; }
        
        public double getProgressPercent() {
            return (double) currentProgress / targetAmount;
        }
        
        public String getProgressString() {
            return currentProgress + "/" + targetAmount;
        }
    }
    
    public static class QuestReward {
        public final int money;
        public final int xp;
        public final String questName;
        
        public QuestReward(int money, int xp, String questName) {
            this.money = money;
            this.xp = xp;
            this.questName = questName;
        }
    }
}

