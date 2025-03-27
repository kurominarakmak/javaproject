package logic;


import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import player.base.Player;

public class CollisionHandler {
    private static final double COLLISION_RADIUS = 30; 

    public static void handleCollisions(Player player, List<Enemy> enemies) {
    	
        
        for (Enemy enemy : enemies) {
            double dx = player.getX() - enemy.getX();
            double dy = player.getY() - enemy.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < COLLISION_RADIUS) {
                double overlap = COLLISION_RADIUS - distance;
                dx /= distance;
                dy /= distance;

                
                player.setX(player.getX() + dx * overlap);
                player.setY(player.getY() + dy * overlap);
            }
        }

       
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy e1 = enemies.get(i);
                Enemy e2 = enemies.get(j);

                double dx = e1.getX() - e2.getX();
                double dy = e1.getY() - e2.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < COLLISION_RADIUS) {
                    double overlap = COLLISION_RADIUS - distance;
                    dx /= distance;
                    dy /= distance;

                    
                    e1.setX(e1.getX() + dx * overlap / 2);
                    e1.setY(e1.getY() + dy * overlap / 2);
                    e2.setX(e2.getX() - dx * overlap / 2);
                    e2.setY(e2.getY() - dy * overlap / 2);
                }
            }
        }
    }
    
    public static boolean canMove(double playerX, double playerY, int[][] mabObstacle) {
        int tileSize = 32;  
        
        int indexX = (int) Math.floor(playerX / tileSize);
        int indexY = (int) Math.floor(playerY / tileSize);

        if (indexX < 0 || indexX >= mabObstacle[0].length || indexY < 0 || indexY >= mabObstacle.length) {
            return false; 
        }

        return mabObstacle[indexY][indexX] == 0; 
    }
    
    public static void drawCollisionBoundaries(GraphicsContext gc, Player player, List<Enemy> enemies) {
//         Draw collision boundary for player (blue oval)
//        gc.setStroke(javafx.scene.paint.Color.BLUE);
//        gc.strokeOval(player.getX() - COLLISION_RADIUS, player.getY() - COLLISION_RADIUS, 
//                      COLLISION_RADIUS * 2, COLLISION_RADIUS * 2);

//        // Draw collision boundaries for enemies (red ovals)
//        gc.setStroke(javafx.scene.paint.Color.RED);
//        for (Enemy enemy : enemies) {
//            gc.strokeOval(enemy.getX() - COLLISION_RADIUS, enemy.getY() - COLLISION_RADIUS, 
//                          COLLISION_RADIUS * 2, COLLISION_RADIUS * 2);
//        }
    }

   
}
