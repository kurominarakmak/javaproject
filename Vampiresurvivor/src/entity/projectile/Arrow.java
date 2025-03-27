package entity.projectile;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Arrow extends Projectile{
	Image arrowImg = new Image(ClassLoader.getSystemResourceAsStream("projectiles/arrow/Arrow.png"));
	private double angle;
	public Arrow(double x, double y, double targetX, double targetY, long now) {
		super(x, y, targetX, targetY, now);
		setDAMAGE(15);
		setLIFETIME(1_000_000_000L);
		setSpeed(1);
		
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
        
    }
	
	@Override
	public void draw(GraphicsContext gc) {
        if (isActive) {
        	
        	gc.save(); 
            gc.translate(x, y);
            gc.rotate(angle);
            Image currentImage = arrowImg;
            gc.drawImage(currentImage, -currentImage.getWidth() / 2, -currentImage.getHeight() / 2,13*2,5*2);
            gc.restore(); 
        }
    }
	
	

}
