package world;

import systems.DecorationManager;
import entities.Crop;
import types.CropType;

import java.util.HashMap;
import java.util.Map;

public class Farm {
    // Opção 1: Array simples (para mapas pequenos/médios < 200x200)
    private Tile[][] tiles;
    
    // Opção 2: Sistema de chunks (para mapas grandes)
    private Map<String, Chunk> chunks;
    private boolean useChunks;
    
    private int width;
    private int height;
    private DecorationManager decorationManager;

    public Farm(int width, int height) {
        this.width = width;
        this.height = height;
        this.decorationManager = new DecorationManager();
        
        // Usar chunks apenas se o mapa for muito grande
        this.useChunks = (width * height) > 10000; // 100x100 ou maior
        
        if (useChunks) {
            this.chunks = new HashMap<>();
            System.out.println("🗺️  Usando sistema de chunks para mapa grande (" + width + "x" + height + ")");
        } else {
            this.tiles = new Tile[width][height];
        }
        
        initializeFarm();
        
        // Gerar decorações após criar o mapa
        decorationManager.generateDecorations(this);
    }
    
    /**
     * Versão otimizada: cria tiles sob demanda para mapas grandes
     */
    private void initializeFarmOptimized() {
        // Para mapas grandes, não inicializar todos os tiles de uma vez
        // Apenas inicializar chunks quando necessário
        int chunkSize = Chunk.getChunkSize();
        int chunksX = (width + chunkSize - 1) / chunkSize;
        int chunksY = (height + chunkSize - 1) / chunkSize;
        
        System.out.println("📦 Mapa dividido em " + chunksX + "x" + chunksY + " chunks");
    }

    private void initializeFarm() {
        if (useChunks) {
            // Para mapas grandes, inicializar chunks sob demanda
            initializeFarmOptimized();
            // Chunks serão criados quando necessário
            return;
        }
        
        // Mapa pequeno: inicializar tudo de uma vez
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Borda de água
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    tiles[x][y] = new Tile(x, y, TileType.WATER);
                }
                // Alguns obstáculos (pedras)
                else if ((x == 5 && y == 5) || (x == 10 && y == 8)) {
                    tiles[x][y] = new Tile(x, y, TileType.STONE);
                }
                // Resto é grama
                else {
                    tiles[x][y] = new Tile(x, y, TileType.GRASS);
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        
        if (useChunks) {
            // Sistema de chunks: carregar chunk se necessário
            return getTileFromChunk(x, y);
        } else {
            // Sistema simples: array direto
            return tiles[x][y];
        }
    }
    
    /**
     * Obtém tile usando sistema de chunks (carregamento sob demanda)
     */
    private Tile getTileFromChunk(int x, int y) {
        int chunkX = Chunk.worldToChunkCoord(x);
        int chunkY = Chunk.worldToChunkCoord(y);
        String chunkKey = chunkX + "," + chunkY;
        
        Chunk chunk = chunks.get(chunkKey);
        if (chunk == null) {
            // Criar chunk sob demanda
            chunk = new Chunk(chunkX, chunkY);
            
            // Inicializar chunk com tipo padrão baseado na posição
            TileType defaultType = determineTileType(x, y);
            chunk.initialize(defaultType);
            
            chunks.put(chunkKey, chunk);
        }
        
        return chunk.getWorldTile(x, y);
    }
    
    /**
     * Determina o tipo de tile baseado na posição (para geração procedura)
     */
    private TileType determineTileType(int x, int y) {
        // Borda de água
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return TileType.WATER;
        }
        // Alguns obstáculos (pedras) - gerar mais aleatoriamente
        else if ((x + y) % 23 == 0 && (x * y) % 17 == 0) {
            return TileType.STONE;
        }
        // Resto é grama
        else {
            return TileType.GRASS;
        }
    }

    public boolean isWalkable(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile == null || !tile.getType().isWalkable()) {
            return false;
        }
        // Verificar se há decoração bloqueando
        return decorationManager.isPositionWalkable(x, y);
    }

    public void tillSoil(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null && tile.getType() == TileType.GRASS) {
            tile.setType(TileType.DIRT);
        }
    }

    public boolean plantCrop(int x, int y, CropType cropType) {
        Tile tile = getTile(x, y);
        if (tile != null && tile.getType() == TileType.DIRT && !tile.hasCrop()) {
            tile.plantCrop(cropType);
            return true;
        }
        return false;
    }

    public Crop harvestCrop(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null && tile.hasCrop()) {
            return tile.harvestCrop();
        }
        return null;
    }

    public void growAllCrops() {
        if (useChunks) {
            // Para chunks: crescer apenas em chunks carregados
            for (Chunk chunk : chunks.values()) {
                if (chunk.isLoaded()) {
                    int chunkX = chunk.getChunkX();
                    int chunkY = chunk.getChunkY();
                    int chunkSize = Chunk.getChunkSize();
                    
                    for (int x = 0; x < chunkSize; x++) {
                        for (int y = 0; y < chunkSize; y++) {
                            Tile tile = chunk.getLocalTile(x, y);
                            if (tile != null) {
                                tile.growCrop();
                            }
                        }
                    }
                }
            }
        } else {
            // Sistema simples
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (tiles[x][y] != null) {
                        tiles[x][y].growCrop();
                    }
                }
            }
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public DecorationManager getDecorationManager() { return decorationManager; }
}

