package types;

public enum CropType {
    WHEAT("Trigo", 3, 20, 50, 255, 215, 0),
    TOMATO("Tomate", 5, 30, 80, 255, 99, 71),
    CORN("Milho", 7, 50, 120, 255, 223, 0),
    CARROT("Cenoura", 4, 25, 60, 255, 140, 0);

    private final String name;
    private final int growthTime;
    private final int seedCost;
    private final int sellPrice;
    private final int red;
    private final int green;
    private final int blue;

    CropType(String name, int growthTime, int seedCost, int sellPrice, int red, int green, int blue) {
        this.name = name;
        this.growthTime = growthTime;
        this.seedCost = seedCost;
        this.sellPrice = sellPrice;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getName() { return name; }
    public int getGrowthTime() { return growthTime; }
    public int getSeedCost() { return seedCost; }
    public int getSellPrice() { return sellPrice; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
}

