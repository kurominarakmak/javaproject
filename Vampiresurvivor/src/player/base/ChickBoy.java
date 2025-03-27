package player.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import audio.NameSE;
import audio.SE;
import entity.enemy.Enemy;
import entity.projectile.Arrow;
import entity.projectile.BigEnergyBall;
import entity.projectile.Chicken;
import entity.projectile.FireballProjectile;
import entity.projectile.FlameKaiser;
import entity.projectile.Kaboom;
import entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import logic.EnemyManager;
import logic.Power;

public class ChickBoy extends Player {
	
    private boolean isMove = false;
    private boolean isRight = true;
    private List<Projectile> projectiles = new ArrayList<>();
    private long lastShotTime = System.nanoTime();
    private static final long SHOOT_INTERVAL = 600_000_000L;
    private Power powerType = Power.Chicken;
    private Power ultimate = Power.Kaboom;
    private boolean isUltiCooldown = false;

    // Animation variables
    private Image spriteSheet;
    private Image[] animationFrames;
    private int totalFrames;
    private double frameWidth;
    private double frameHeight;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.1;

    private String currentAnimation = ""; // Track which animation is playing

    public ChickBoy(double x, double y) {
    	//spawnposition [x,y], maxHP, speed, armor
        super(x, y, 35, 3,0);
        setDamage(100.00);
        setDesc("A boy was birth in HOLY Mother Chiken Day he got a power and Ability of Chiken!!!");
        setName("Chick Boy");
        loadSpriteSheet("idle"); // Start with idle animation
    }

    @Override
    public void update(Set<KeyCode> keys, int gameWidth, int gameHeight, int borderSize, EnemyManager enemyManager, double deltaTime, long now) {
    	if (!entity.enemy.BigBoss.isTimeStopActive()) {
    		super.update(keys, gameWidth, gameHeight, borderSize);
    		shootMagic(enemyManager);
    	}
        // Track movement direction
        boolean wasMoving = isMove;
        boolean wasRight = isRight;

        if(!entity.enemy.BigBoss.isTimeStopActive()) {
        	if (keys.contains(KeyCode.D)) {
                isMove = true;
                isRight = true;
            } else if (keys.contains(KeyCode.A)) {
                isMove = true;
                isRight = false;
            } else if(keys.contains(KeyCode.W)||keys.contains(KeyCode.S)){
            	 isMove = true;
           
            }else{
                isMove = false;
            }
        }
        
        
        if (keys.contains(KeyCode.R)) {
        	
        	releaseUltimate(enemyManager);
        }
        
        //Load new animation only if state changed
        if (wasMoving != isMove || wasRight != isRight) {
            loadSpriteSheet(isMove ? "run" : "idle");
        }

        // Projectile updates
        projectiles.forEach(p -> p.update(enemyManager.getEnemies(), deltaTime, now));
        projectiles.removeIf(p -> !p.isActive());

        // Animation update
        animationTimer += deltaTime;
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;
            animationTimer -= frameDuration;
        }
    }

    private void shootMagic(EnemyManager enemyManager) {
        if (System.nanoTime() - lastShotTime < SHOOT_INTERVAL) return;

        Enemy nearestEnemy = enemyManager.getNearestEnemy(getX(), getY());
        if (nearestEnemy != null) {
            Projectile pt = new Chicken(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
            projectiles.add(pt);
            lastShotTime = System.nanoTime();
        }
    }
    
    private void releaseUltimate(EnemyManager enemyManager) {
    	Thread ultimateCooldown = new Thread(() -> {
    		try {
    			isUltiCooldown = true;
    			SE.playSE(NameSE.SND_BIGBOMBEXPLOSION);
				Thread.sleep(5000);
				isUltiCooldown = false;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	});

        Enemy nearestEnemy = enemyManager.getNearestEnemy(getX(), getY());
        
            if (!isUltiCooldown && nearestEnemy != null) {
            	Projectile ultimate = new Kaboom(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
            	projectiles.add(ultimate);
            	ultimateCooldown.start();
            	
            }else {
            	return;
            }
            	
            
        
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        Image frame = animationFrames[currentFrame];
        double drawWidth = frameWidth * 2;
        double drawHeight = frameHeight * 2;

        if (!isRight) {
            gc.translate(getX() + drawWidth / 2, getY() - drawHeight / 2);
            gc.scale(-1, 1);
            gc.drawImage(frame, 0, 0, drawWidth-10, drawHeight-10);
        } else {
            gc.drawImage(frame, getX() - drawWidth / 2, getY() - drawHeight / 2, drawWidth-10, drawHeight-10);
        }

        gc.restore();
        super.drawHealthBar(gc);
        projectiles.forEach(p -> p.draw(gc));
    }

    public void loadSpriteSheet(String animationType) {
        if (animationType.equals(currentAnimation)) return; 

        currentAnimation = animationType;
        String spritePath = (animationType.equals("run"))
                ? "player/chickboy/ChikBoyRun.png"
                : "player/chickboy/ChikBoyIdle.png";

        spriteSheet = new Image(ClassLoader.getSystemResourceAsStream(spritePath));

        totalFrames = (animationType.equals("run")) ? 10 : 6; 
        frameWidth = spriteSheet.getWidth();
        frameHeight = spriteSheet.getHeight() / totalFrames;

        animationFrames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            animationFrames[i] = new WritableImage(spriteSheet.getPixelReader(),
                    0, (int) (i * frameHeight), (int) frameWidth, (int) frameHeight);
        }

        currentFrame = 0;
    }
    
    @Override
    public Image getIdleFrame() {
        loadSpriteSheet("idle"); // โหลด Sprite Idle
        return (animationFrames.length > 0) ? animationFrames[0] : null;
    }
    

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}
