package systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Sistema de partículas para efeitos visuais
 * Cria efeitos de colheita, dano, level up, clima, etc.
 */
public class ParticleSystem {
    private List<Particle> particles;
    private Random random;
    private static final int MAX_PARTICLES = 500;
    
    public ParticleSystem() {
        this.particles = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Atualiza todas as partículas
     */
    public void update() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) {
                it.remove();
            }
        }
    }
    
    /**
     * Renderiza todas as partículas
     */
    public void render(GraphicsContext gc, int offsetX, int offsetY, int tileSize) {
        for (Particle p : particles) {
            p.render(gc, offsetX, offsetY, tileSize);
        }
    }
    
    // ===== EFEITOS ESPECÍFICOS =====
    
    /**
     * Efeito de colheita - partículas douradas subindo
     */
    public void spawnHarvestEffect(int tileX, int tileY) {
        for (int i = 0; i < 15; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 0.5 + random.nextDouble() * 1.5;
            double vx = Math.cos(angle) * speed;
            double vy = -1 - random.nextDouble() * 2; // Sempre sobe
            
            Color color = random.nextBoolean() ? 
                Color.rgb(255, 215, 0) : // Dourado
                Color.rgb(255, 255, 100); // Amarelo claro
            
            particles.add(new Particle(
                tileX + 0.5, tileY + 0.5,
                vx, vy,
                color,
                3 + random.nextInt(4),
                30 + random.nextInt(30),
                ParticleType.HARVEST
            ));
        }
    }
    
    /**
     * Efeito de dano - partículas vermelhas
     */
    public void spawnDamageEffect(int tileX, int tileY) {
        for (int i = 0; i < 12; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 1 + random.nextDouble() * 2;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            
            Color color = Color.rgb(
                200 + random.nextInt(55),
                random.nextInt(50),
                random.nextInt(50)
            );
            
            particles.add(new Particle(
                tileX + 0.5, tileY + 0.5,
                vx, vy,
                color,
                4 + random.nextInt(3),
                20 + random.nextInt(20),
                ParticleType.DAMAGE
            ));
        }
    }
    
    /**
     * Efeito de level up - explosão de estrelas
     */
    public void spawnLevelUpEffect(int tileX, int tileY) {
        for (int i = 0; i < 30; i++) {
            double angle = (i / 30.0) * Math.PI * 2;
            double speed = 2 + random.nextDouble() * 2;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            
            Color[] colors = {
                Color.rgb(255, 215, 0),   // Dourado
                Color.rgb(255, 255, 255), // Branco
                Color.rgb(255, 165, 0),   // Laranja
                Color.rgb(255, 255, 100)  // Amarelo
            };
            
            particles.add(new Particle(
                tileX + 0.5, tileY + 0.5,
                vx, vy,
                colors[random.nextInt(colors.length)],
                5 + random.nextInt(5),
                40 + random.nextInt(30),
                ParticleType.LEVELUP
            ));
        }
    }
    
    /**
     * Efeito de coleta de recurso - partículas do tipo do recurso
     */
    public void spawnResourceEffect(int tileX, int tileY, String resourceType) {
        Color color;
        switch (resourceType.toLowerCase()) {
            case "wood":
            case "madeira":
                color = Color.rgb(139, 90, 43);
                break;
            case "stone":
            case "pedra":
                color = Color.rgb(128, 128, 128);
                break;
            case "fiber":
            case "fibra":
                color = Color.rgb(50, 150, 50);
                break;
            default:
                color = Color.rgb(200, 200, 200);
        }
        
        for (int i = 0; i < 10; i++) {
            double vx = (random.nextDouble() - 0.5) * 2;
            double vy = -1 - random.nextDouble() * 1.5;
            
            particles.add(new Particle(
                tileX + 0.5, tileY + 0.5,
                vx, vy,
                color,
                3 + random.nextInt(3),
                25 + random.nextInt(20),
                ParticleType.RESOURCE
            ));
        }
    }
    
    /**
     * Efeito de cura - partículas verdes/rosa
     */
    public void spawnHealEffect(int tileX, int tileY) {
        for (int i = 0; i < 20; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 0.3 + random.nextDouble() * 0.5;
            double vx = Math.cos(angle) * speed;
            double vy = -0.5 - random.nextDouble(); // Sobe devagar
            
            Color color = random.nextBoolean() ?
                Color.rgb(100, 255, 100) : // Verde claro
                Color.rgb(255, 150, 200);  // Rosa
            
            particles.add(new Particle(
                tileX + random.nextDouble() - 0.5, 
                tileY + random.nextDouble() - 0.5,
                vx, vy,
                color,
                4 + random.nextInt(3),
                40 + random.nextInt(30),
                ParticleType.HEAL
            ));
        }
    }
    
    /**
     * Efeito de chuva
     */
    public void spawnRainDrop(int screenWidth, int screenHeight, int tileSize) {
        if (particles.size() < MAX_PARTICLES) {
            double x = random.nextDouble() * (screenWidth / (double)tileSize + 4) - 2;
            double y = -1;
            
            particles.add(new Particle(
                x, y,
                -0.5, 8, // Cai diagonal
                Color.rgb(100, 150, 255, 0.6),
                2,
                60,
                ParticleType.RAIN
            ));
        }
    }
    
    /**
     * Efeito de neve
     */
    public void spawnSnowflake(int screenWidth, int screenHeight, int tileSize) {
        if (particles.size() < MAX_PARTICLES) {
            double x = random.nextDouble() * (screenWidth / (double)tileSize + 4) - 2;
            double y = -1;
            
            particles.add(new Particle(
                x, y,
                (random.nextDouble() - 0.5) * 0.5, // Leve movimento horizontal
                1 + random.nextDouble(), // Cai devagar
                Color.rgb(255, 255, 255, 0.8),
                3 + random.nextInt(3),
                120,
                ParticleType.SNOW
            ));
        }
    }
    
    /**
     * Efeito de ataque
     */
    public void spawnAttackEffect(int tileX, int tileY, String direction) {
        double baseAngle = 0;
        switch (direction) {
            case "UP": baseAngle = -Math.PI / 2; break;
            case "DOWN": baseAngle = Math.PI / 2; break;
            case "LEFT": baseAngle = Math.PI; break;
            case "RIGHT": baseAngle = 0; break;
        }
        
        for (int i = 0; i < 8; i++) {
            double angle = baseAngle + (random.nextDouble() - 0.5) * Math.PI / 3;
            double speed = 3 + random.nextDouble() * 2;
            
            particles.add(new Particle(
                tileX + 0.5, tileY + 0.5,
                Math.cos(angle) * speed,
                Math.sin(angle) * speed,
                Color.rgb(255, 255, 255, 0.9),
                2 + random.nextInt(2),
                10 + random.nextInt(10),
                ParticleType.ATTACK
            ));
        }
    }
    
    public int getParticleCount() {
        return particles.size();
    }
    
    // ===== CLASSES INTERNAS =====
    
    public enum ParticleType {
        HARVEST, DAMAGE, LEVELUP, RESOURCE, HEAL, RAIN, SNOW, ATTACK
    }
    
    private class Particle {
        double x, y;
        double vx, vy;
        Color color;
        double size;
        int lifetime;
        int maxLifetime;
        ParticleType type;
        double gravity;
        double friction;
        
        public Particle(double x, double y, double vx, double vy, 
                       Color color, double size, int lifetime, ParticleType type) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.size = size;
            this.lifetime = lifetime;
            this.maxLifetime = lifetime;
            this.type = type;
            
            // Configurar física baseada no tipo
            switch (type) {
                case HARVEST:
                case HEAL:
                    gravity = -0.02; // Sobe
                    friction = 0.98;
                    break;
                case RAIN:
                    gravity = 0.1;
                    friction = 1;
                    break;
                case SNOW:
                    gravity = 0;
                    friction = 0.99;
                    break;
                case LEVELUP:
                    gravity = 0.05;
                    friction = 0.95;
                    break;
                default:
                    gravity = 0.1;
                    friction = 0.95;
            }
        }
        
        public void update() {
            // Física
            vy += gravity;
            vx *= friction;
            vy *= friction;
            
            x += vx * 0.1;
            y += vy * 0.1;
            
            // Movimento especial para neve
            if (type == ParticleType.SNOW) {
                vx += (random.nextDouble() - 0.5) * 0.1;
            }
            
            lifetime--;
        }
        
        public void render(GraphicsContext gc, int offsetX, int offsetY, int tileSize) {
            double screenX = x * tileSize + offsetX;
            double screenY = y * tileSize + offsetY;
            
            // Fade out
            double alpha = (double) lifetime / maxLifetime;
            
            // Ajustar cor com alpha
            Color renderColor = Color.rgb(
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                alpha * color.getOpacity()
            );
            
            gc.setFill(renderColor);
            
            // Forma baseada no tipo
            switch (type) {
                case RAIN:
                    // Linha diagonal
                    gc.setStroke(renderColor);
                    gc.setLineWidth(1);
                    gc.strokeLine(screenX, screenY, screenX + 3, screenY + 8);
                    break;
                case SNOW:
                    // Flocos
                    gc.fillOval(screenX - size/2, screenY - size/2, size, size);
                    break;
                case LEVELUP:
                    // Estrelas
                    drawStar(gc, screenX, screenY, size, renderColor);
                    break;
                default:
                    // Círculos
                    double currentSize = size * (0.5 + alpha * 0.5);
                    gc.fillOval(screenX - currentSize/2, screenY - currentSize/2, 
                               currentSize, currentSize);
            }
        }
        
        private void drawStar(GraphicsContext gc, double cx, double cy, double size, Color color) {
            gc.setFill(color);
            double[] xPoints = new double[10];
            double[] yPoints = new double[10];
            
            for (int i = 0; i < 10; i++) {
                double angle = Math.PI / 2 + i * Math.PI / 5;
                double r = (i % 2 == 0) ? size : size / 2;
                xPoints[i] = cx + r * Math.cos(angle);
                yPoints[i] = cy - r * Math.sin(angle);
            }
            
            gc.fillPolygon(xPoints, yPoints, 10);
        }
        
        public boolean isDead() {
            return lifetime <= 0;
        }
    }
}

