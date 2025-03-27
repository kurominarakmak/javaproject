package entity.projectile;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BigEnergyBall extends Projectile{
	private double deltaTimeFix = 0.01667;
	private int totalFrames = 4;
    private Image[] frames;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.2;
    private double angle;
    private double frameWidth;
    private double frameHeight;
    
    private double stopX; 
    private double stopY; 
    private boolean hasStopped = false; 

	public BigEnergyBall(double x, double y, double targetX, double targetY, long now) {
		super(x, y, targetX, targetY, now);
		setDAMAGE(40);
		setLIFETIME(2_000_000_000L);
		setSpeed(2.25);
		
		 // Load frames dynamically
        frames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            String path = String.format("projectiles/bigenergyball/BigEnergyBall%01d.png", i);
            frames[i] = new Image(ClassLoader.getSystemResourceAsStream(path));
        }

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
                isActive = false;
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
	public void draw(GraphicsContext gc) {
        if (isActive) {
        	
        	gc.save(); 
            gc.translate(x, y);
            gc.rotate(angle);
            Image currentImage = frames[currentFrame];
            gc.drawImage(currentImage, -currentImage.getWidth() / 2, -currentImage.getHeight() / 2,40,40);
            gc.restore(); 
        }
    }
	
	

}


