
package brick.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
     public int map[][];
    public int bricksWidth;
    public int bricksHeight;
    public MapGenerator(int row , int col){
        map = new int[row][col];
         for (int[] map1 : map) {
             for (int j = 0; j < map[0].length; j++) {
                 map1[j] = 1;
             }
         }
        bricksWidth = 540/col;
        bricksHeight = 150/row;
    }
    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Different colors for each row
                    Color brickColor;
                    switch (i) {
                        case 0: brickColor = new Color(255, 100, 100); break; // Red
                        case 1: brickColor = new Color(100, 255, 100); break; // Green  
                        case 2: brickColor = new Color(100, 100, 255); break; // Blue
                        default: brickColor = new Color(255, 255, 100); break; // Yellow
                    }
                    
                    int x = j * bricksWidth + 80;
                    int y = i * bricksHeight + 50;
                    
                    // Draw brick with gradient effect
                    java.awt.GradientPaint brickGradient = new java.awt.GradientPaint(
                        x, y, brickColor,
                        x, y + bricksHeight, brickColor.darker()
                    );
                    g.setPaint(brickGradient);
                    g.fillRoundRect(x, y, bricksWidth, bricksHeight, 8, 8);
                    
                    // Highlight on top
                    g.setColor(new Color(255, 255, 255, 100));
                    g.fillRoundRect(x + 2, y + 2, bricksWidth - 4, 4, 4, 4);
                    
                    // Border
                    g.setStroke(new BasicStroke(2));
                    g.setColor(brickColor.darker().darker());
                    g.drawRoundRect(x, y, bricksWidth, bricksHeight, 8, 8);

                }
            }

        }
    }
    public void setBricksValue(int value,int row,int col)
    {
        map[row][col] = value;

    }
    
}
