package world;

import entities.Crop;
import types.CropType;

/**
 * Representa um chunk (pedaço) do mapa
 * Chunks permitem carregar/descarregar partes do mapa sob demanda
 * Tamanho padrão: 32x32 tiles
 */
public class Chunk {
    private static final int CHUNK_SIZE = 32;
    
    private Tile[][] tiles;
    private int chunkX; // Posição do chunk no mapa global
    private int chunkY;
    private boolean isLoaded;
    
    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.tiles = new Tile[CHUNK_SIZE][CHUNK_SIZE];
        this.isLoaded = false;
    }
    
    /**
     * Inicializa o chunk com tiles padrão
     */
    public void initialize(TileType defaultType) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                int worldX = chunkX * CHUNK_SIZE + x;
                int worldY = chunkY * CHUNK_SIZE + y;
                tiles[x][y] = new Tile(worldX, worldY, defaultType);
            }
        }
        isLoaded = true;
    }
    
    /**
     * Obtém um tile local dentro do chunk
     */
    public Tile getLocalTile(int localX, int localY) {
        if (localX >= 0 && localX < CHUNK_SIZE && 
            localY >= 0 && localY < CHUNK_SIZE) {
            return tiles[localX][localY];
        }
        return null;
    }
    
    /**
     * Converte coordenadas do mundo para coordenadas locais do chunk
     */
    public Tile getWorldTile(int worldX, int worldY) {
        int localX = worldX - (chunkX * CHUNK_SIZE);
        int localY = worldY - (chunkY * CHUNK_SIZE);
        return getLocalTile(localX, localY);
    }
    
    /**
     * Verifica se uma coordenada do mundo está neste chunk
     */
    public boolean contains(int worldX, int worldY) {
        int chunkWorldX = chunkX * CHUNK_SIZE;
        int chunkWorldY = chunkY * CHUNK_SIZE;
        return worldX >= chunkWorldX && worldX < chunkWorldX + CHUNK_SIZE &&
               worldY >= chunkWorldY && worldY < chunkWorldY + CHUNK_SIZE;
    }
    
    public boolean isLoaded() {
        return isLoaded;
    }
    
    public void unload() {
        // Liberar memória se necessário
        // Por enquanto, manter tiles mas marcar como não carregado
        isLoaded = false;
    }
    
    public int getChunkX() { return chunkX; }
    public int getChunkY() { return chunkY; }
    public static int getChunkSize() { return CHUNK_SIZE; }
    
    /**
     * Obtém coordenada do chunk a partir de coordenadas do mundo
     */
    public static int worldToChunkCoord(int worldCoord) {
        return worldCoord < 0 ? (worldCoord / CHUNK_SIZE) - 1 : worldCoord / CHUNK_SIZE;
    }
}


