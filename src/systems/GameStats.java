package systems;

import java.util.HashMap;
import java.util.Map;
import types.CropType;

/**
 * Sistema de Estatísticas do Jogo
 * Rastreia todas as ações do jogador para métricas e conquistas
 */
public class GameStats {
    // Estatísticas de cultivos
    private int totalCropsPlanted;
    private int totalCropsHarvested;
    private Map<CropType, Integer> cropsPlantedByType;
    private Map<CropType, Integer> cropsHarvestedByType;
    
    // Estatísticas financeiras
    private int totalMoneyEarned;
    private int totalMoneySpent;
    private int highestMoneyReached;
    
    // Estatísticas de tempo
    private long gameStartTime;
    private int totalDaysPlayed;
    private int totalStepsTaken;
    
    // Estatísticas de recursos
    private int totalWoodCollected;
    private int totalStoneCollected;
    private int totalFiberCollected;
    
    // Estatísticas de ações
    private int totalTilesPlowed;
    private int totalTreesChopped;
    private int totalRocksDestroyed;
    
    public GameStats() {
        this.cropsPlantedByType = new HashMap<>();
        this.cropsHarvestedByType = new HashMap<>();
        this.gameStartTime = System.currentTimeMillis();
        
        // Inicializar contadores por tipo
        for (CropType type : CropType.values()) {
            cropsPlantedByType.put(type, 0);
            cropsHarvestedByType.put(type, 0);
        }
    }
    
    // ===== CULTIVOS =====
    
    public void recordCropPlanted(CropType type) {
        totalCropsPlanted++;
        cropsPlantedByType.merge(type, 1, Integer::sum);
    }
    
    public void recordCropHarvested(CropType type) {
        totalCropsHarvested++;
        cropsHarvestedByType.merge(type, 1, Integer::sum);
    }
    
    // ===== FINANÇAS =====
    
    public void recordMoneyEarned(int amount) {
        totalMoneyEarned += amount;
    }
    
    public void recordMoneySpent(int amount) {
        totalMoneySpent += amount;
    }
    
    public void updateHighestMoney(int currentMoney) {
        if (currentMoney > highestMoneyReached) {
            highestMoneyReached = currentMoney;
        }
    }
    
    // ===== RECURSOS =====
    
    public void recordWoodCollected(int amount) {
        totalWoodCollected += amount;
    }
    
    public void recordStoneCollected(int amount) {
        totalStoneCollected += amount;
    }
    
    public void recordFiberCollected(int amount) {
        totalFiberCollected += amount;
    }
    
    // ===== AÇÕES =====
    
    public void recordTilePlowed() {
        totalTilesPlowed++;
    }
    
    public void recordTreeChopped() {
        totalTreesChopped++;
    }
    
    public void recordRockDestroyed() {
        totalRocksDestroyed++;
    }
    
    public void recordStep() {
        totalStepsTaken++;
    }
    
    public void recordDayPassed() {
        totalDaysPlayed++;
    }
    
    // ===== GETTERS =====
    
    public int getTotalCropsPlanted() { return totalCropsPlanted; }
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public int getTotalMoneyEarned() { return totalMoneyEarned; }
    public int getTotalMoneySpent() { return totalMoneySpent; }
    public int getHighestMoneyReached() { return highestMoneyReached; }
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public int getTotalStepsTaken() { return totalStepsTaken; }
    public int getTotalWoodCollected() { return totalWoodCollected; }
    public int getTotalStoneCollected() { return totalStoneCollected; }
    public int getTotalFiberCollected() { return totalFiberCollected; }
    public int getTotalTilesPlowed() { return totalTilesPlowed; }
    public int getTotalTreesChopped() { return totalTreesChopped; }
    public int getTotalRocksDestroyed() { return totalRocksDestroyed; }
    
    public int getCropsPlantedByType(CropType type) {
        return cropsPlantedByType.getOrDefault(type, 0);
    }
    
    public int getCropsHarvestedByType(CropType type) {
        return cropsHarvestedByType.getOrDefault(type, 0);
    }
    
    /**
     * Retorna tempo de jogo formatado
     */
    public String getPlayTimeFormatted() {
        long elapsed = System.currentTimeMillis() - gameStartTime;
        long seconds = elapsed / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    /**
     * Calcula lucro líquido
     */
    public int getNetProfit() {
        return totalMoneyEarned - totalMoneySpent;
    }
    
    /**
     * Retorna resumo das estatísticas para exibição
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTATÍSTICAS DO JOGO ===\n\n");
        
        sb.append("📊 CULTIVOS:\n");
        sb.append(String.format("  Plantados: %d | Colhidos: %d\n", totalCropsPlanted, totalCropsHarvested));
        
        sb.append("\n💰 FINANÇAS:\n");
        sb.append(String.format("  Ganho: $%d | Gasto: $%d\n", totalMoneyEarned, totalMoneySpent));
        sb.append(String.format("  Lucro: $%d | Recorde: $%d\n", getNetProfit(), highestMoneyReached));
        
        sb.append("\n🪵 RECURSOS:\n");
        sb.append(String.format("  Madeira: %d | Pedra: %d | Fibra: %d\n", 
            totalWoodCollected, totalStoneCollected, totalFiberCollected));
        
        sb.append("\n⏱️ TEMPO:\n");
        sb.append(String.format("  Dias jogados: %d | Tempo: %s\n", totalDaysPlayed, getPlayTimeFormatted()));
        sb.append(String.format("  Passos: %d\n", totalStepsTaken));
        
        return sb.toString();
    }
}

