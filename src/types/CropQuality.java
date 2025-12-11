package types;

/**
 * Qualidade dos cultivos colhidos
 * Afeta preço de venda e aparência
 */
public enum CropQuality {
    NORMAL("Normal", 1.0, 200, 200, 200),      // Cinza claro
    SILVER("Prata", 1.25, 192, 192, 192),      // Prata
    GOLD("Ouro", 1.5, 255, 215, 0),            // Dourado
    IRIDIUM("Irídio", 2.0, 138, 43, 226);      // Roxo
    
    private final String name;
    private final double priceMultiplier;
    private final int red, green, blue;
    
    CropQuality(String name, double priceMultiplier, int red, int green, int blue) {
        this.name = name;
        this.priceMultiplier = priceMultiplier;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public String getName() { return name; }
    public double getPriceMultiplier() { return priceMultiplier; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    
    /**
     * Determina qualidade aleatória baseada em bônus
     * @param qualityBonus Bônus de 0.0 a 1.0 (nível do jogador, fertilizante, etc)
     * @return Qualidade determinada
     */
    public static CropQuality determineQuality(double qualityBonus) {
        double roll = Math.random();
        
        // Chances base: Normal 60%, Prata 25%, Ouro 12%, Irídio 3%
        // Bônus aumenta chance de qualidades superiores
        double iridiumChance = 0.03 + (qualityBonus * 0.10);
        double goldChance = 0.12 + (qualityBonus * 0.15);
        double silverChance = 0.25 + (qualityBonus * 0.20);
        
        if (roll < iridiumChance) {
            return IRIDIUM;
        } else if (roll < iridiumChance + goldChance) {
            return GOLD;
        } else if (roll < iridiumChance + goldChance + silverChance) {
            return SILVER;
        } else {
            return NORMAL;
        }
    }
    
    /**
     * Calcula preço final do cultivo
     */
    public int calculatePrice(int basePrice) {
        return (int) (basePrice * priceMultiplier);
    }
    
    /**
     * Retorna símbolo para exibição
     */
    public String getSymbol() {
        switch (this) {
            case IRIDIUM: return "★★★";
            case GOLD: return "★★";
            case SILVER: return "★";
            default: return "";
        }
    }
}

