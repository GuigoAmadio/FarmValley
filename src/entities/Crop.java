package entities;

import types.CropType;

public class Crop {
    private CropType type;
    private int daysGrown;
    private boolean fullyGrown;

    public Crop(CropType type) {
        this.type = type;
        this.daysGrown = 0;
        this.fullyGrown = false;
    }

    public void grow() {
        if (!fullyGrown) {
            daysGrown++;
            if (daysGrown >= type.getGrowthTime()) {
                fullyGrown = true;
            }
        }
    }

    public boolean isFullyGrown() {
        return fullyGrown;
    }

    public CropType getType() {
        return type;
    }

    public int getDaysGrown() {
        return daysGrown;
    }

    public double getGrowthProgress() {
        return Math.min(1.0, (double) daysGrown / type.getGrowthTime());
    }
}

