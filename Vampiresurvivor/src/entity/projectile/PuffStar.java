package entity.projectile;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PuffStar extends Projectile{
	private double deltaTimeFix = 0.01667;
	private static int totalFrames = 60;
    private static Image[] frames ;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.06;
    private double angle;
    private double frameWidth;
    private double frameHeight;
    
    private double stopX; 
    private double stopY; 
    private boolean hasStopped = false; 
    
    static {
    	frames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            String path = String.format("projectiles/puff/Effect_PuffAndStars_1_%03d.png", i);
            frames[i] = new Image(ClassLoader.getSystemResourceAsStream(path));
        }
    }

	public PuffStar(double x, double y, double targetX, double targetY, long now) {
		super(x, y, targetX, targetY, now);
		setDAMAGE(100);
		setLIFETIME(1_000_000_000L);
		setSpeed(1.2);
		
		 // Load frames dynamically
        

        // Get frame size from first image
        frameWidth = frames[0].getWidth();
        frameHeight = frames[0].getHeight();

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
        return Math.hypot(enemy.getX() - x, enemy.getY() - y) < 20;
    }
	
	@Override
	public void draw(GraphicsContext gc) {
        if (isActive) {
        	
        	gc.save(); 
            gc.translate(x, y);
            gc.rotate(angle);
            Image currentImage = frames[currentFrame];
            gc.drawImage(currentImage, -currentImage.getWidth() / 2, -currentImage.getHeight() / 2,frameWidth+20,frameHeight+20);
            gc.restore(); 
        }
    }
	
	

}


