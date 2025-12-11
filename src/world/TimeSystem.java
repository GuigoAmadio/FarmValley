package world;

/**
 * Sistema de Tempo Completo
 * Gerencia dia, hora, estação e ano
 */
public class TimeSystem {
    // Tempo do dia (em minutos do jogo, 0-1440)
    private int timeOfDay; // 0 = meia-noite, 360 = 6:00, 720 = meio-dia, 1080 = 18:00
    private static final int MINUTES_PER_DAY = 1440;
    private static final int DAWN_START = 360;    // 6:00
    private static final int DAY_START = 420;     // 7:00
    private static final int DUSK_START = 1080;   // 18:00
    private static final int NIGHT_START = 1200;  // 20:00
    
    // Velocidade do tempo (minutos de jogo por tick real)
    private int timeSpeed = 1;
    private long lastTimeUpdate;
    private static final long TIME_UPDATE_INTERVAL = 50_000_000; // 50ms
    
    // Calendário
    private int currentDay;
    private int dayOfSeason;
    private int seasonIndex;
    private int year;
    
    private static final String[] SEASONS = {"Primavera", "Verão", "Outono", "Inverno"};
    private static final int DAYS_PER_SEASON = 28;
    
    // Dias da semana
    private static final String[] WEEKDAYS = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};

    public TimeSystem() {
        this.currentDay = 1;
        this.seasonIndex = 0;
        this.dayOfSeason = 1;
        this.year = 1;
        this.timeOfDay = 360; // Começa às 6:00
        this.lastTimeUpdate = System.nanoTime();
    }
    
    /**
     * Atualiza o tempo do jogo (chamar a cada frame)
     */
    public void update() {
        long currentTime = System.nanoTime();
        if (currentTime - lastTimeUpdate >= TIME_UPDATE_INTERVAL) {
            timeOfDay += timeSpeed;
            
            // Verificar se passou da meia-noite
            if (timeOfDay >= MINUTES_PER_DAY) {
                timeOfDay = timeOfDay % MINUTES_PER_DAY;
                // Não avança dia automaticamente - jogador precisa dormir
            }
            
            lastTimeUpdate = currentTime;
        }
    }

    public void nextDay() {
        currentDay++;
        dayOfSeason++;
        timeOfDay = 360; // Reset para 6:00
        
        if (dayOfSeason > DAYS_PER_SEASON) {
            dayOfSeason = 1;
            changeSeason();
        }
    }

    private void changeSeason() {
        seasonIndex = (seasonIndex + 1) % SEASONS.length;
        
        // Verificar se completou um ano
        if (seasonIndex == 0) {
            year++;
            System.out.println("🎊 Feliz Ano Novo! Ano " + year);
        }
    }
    
    // ===== PERÍODO DO DIA =====
    
    public enum DayPeriod {
        DAWN,   // Amanhecer (6:00 - 7:00)
        DAY,    // Dia (7:00 - 18:00)
        DUSK,   // Entardecer (18:00 - 20:00)
        NIGHT   // Noite (20:00 - 6:00)
    }
    
    public DayPeriod getDayPeriod() {
        if (timeOfDay >= NIGHT_START || timeOfDay < DAWN_START) {
            return DayPeriod.NIGHT;
        } else if (timeOfDay < DAY_START) {
            return DayPeriod.DAWN;
        } else if (timeOfDay < DUSK_START) {
            return DayPeriod.DAY;
        } else {
            return DayPeriod.DUSK;
        }
    }
    
    /**
     * Retorna opacidade de escuridão (0.0 = dia claro, 0.7 = noite escura)
     */
    public double getDarknessLevel() {
        DayPeriod period = getDayPeriod();
        
        switch (period) {
            case NIGHT:
                return 0.6;
            case DAWN:
                // Gradiente de escuro para claro
                double dawnProgress = (double)(timeOfDay - DAWN_START) / (DAY_START - DAWN_START);
                return 0.6 * (1.0 - dawnProgress);
            case DUSK:
                // Gradiente de claro para escuro
                double duskProgress = (double)(timeOfDay - DUSK_START) / (NIGHT_START - DUSK_START);
                return 0.6 * duskProgress;
            default:
                return 0.0;
        }
    }
    
    /**
     * Retorna cor de iluminação baseada na hora
     * RGB normalizado (0-1)
     */
    public double[] getLightColor() {
        DayPeriod period = getDayPeriod();
        
        switch (period) {
            case DAWN:
                return new double[]{1.0, 0.85, 0.7}; // Laranja suave
            case DUSK:
                return new double[]{1.0, 0.7, 0.5}; // Laranja intenso
            case NIGHT:
                return new double[]{0.4, 0.4, 0.6}; // Azulado
            default:
                return new double[]{1.0, 1.0, 1.0}; // Branco
        }
    }
    
    // ===== FORMATAÇÃO =====
    
    public String getTimeString() {
        int hours = timeOfDay / 60;
        int minutes = timeOfDay % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
    
    public String getWeekday() {
        return WEEKDAYS[(currentDay - 1) % 7];
    }

    // ===== GETTERS =====
    
    public int getCurrentDay() { return currentDay; }
    public String getSeason() { return SEASONS[seasonIndex]; }
    public int getSeasonIndex() { return seasonIndex; }
    public int getDayOfSeason() { return dayOfSeason; }
    public int getYear() { return year; }
    public int getTimeOfDay() { return timeOfDay; }
    
    public String getDateString() {
        return String.format("%s, Dia %d de %s, Ano %d - %s", 
            getWeekday(), dayOfSeason, getSeason(), year, getTimeString());
    }
    
    public String getShortDateString() {
        return String.format("%s %d - %s", getSeason().substring(0, 3), dayOfSeason, getTimeString());
    }
    
    // ===== SETTERS =====
    
    public void setTimeSpeed(int speed) {
        this.timeSpeed = Math.max(0, Math.min(10, speed));
    }
    
    public void setTimeOfDay(int minutes) {
        this.timeOfDay = Math.max(0, Math.min(MINUTES_PER_DAY - 1, minutes));
    }
    
    /**
     * Verifica se é hora de dormir (após 20:00 ou antes das 6:00)
     */
    public boolean isSleepTime() {
        return timeOfDay >= NIGHT_START || timeOfDay < DAWN_START;
    }
    
    /**
     * Verifica se o jogador está acordado muito tarde (após 2:00)
     * Pode causar penalidades
     */
    public boolean isVeryLate() {
        return timeOfDay >= 120 && timeOfDay < DAWN_START; // 2:00 - 6:00
    }
}

