package entity.projectile;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Kaboom extends Projectile{
	
	private static final int totalFrames = 60;
	private static final Image[] frames = new Image[totalFrames];
	
	private double deltaTimeFix = 0.01667;
	
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.2;
    private double angle;
    private static double frameWidth;
    private static double frameHeight;
    
    private double stopX; 
    private double stopY; 
    private boolean hasStopped = false; 
    
    
    static {
        new Thread(() -> {
            for (int i = 0; i < totalFrames; i++) {
                String path = String.format("projectiles/kaboom/Effect_Kabooms_1_%03d.png", i);
                frames[i] = new Image(ClassLoader.getSystemResourceAsStream(path));
                
            }
            
            
        }).start();
    }

	public Kaboom(double x, double y, double targetX, double targetY, long now) {
		super(x, y, targetX, targetY, now);
		setDAMAGE(100);
		setLIFETIME(2_000_000_000L);
		setSpeed(1.5);
		
		Image justForPull = new Image(ClassLoader.getSystemResourceAsStream("projectiles/kaboom/Effect_Kabooms_1_000.png"));
        // Get frame size from first image
		frameWidth = justForPull.getWidth();
        frameHeight = justForPull.getHeight();

        // Calculate angle
        double dx = targetX - x;
        double dy = targetY - y;
        angle = Math.toDegrees(Math.atan2(dy, dx)) + 90;

        // **Calculate stopping position (300 pixels past the target)**
        double length = Math.sqrt(dx * dx + dy * dy);
        stopX = targetX + (dx / length) * 300; // Move 300 pixels past
        stopY = targetY + (dy / length) * 300;
        

	}
	
	public void update(List<Enemy> enemies, double deltaTime, long now) {
        if (!isActive) return;

        double dx = stopX - x;
        double dy = stopY - y;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length > 3) {
            dx /= length;
            dy /= length;
            x += dx * speed;
            y += dy * speed;
        } else {
            hasStopped = true; // Mark projectile as stopped
        }
    
//       angle = Math.toDegrees(Math.atan2(dy, dx));

        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (checkCollision(enemy)) {
                enemy.takeDamage(DAMAGE);
//                isActive = false;
                if (enemy.isDead()) {
                    iterator.remove();
                }
                break;
            }
        }

      
        if (now - spawnTime > LIFETIME) {
            isActive = false;
        }
        
        animationTimer+= deltaTime;
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            animationTimer =0;
        }
    }
	
	@Override
	public boolean checkCollision(Enemy enemy) {
        return Math.hypot(enemy.getX() - x, enemy.getY() - y) < 100;
    }
	
	@Override
	public void draw(GraphicsContext gc) {
        if (isActive) {
        	
        	gc.save(); 
            gc.translate(x, y);
            gc.rotate(angle);
            Image currentImage = frames[currentFrame];
            gc.drawImage(currentImage, -currentImage.getWidth() / 2, -currentImage.getHeight() / 2,frameWidth-100,frameHeight-100);
            gc.restore(); 
        }
    }
	
	

}


