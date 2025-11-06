package world;

public class TimeSystem {
    private int currentDay;
    private String season;
    private int dayOfSeason;
    private static final String[] SEASONS = {"Primavera", "Verão", "Outono", "Inverno"};
    private static final int DAYS_PER_SEASON = 28;

    public TimeSystem() {
        this.currentDay = 1;
        this.season = SEASONS[0];
        this.dayOfSeason = 1;
    }

    public void nextDay() {
        currentDay++;
        dayOfSeason++;
        
        if (dayOfSeason > DAYS_PER_SEASON) {
            dayOfSeason = 1;
            changeSeason();
        }
    }

    private void changeSeason() {
        int currentSeasonIndex = getSeasonIndex();
        int nextSeasonIndex = (currentSeasonIndex + 1) % SEASONS.length;
        season = SEASONS[nextSeasonIndex];
    }

    private int getSeasonIndex() {
        for (int i = 0; i < SEASONS.length; i++) {
            if (SEASONS[i].equals(season)) {
                return i;
            }
        }
        return 0;
    }

    public int getCurrentDay() { return currentDay; }
    public String getSeason() { return season; }
    public int getDayOfSeason() { return dayOfSeason; }
    
    public String getDateString() {
        return season + " - Dia " + dayOfSeason;
    }
}

