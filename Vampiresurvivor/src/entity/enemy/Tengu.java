package entity.enemy;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.enemyMovable;
import player.base.Player;

public class Tengu extends Enemy implements enemyMovable{

	public Tengu(double x, double y, double health, double damage, double speed) {
		
		super(x, y, 50, 30, 1);
		
		Image[] movementFrames = new Image[] {
				
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk1.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk2.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk3.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk4.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk5.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk6.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk7.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk8.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk9.png")),
                new Image(ClassLoader.getSystemResourceAsStream("tengu/Walk10.png"))
                
            };
		
	    setFrameDuration(0.3);
		setFrames(movementFrames);
		
	}

	public void update(Player player, List<Enemy> enemies) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        x += dx * speed;
        y += dy * speed;

        if (Math.hypot(player.getX() - x, player.getY() - y) < 30) {
        	
        	//cooldown attack
        	Thread cooldownAttack = new Thread(()->{
        		try {
					Thread.sleep(2000);
					isAttack=false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        	});
        	
        	if (!isAttack) {
        		player.takeDamage(damage);
        		isAttack=true;
        		cooldownAttack.start();
        	}
        	
            
            
        }
        
        

        if (health <= 0) {
            markedForRemoval = true;
        }

        // Animation update
        animationTimer+= deltaTime;
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            animationTimer =0;
        }
    }
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.save();
		if (isTakingDamage && (System.nanoTime() - damageEffectStartTime < DAMAGE_EFFECT_DURATION)) {
		    gc.setGlobalBlendMode(javafx.scene.effect.BlendMode.SCREEN); 
		}
		gc.drawImage(frames[currentFrame], x - 53, y - 33, 53 * 2, 33 * 2);
		gc.restore();
		gc.setGlobalBlendMode(null); // Reset after rendering

	}

	
}
