
package brick.game;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author chinm
 */
public class GamePlay extends JPanel implements KeyListener, ActionListener {
    
     private boolean play = false;
    private int score = 0;
    private int totalbricks = 21;
    private Timer Timer;
    private int delay = 8;
    private int playerX = 310;
    private MapGenerator map;
    
    // Multiple ball system
    private java.util.List<Ball> balls = new java.util.ArrayList<>();
    
    // Enhanced features
    private java.util.List<Particle> particles = new java.util.ArrayList<>();
    private int level = 1;
    private boolean showFPS = true;
    private long lastFPSTime = System.currentTimeMillis();
    private int fps = 0;
    private int frameCount = 0;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(Color.BLACK);
        
        // Initialize with one ball
        balls.add(new Ball(120, 350, -1, -2));
        
        Timer = new Timer(delay, this);
        Timer.start();
    }
    
     public void paint(Graphics g) {
        // Clear the background first
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw gradient background (dark blue to black)
        java.awt.GradientPaint gradient = new java.awt.GradientPaint(
            0, 0, new Color(20, 30, 60), 
            0, 592, new Color(5, 10, 25)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g);

        // Draw borders with neon effect
        g2d.setColor(new Color(0, 255, 255)); // Cyan
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawRect(1, 1, 690, 590);
        
        // Inner glow effect
        g2d.setColor(new Color(0, 200, 255, 100));
        g2d.setStroke(new java.awt.BasicStroke(1));
        g2d.drawRect(2, 2, 688, 588);

        // Score display with modern styling
        g2d.setColor(new Color(255, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("SCORE: " + score, 520, 35);
        
        // Level display
        g2d.setColor(new Color(255, 200, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("LEVEL " + level, 520, 60);
        
        // Balls count display
        g2d.setColor(new Color(100, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("BALLS: " + balls.size(), 520, 85);
        
        // FPS display
        if (showFPS) {
            g2d.setColor(new Color(100, 255, 100));
            g2d.setFont(new Font("Courier", Font.PLAIN, 12));
            g2d.drawString("FPS: " + fps, 10, 25);
        }

        // Draw particles
        drawParticles(g2d);

        // Draw enhanced paddle
        drawEnhancedPaddle(g2d, playerX, 550);

        // Draw all balls
        for (Ball ball : balls) {
            ball.draw(g2d);
        }

        // Game over check is now handled in actionPerformed when balls.isEmpty()
        if (!play && balls.isEmpty()) {
            // Modern game over screen
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, 692, 592);
            
            g2d.setColor(new Color(255, 50, 50));
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.drawString("GAME OVER", 250, 280);
            
            g2d.setColor(new Color(255, 255, 255));
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            g2d.drawString("Final Score: " + score, 280, 320);
            g2d.drawString("Level Reached: " + level, 270, 350);
            
            g2d.setColor(new Color(0, 255, 255));
            g2d.setFont(new Font("Arial", Font.ITALIC, 16));
            g2d.drawString("Press Enter to Restart", 270, 380);
        }
        
        if(totalbricks == 0){
            // Level complete!
            level++;
            totalbricks = 21;
            map = new MapGenerator(3, 7);
            
            // Reset balls with increased speed
            balls.clear();
            balls.add(new Ball(120, 350, -1, -2 - level, new Color(100, 255, 100)));
            
            // Show level complete message briefly
            play = false;
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, 692, 592);
            
            g2d.setColor(new Color(100, 255, 100));
            g2d.setFont(new Font("Arial", Font.BOLD, 32));
            g2d.drawString("LEVEL " + (level-1) + " COMPLETE!", 200, 280);
            
            g2d.setColor(new Color(255, 255, 255));
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString("Get ready for Level " + level + "!", 260, 320);
            
            // Auto continue after brief pause
            Timer levelTimer = new Timer(2000, evt -> {
                play = true;
                ((Timer)evt.getSource()).stop();
            });
            levelTimer.setRepeats(false);
            levelTimer.start();
        }

        g.dispose();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Timer.start();
        
        // Update FPS counter
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFPSTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFPSTime = currentTime;
        }
        
        // Update particles
        particles.removeIf(Particle::isDead);
        particles.forEach(Particle::update);

        if (play) {
            // Update all balls
            Rectangle paddleRect = new Rectangle(playerX, 550, 100, 8);
            
            // Check ball-paddle collisions and ball updates
            for (Ball ball : balls) {
                ball.update();
                
                if (ball.paddleCollision(paddleRect)) {
                    ball.reversePaddleHit();
                    createPaddleParticles();
                }
            }
            
            // Remove balls that are out of bounds
            balls.removeIf(Ball::isOutOfBounds);
            
            // Game over if no balls left
            if (balls.isEmpty()) {
                play = false;
            }

            // Check ball-brick collisions
            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.bricksWidth + 80;
                        int brickY = i * map.bricksHeight + 50;
                        int bricksWidth = map.bricksWidth;
                        int bricksHeight = map.bricksHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                        
                        for (Ball ball : balls) {
                            if (ball.getBounds().intersects(brickRect)) {
                                // Create brick destruction particles
                                createBrickParticles(brickX + bricksWidth/2, brickY + bricksHeight/2, getBrickColor(i));
                                
                                map.setBricksValue(0, i, j);
                                totalbricks--;
                                score += 5 * level; // Score multiplier by level
                                
                                // 20% chance to spawn extra ball when brick is destroyed
                                if (Math.random() < 0.2 && balls.size() < 5) {
                                    spawnExtraBall(ball);
                                }
                                
                                if (ball.getX() + 19 <= brickRect.x || ball.getX() + 1 >= brickRect.x + bricksWidth) {
                                    ball.reverseBrickHitX();
                                } else {
                                    ball.reverseBrickHitY();
                                }
                                break A;
                            }
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

       }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                // Reset game
                balls.clear();
                balls.add(new Ball(120, 350, -1, -2));
                score = 0;
                level = 1;
                playerX = 310;
                totalbricks = 21;
                map = new MapGenerator(3, 7);
                particles.clear();
                play = true;

                repaint();
            }
        }
        
        // Toggle FPS display with F key
        if (e.getKeyCode() == KeyEvent.VK_F) {
            showFPS = !showFPS;
        }
        
        // Spawn extra ball manually with SPACE key
        if (e.getKeyCode() == KeyEvent.VK_SPACE && play && balls.size() < 5) {
            if (!balls.isEmpty()) {
                spawnExtraBall(balls.get(0));
            }
        }


        }

        public void moveRight ()
        {
            play = true;
            playerX += 20;
        }
        public void moveLeft ()
        {
            play = true;
            playerX -= 20;
        }
        
        // Enhanced Paddle Drawing Method
        private void drawEnhancedPaddle(Graphics2D g2d, int x, int y) {
            // Main paddle body with gradient
            java.awt.GradientPaint paddleGradient = new java.awt.GradientPaint(
                x, y, new Color(255, 100, 0),  // Orange
                x, y + 8, new Color(255, 200, 0) // Yellow
            );
            g2d.setPaint(paddleGradient);
            g2d.fillRoundRect(x, y, 100, 8, 4, 4);
            
            // Top highlight
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.fillRoundRect(x + 2, y, 96, 2, 2, 2);
            
            // Bottom shadow
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(x + 2, y + 6, 96, 2, 2, 2);
        }
        
        // Enhanced Ball Drawing Method
        private void drawEnhancedBall(Graphics2D g2d, int x, int y) {
            // Glow effect
            for (int i = 5; i > 0; i--) {
                g2d.setColor(new Color(0, 255, 100, 30 - i * 5));
                g2d.fillOval(x - i, y - i, 20 + i * 2, 20 + i * 2);
            }
            
            // Main ball with gradient
            java.awt.GradientPaint ballGradient = new java.awt.GradientPaint(
                x, y, new Color(100, 255, 100),  // Light green
                x + 20, y + 20, new Color(0, 200, 50) // Dark green
            );
            g2d.setPaint(ballGradient);
            g2d.fillOval(x, y, 20, 20);
            
            // Highlight spot
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.fillOval(x + 4, y + 4, 6, 6);
        }
        
        // Particle system methods
        private void drawParticles(Graphics2D g2d) {
            for (Particle particle : particles) {
                particle.draw(g2d);
            }
        }
        
        // Multiple ball system methods
        private void spawnExtraBall(Ball baseBall) {
            // Create different colored balls
            Color[] ballColors = {
                new Color(255, 100, 100), // Red
                new Color(100, 100, 255), // Blue  
                new Color(255, 255, 100), // Yellow
                new Color(255, 100, 255), // Magenta
                new Color(100, 255, 255)  // Cyan
            };
            
            Color newColor = ballColors[(int)(Math.random() * ballColors.length)];
            
            // Spawn with slightly different direction
            int newXDir = baseBall.getXDir() + (int)(Math.random() * 4 - 2);
            int newYDir = baseBall.getYDir() + (int)(Math.random() * 4 - 2);
            
            Ball newBall = new Ball(baseBall.getX(), baseBall.getY(), newXDir, newYDir, newColor);
            balls.add(newBall);
            
            // Create spawn particles
            createBrickParticles(baseBall.getX() + 10, baseBall.getY() + 10, newColor);
        }
        
        private void createBrickParticles(int x, int y, Color color) {
            for (int i = 0; i < 8; i++) {
                particles.add(new Particle(x, y, color));
            }
        }
        
        private void createPaddleParticles() {
            Color paddleColor = new Color(255, 150, 0);
            for (int i = 0; i < 5; i++) {
                particles.add(new Particle(playerX + 50, 550, paddleColor));
            }
        }
        
        private Color getBrickColor(int row) {
            switch (row) {
                case 0: return new Color(255, 100, 100); // Red
                case 1: return new Color(100, 255, 100); // Green  
                case 2: return new Color(100, 100, 255); // Blue
                default: return new Color(255, 255, 100); // Yellow
            }
        }
    
}
