package world;

/**
 * Tipos de biomas para geração procedural do mundo
 */
public enum BiomeType {
    PLAINS("Planícies", 0, 180, 0, 0.7),       // Verde - fértil
    FOREST("Floresta", 34, 139, 34, 0.3),      // Verde escuro - muitas árvores
    DESERT("Deserto", 210, 180, 140, 0.1),     // Areia - pouco fértil
    LAKE("Lago", 30, 144, 255, 0.0),           // Azul - água
    MOUNTAIN("Montanha", 128, 128, 128, 0.2),  // Cinza - pedras
    SWAMP("Pântano", 85, 107, 47, 0.4);        // Verde musgo - úmido
    
    private final String name;
    private final int red, green, blue;
    private final double fertility; // Afeta velocidade de crescimento
    
    BiomeType(String name, int red, int green, int blue, double fertility) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.fertility = fertility;
    }
    
    public String getName() { return name; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public double getFertility() { return fertility; }
    
    /**
     * Determina bioma baseado em ruído Perlin simplificado
     */
    public static BiomeType fromNoise(double elevation, double moisture) {
        // Água em áreas baixas
        if (elevation < 0.2) {
            return LAKE;
        }
        // Montanhas em áreas altas
        if (elevation > 0.8) {
            return MOUNTAIN;
        }
        // Deserto em áreas secas
        if (moisture < 0.3) {
            return DESERT;
        }
        // Pântano em áreas muito úmidas
        if (moisture > 0.7 && elevation < 0.4) {
            return SWAMP;
        }
        // Floresta em áreas úmidas
        if (moisture > 0.5) {
            return FOREST;
        }
        // Planície padrão
        return PLAINS;
    }
}

