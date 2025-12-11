package systems;

/**
 * Sistema de Níveis e Experiência
 * Jogador ganha XP por ações e sobe de nível
 */
public class LevelSystem {
    private int level;
    private int currentXP;
    private int xpToNextLevel;
    
    // Multiplicadores de XP por ação
    private static final int XP_PLANT = 5;
    private static final int XP_HARVEST = 15;
    private static final int XP_CHOP_TREE = 10;
    private static final int XP_MINE_ROCK = 12;
    private static final int XP_PLOW = 3;
    private static final int XP_SELL = 2; // Por item vendido
    
    // Bônus por nível
    private int energyBonus;
    private double sellPriceMultiplier;
    private double cropQualityBonus;
    
    public LevelSystem() {
        this.level = 1;
        this.currentXP = 0;
        this.xpToNextLevel = calculateXPForLevel(2);
        updateBonuses();
    }
    
    /**
     * Calcula XP necessário para um nível específico
     * Fórmula: base * (nivel ^ 1.5)
     */
    private int calculateXPForLevel(int targetLevel) {
        return (int) (100 * Math.pow(targetLevel, 1.5));
    }
    
    /**
     * Adiciona XP e verifica level up
     */
    public boolean addXP(int amount) {
        currentXP += amount;
        
        boolean leveledUp = false;
        while (currentXP >= xpToNextLevel) {
            currentXP -= xpToNextLevel;
            level++;
            xpToNextLevel = calculateXPForLevel(level + 1);
            updateBonuses();
            leveledUp = true;
            System.out.println("🎉 LEVEL UP! Agora você é nível " + level);
        }
        
        return leveledUp;
    }
    
    /**
     * Atualiza bônus baseados no nível atual
     */
    private void updateBonuses() {
        // +5 de energia máxima por nível
        energyBonus = (level - 1) * 5;
        
        // +2% no preço de venda por nível
        sellPriceMultiplier = 1.0 + (level - 1) * 0.02;
        
        // +3% chance de qualidade superior por nível
        cropQualityBonus = (level - 1) * 0.03;
    }
    
    // ===== MÉTODOS DE XP POR AÇÃO =====
    
    public boolean onPlant() {
        return addXP(XP_PLANT);
    }
    
    public boolean onHarvest() {
        return addXP(XP_HARVEST);
    }
    
    public boolean onChopTree() {
        return addXP(XP_CHOP_TREE);
    }
    
    public boolean onMineRock() {
        return addXP(XP_MINE_ROCK);
    }
    
    public boolean onPlow() {
        return addXP(XP_PLOW);
    }
    
    public boolean onSell(int itemCount) {
        return addXP(XP_SELL * itemCount);
    }
    
    // ===== GETTERS =====
    
    public int getLevel() { return level; }
    public int getCurrentXP() { return currentXP; }
    public int getXPToNextLevel() { return xpToNextLevel; }
    public int getEnergyBonus() { return energyBonus; }
    public double getSellPriceMultiplier() { return sellPriceMultiplier; }
    public double getCropQualityBonus() { return cropQualityBonus; }
    
    /**
     * Retorna progresso para próximo nível (0.0 a 1.0)
     */
    public double getLevelProgress() {
        return (double) currentXP / xpToNextLevel;
    }
    
    /**
     * Retorna string formatada do progresso
     */
    public String getProgressString() {
        return String.format("Nível %d (%d/%d XP)", level, currentXP, xpToNextLevel);
    }
    
    /**
     * Calcula preço de venda com bônus de nível
     */
    public int calculateSellPrice(int basePrice) {
        return (int) (basePrice * sellPriceMultiplier);
    }
}

