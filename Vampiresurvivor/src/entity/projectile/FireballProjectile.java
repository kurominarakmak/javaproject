package entity.projectile;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class FireballProjectile extends Projectile {
	
//	private double deltaTime1 = 0.01667;
    private Image[] frames;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.6;
    private double angle;
    
	public FireballProjectile(double x, double y, double targetX, double targetY, long now) {
		super(x, y, targetX, targetY, now);
		setSpeed(2);
		setLIFETIME(3_000_000_000L);
		setDAMAGE(30);
		frames = new Image[] {
                new Image(ClassLoader.getSystemResourceAsStream("projectiles/fireball/Fireball1.png")),
                new Image(ClassLoader.getSystemResourceAsStream("projectiles/fireball/Fireball2.png")),
                new Image(ClassLoader.getSystemResourceAsStream("projectiles/fireball/Fireball3.png")),
                new Image(ClassLoader.getSystemResourceAsStream("projectiles/fireball/Fireball4.png"))
            };
		
		//calculate angle
				double dx = targetX - x;
		        double dy = targetY - y;
		        angle = Math.toDegrees(Math.atan2(dy, dx));
		
	}
	
	public void update(List<Enemy> enemies, double deltaTime, long now) {
        if (!isActive) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        x += dx * speed;
        y += dy * speed;
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
            gc.drawImage(currentImage, -currentImage.getWidth() / 2, -currentImage.getHeight() / 2,20,20);
            gc.restore(); 
        }
    }
	
	

}
