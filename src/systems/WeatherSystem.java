package systems;

import java.util.Random;

/**
 * Sistema de clima do jogo
 * Controla chuva, neve, sol e efeitos visuais
 */
public class WeatherSystem {
    private WeatherType currentWeather;
    private WeatherType nextWeather;
    private double transitionProgress;
    private double intensity;
    private Random random;
    
    // Temporizadores
    private long weatherDuration;
    private long weatherStartTime;
    private static final long MIN_WEATHER_DURATION = 60000; // 1 minuto
    private static final long MAX_WEATHER_DURATION = 180000; // 3 minutos
    
    public enum WeatherType {
        SUNNY("Ensolarado", 1.0, 0),
        CLOUDY("Nublado", 0.7, 0),
        RAINY("Chuvoso", 0.4, 1),
        STORMY("Tempestade", 0.2, 2),
        SNOWY("Nevando", 0.5, 1);
        
        private final String name;
        private final double brightness;
        private final int particleLevel; // 0 = none, 1 = light, 2 = heavy
        
        WeatherType(String name, double brightness, int particleLevel) {
            this.name = name;
            this.brightness = brightness;
            this.particleLevel = particleLevel;
        }
        
        public String getName() { return name; }
        public double getBrightness() { return brightness; }
        public int getParticleLevel() { return particleLevel; }
    }
    
    public WeatherSystem() {
        this.random = new Random();
        this.currentWeather = WeatherType.SUNNY;
        this.nextWeather = WeatherType.SUNNY;
        this.transitionProgress = 1.0;
        this.intensity = 1.0;
        this.weatherStartTime = System.currentTimeMillis();
        this.weatherDuration = MIN_WEATHER_DURATION + random.nextLong() % (MAX_WEATHER_DURATION - MIN_WEATHER_DURATION);
    }
    
    /**
     * Atualiza o sistema de clima
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // Verificar se é hora de mudar o clima
        if (currentTime - weatherStartTime >= weatherDuration) {
            changeWeather();
        }
        
        // Atualizar transição
        if (transitionProgress < 1.0) {
            transitionProgress += 0.01;
            if (transitionProgress >= 1.0) {
                transitionProgress = 1.0;
                currentWeather = nextWeather;
            }
        }
        
        // Variar intensidade ligeiramente
        intensity = 0.8 + random.nextDouble() * 0.4;
    }
    
    /**
     * Muda para um novo clima aleatório
     */
    public void changeWeather() {
        nextWeather = getRandomWeather();
        transitionProgress = 0.0;
        weatherStartTime = System.currentTimeMillis();
        weatherDuration = MIN_WEATHER_DURATION + random.nextLong() % (MAX_WEATHER_DURATION - MIN_WEATHER_DURATION);
        
        System.out.println("🌤️ Clima mudando para: " + nextWeather.getName());
    }
    
    private WeatherType getRandomWeather() {
        double roll = random.nextDouble();
        
        // Probabilidades
        if (roll < 0.4) return WeatherType.SUNNY;
        if (roll < 0.6) return WeatherType.CLOUDY;
        if (roll < 0.85) return WeatherType.RAINY;
        if (roll < 0.95) return WeatherType.STORMY;
        return WeatherType.SNOWY;
    }
    
    /**
     * Força um clima específico
     */
    public void setWeather(WeatherType weather) {
        this.nextWeather = weather;
        this.transitionProgress = 0.0;
        this.weatherStartTime = System.currentTimeMillis();
    }
    
    /**
     * Retorna brilho atual (para escurecer a tela)
     */
    public double getBrightness() {
        if (transitionProgress >= 1.0) {
            return currentWeather.getBrightness();
        }
        
        // Interpolar entre os dois climas
        double currentBright = currentWeather.getBrightness();
        double nextBright = nextWeather.getBrightness();
        return currentBright + (nextBright - currentBright) * transitionProgress;
    }
    
    /**
     * Verifica se deve spawnar partículas de chuva
     */
    public boolean shouldSpawnRain() {
        WeatherType effective = transitionProgress >= 0.5 ? nextWeather : currentWeather;
        return (effective == WeatherType.RAINY || effective == WeatherType.STORMY) 
               && random.nextDouble() < intensity * 0.3;
    }
    
    /**
     * Verifica se deve spawnar flocos de neve
     */
    public boolean shouldSpawnSnow() {
        WeatherType effective = transitionProgress >= 0.5 ? nextWeather : currentWeather;
        return effective == WeatherType.SNOWY && random.nextDouble() < intensity * 0.2;
    }
    
    /**
     * Retorna cor do overlay do clima
     */
    public double[] getOverlayColor() {
        WeatherType effective = transitionProgress >= 0.5 ? nextWeather : currentWeather;
        
        switch (effective) {
            case RAINY:
                return new double[]{0.3, 0.3, 0.5, 0.2}; // Azul escuro
            case STORMY:
                return new double[]{0.2, 0.2, 0.3, 0.4}; // Cinza escuro
            case SNOWY:
                return new double[]{0.8, 0.8, 0.9, 0.15}; // Branco
            case CLOUDY:
                return new double[]{0.5, 0.5, 0.5, 0.1}; // Cinza
            default:
                return new double[]{0, 0, 0, 0}; // Transparente
        }
    }
    
    /**
     * Bônus de crescimento baseado no clima
     */
    public double getGrowthMultiplier() {
        switch (currentWeather) {
            case RAINY: return 1.5;  // Chuva ajuda!
            case SUNNY: return 1.2;  // Sol é bom
            case CLOUDY: return 1.0; // Normal
            case STORMY: return 0.8; // Tempestade prejudica
            case SNOWY: return 0.5;  // Neve é ruim
            default: return 1.0;
        }
    }
    
    /**
     * Verifica se tem relâmpago (para efeito visual)
     */
    public boolean shouldFlashLightning() {
        return currentWeather == WeatherType.STORMY && random.nextDouble() < 0.005;
    }
    
    // Getters
    public WeatherType getCurrentWeather() { return currentWeather; }
    public WeatherType getNextWeather() { return nextWeather; }
    public double getTransitionProgress() { return transitionProgress; }
    public double getIntensity() { return intensity; }
    
    public String getWeatherIcon() {
        switch (currentWeather) {
            case SUNNY: return "☀️";
            case CLOUDY: return "☁️";
            case RAINY: return "🌧️";
            case STORMY: return "⛈️";
            case SNOWY: return "❄️";
            default: return "🌤️";
        }
    }
    
    public String getWeatherName() {
        return currentWeather.getName();
    }
}

