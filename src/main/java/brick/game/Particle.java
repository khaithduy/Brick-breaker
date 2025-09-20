package brick.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Particle {
    private float x, y;
    private float vx, vy;
    private Color color;
    private int life;
    private int maxLife;
    private float size;
    private static Random random = new Random();
    
    public Particle(float x, float y, Color baseColor) {
        this.x = x;
        this.y = y;
        this.vx = (random.nextFloat() - 0.5f) * 8;
        this.vy = (random.nextFloat() - 0.5f) * 8;
        this.maxLife = 60;
        this.life = maxLife;
        this.size = random.nextFloat() * 4 + 2;
        
        // Vary the color slightly
        int r = Math.min(255, Math.max(0, baseColor.getRed() + random.nextInt(100) - 50));
        int g = Math.min(255, Math.max(0, baseColor.getGreen() + random.nextInt(100) - 50));
        int b = Math.min(255, Math.max(0, baseColor.getBlue() + random.nextInt(100) - 50));
        this.color = new Color(r, g, b);
    }
    
    public void update() {
        x += vx;
        y += vy;
        vy += 0.2f; // Gravity
        vx *= 0.98f; // Air resistance
        life--;
    }
    
    public void draw(Graphics2D g2d) {
        if (life <= 0) return;
        
        float alpha = (float) life / maxLife;
        Color drawColor = new Color(
            color.getRed(),
            color.getGreen(), 
            color.getBlue(),
            (int)(255 * alpha)
        );
        
        g2d.setColor(drawColor);
        float currentSize = size * alpha;
        g2d.fillOval((int)(x - currentSize/2), (int)(y - currentSize/2), 
                    (int)currentSize, (int)currentSize);
    }
    
    public boolean isDead() {
        return life <= 0;
    }
}