package world;

import java.util.Random;

/**
 * Gerador procedural de mundo
 * Usa Perlin Noise simplificado para criar terrenos naturais
 */
public class WorldGenerator {
    private Random random;
    private long seed;
    
    // Parâmetros de geração
    private double elevationScale = 0.05;  // Escala do ruído de elevação
    private double moistureScale = 0.08;   // Escala do ruído de umidade
    
    public WorldGenerator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }
    
    public WorldGenerator() {
        this(System.currentTimeMillis());
    }
    
    /**
     * Gera um array de tipos de tile para o mapa
     */
    public TileType[][] generateTerrain(int width, int height) {
        TileType[][] terrain = new TileType[width][height];
        
        // Calcular centro para área inicial
        int centerX = width / 2;
        int centerY = height / 2;
        int safeRadius = 8; // Área segura ao redor do spawn
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Borda do mapa é sempre água
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    terrain[x][y] = TileType.WATER;
                    continue;
                }
                
                // Área inicial é sempre grama limpa
                double distFromCenter = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                if (distFromCenter < safeRadius) {
                    terrain[x][y] = TileType.GRASS;
                    continue;
                }
                
                // Gerar ruído para elevação e umidade
                double elevation = generateNoise(x, y, elevationScale, seed);
                double moisture = generateNoise(x, y, moistureScale, seed + 1000);
                
                // Determinar bioma e tipo de tile
                BiomeType biome = BiomeType.fromNoise(elevation, moisture);
                terrain[x][y] = biomeToTile(biome, elevation);
            }
        }
        
        // Suavizar o terreno (remover tiles isolados)
        smoothTerrain(terrain, width, height);
        
        return terrain;
    }
    
    /**
     * Converte bioma para tipo de tile
     */
    private TileType biomeToTile(BiomeType biome, double elevation) {
        switch (biome) {
            case LAKE:
                return TileType.WATER;
            case MOUNTAIN:
                // Pedras em altitudes muito altas
                return elevation > 0.9 ? TileType.STONE : TileType.GRASS;
            case DESERT:
            case SWAMP:
            case FOREST:
            case PLAINS:
            default:
                return TileType.GRASS;
        }
    }
    
    /**
     * Ruído Perlin simplificado (Value Noise)
     */
    private double generateNoise(int x, int y, double scale, long noiseSeed) {
        // Usar função hash para gerar valor pseudo-aleatório
        double nx = x * scale;
        double ny = y * scale;
        
        int xi = (int) Math.floor(nx);
        int yi = (int) Math.floor(ny);
        
        double xf = nx - xi;
        double yf = ny - yi;
        
        // Valores nos 4 cantos
        double v00 = hash(xi, yi, noiseSeed);
        double v10 = hash(xi + 1, yi, noiseSeed);
        double v01 = hash(xi, yi + 1, noiseSeed);
        double v11 = hash(xi + 1, yi + 1, noiseSeed);
        
        // Interpolação suave
        double sx = smoothstep(xf);
        double sy = smoothstep(yf);
        
        double nx0 = lerp(v00, v10, sx);
        double nx1 = lerp(v01, v11, sx);
        
        return lerp(nx0, nx1, sy);
    }
    
    /**
     * Função hash para gerar valor pseudo-aleatório
     */
    private double hash(int x, int y, long noiseSeed) {
        long n = x + y * 57 + noiseSeed;
        n = (n << 13) ^ n;
        return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0) * 0.5 + 0.5;
    }
    
    /**
     * Interpolação linear
     */
    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }
    
    /**
     * Função smoothstep para interpolação suave
     */
    private double smoothstep(double t) {
        return t * t * (3 - 2 * t);
    }
    
    /**
     * Suaviza o terreno removendo tiles isolados
     */
    private void smoothTerrain(TileType[][] terrain, int width, int height) {
        for (int pass = 0; pass < 2; pass++) {
            for (int x = 1; x < width - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    // Contar vizinhos do mesmo tipo
                    TileType current = terrain[x][y];
                    int sameCount = 0;
                    
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) continue;
                            if (terrain[x + dx][y + dy] == current) {
                                sameCount++;
                            }
                        }
                    }
                    
                    // Se muito isolado, converter para tipo dominante
                    if (sameCount < 2) {
                        // Encontrar tipo mais comum entre vizinhos
                        terrain[x][y] = findDominantNeighbor(terrain, x, y);
                    }
                }
            }
        }
    }
    
    /**
     * Encontra o tipo de tile mais comum entre vizinhos
     */
    private TileType findDominantNeighbor(TileType[][] terrain, int x, int y) {
        int grassCount = 0;
        int waterCount = 0;
        int stoneCount = 0;
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                TileType neighbor = terrain[x + dx][y + dy];
                if (neighbor == TileType.GRASS) grassCount++;
                else if (neighbor == TileType.WATER) waterCount++;
                else if (neighbor == TileType.STONE) stoneCount++;
            }
        }
        
        if (waterCount > grassCount && waterCount > stoneCount) return TileType.WATER;
        if (stoneCount > grassCount) return TileType.STONE;
        return TileType.GRASS;
    }
    
    public long getSeed() { return seed; }
}

