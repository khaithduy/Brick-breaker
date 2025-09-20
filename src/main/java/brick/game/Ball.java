package brick.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Ball {
    private int x, y;
    private int xDir, yDir;
    private static final int SIZE = 20;
    private List<java.awt.Point> trail;
    private Color color;
    
    public Ball(int x, int y, int xDir, int yDir) {
        this.x = x;
        this.y = y;
        this.xDir = xDir;
        this.yDir = yDir;
        this.trail = new ArrayList<>();
        this.color = new Color(100, 255, 100); // Default green
    }
    
    public Ball(int x, int y, int xDir, int yDir, Color color) {
        this(x, y, xDir, yDir);
        this.color = color;
    }
    
    public void update() {
        x += xDir;
        y += yDir;
        
        // Wall collision
        if (x < 0) {
            xDir = -xDir;
            x = 0;
        }
        if (x > 670) {
            xDir = -xDir;
            x = 670;
        }
        if (y < 0) {
            yDir = -yDir;
            y = 0;
        }
        
        // Update trail
        trail.add(new java.awt.Point(x + SIZE/2, y + SIZE/2));
        if (trail.size() > 8) {
            trail.remove(0);
        }
    }
    
    public void draw(Graphics2D g2d) {
        // Draw trail
        for (int i = 0; i < trail.size(); i++) {
            float alpha = (float) i / trail.size() * 0.4f;
            Color trailColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255 * alpha));
            g2d.setColor(trailColor);
            java.awt.Point point = trail.get(i);
            int size = i + 2;
            g2d.fillOval(point.x - size/2, point.y - size/2, size, size);
        }
        
        // Draw glow effect
        for (int i = 5; i > 0; i--) {
            Color glowColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 30 - i * 5);
            g2d.setColor(glowColor);
            g2d.fillOval(x - i, y - i, SIZE + i * 2, SIZE + i * 2);
        }
        
        // Draw main ball with gradient
        java.awt.GradientPaint ballGradient = new java.awt.GradientPaint(
            x, y, color,
            x + SIZE, y + SIZE, color.darker()
        );
        g2d.setPaint(ballGradient);
        g2d.fillOval(x, y, SIZE, SIZE);
        
        // Highlight spot
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(x + 4, y + 4, 6, 6);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }
    
    public boolean paddleCollision(Rectangle paddle) {
        return getBounds().intersects(paddle);
    }
    
    public boolean isOutOfBounds() {
        return y > 570;
    }
    
    public void reversePaddleHit() {
        yDir = -yDir;
    }
    
    public void reverseBrickHitX() {
        xDir = -xDir;
    }
    
    public void reverseBrickHitY() {
        yDir = -yDir;
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getXDir() { return xDir; }
    public int getYDir() { return yDir; }
    public Color getColor() { return color; }
    
    // Setters
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setDirection(int xDir, int yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }
}