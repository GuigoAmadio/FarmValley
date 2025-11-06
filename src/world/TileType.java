package world;

public enum TileType {
    GRASS(0, 0, 200, 0, true),        // Verde claro - pode andar
    DIRT(139, 90, 43, 0, true),       // Marrom - terra arada
    WATER(30, 144, 255, 0, false),    // Azul - não pode andar
    STONE(128, 128, 128, 0, false),   // Cinza - não pode andar
    PLANTED(101, 67, 33, 0, true);    // Terra com planta

    private final int red;
    private final int green;
    private final int blue;
    private final int cost;
    private final boolean walkable;

    TileType(int red, int green, int blue, int cost, boolean walkable) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.cost = cost;
        this.walkable = walkable;
    }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public int getCost() { return cost; }
    public boolean isWalkable() { return walkable; }
}

