package player.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import audio.NameSE;
import audio.SE;
import entity.enemy.Enemy;
import entity.projectile.Arrow;
import entity.projectile.BigEnergyBall;
import entity.projectile.FireballProjectile;
import entity.projectile.FlameKaiser;
import entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import logic.EnemyManager;
import logic.Power;

public class RedHoodBoy extends Player {

    private boolean isMove = false;
    private boolean isRight = true;
    private List<Projectile> projectiles = new ArrayList<>();
    private long lastShotTime = System.nanoTime();
    private static final long SHOOT_INTERVAL = 500_000_000L;
    private Power powerType = Power.Fireball;
    private Power ultimateType = Power.Flamekaiser;
    private boolean isUltiCooldown = false;

    // Animation fields
    private Image spriteSheet;
    private Image[] animationFrames;
    private int totalFrames;
    private double frameWidth;
    private double frameHeight;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.2;
    
    // Track which animation is playing
    private String currentAnimation = ""; 

    public RedHoodBoy(double x, double y) {
    	//spawnposition [x,y], maxHP, speed, armor
        super(x, y, 150, 3,20);
        setDamage(30.00);
        setDesc("He is the famous character from a story's tail Redhood's girl wiht the wolf the true story actually the redhoodgirl is BOY!!");
        setName("Red Hood Boy");
        loadSpriteSheet("idle"); 
    }

    @Override
    public void update(Set<KeyCode> keys, int gameWidth, int gameHeight, int borderSize, EnemyManager enemyManager, double deltaTime, long now) {
    	if (!entity.enemy.BigBoss.isTimeStopActive()) {
    		super.update(keys, gameWidth, gameHeight, borderSize);
    		shootProjectiles(enemyManager);
    	}

        // Track movement direction
        boolean wasMoving = isMove;
        boolean wasRight = isRight;
        // for run animation that has different frame with idle
        frameDuration = 0.5;
        
        if(!entity.enemy.BigBoss.isTimeStopActive()) {
        	if (keys.contains(KeyCode.D)) {
                isMove = true;
                isRight = true;
                frameDuration = 0.1;
            } else if (keys.contains(KeyCode.A)) {
                isMove = true;
                isRight = false;
                frameDuration = 0.1;
            } else if(keys.contains(KeyCode.W)||keys.contains(KeyCode.S)){
            	isMove = true;
            	frameDuration = 0.1;
            }else{
                isMove = false;
            }
        }
        //Load new animation only if state change
        if (wasMoving != isMove || wasRight != isRight) {
            loadSpriteSheet(isMove ? "run" : "idle");
        }
        
        if (keys.contains(KeyCode.R)) {
        	
        	releaseUltimate(enemyManager);
        }

        // Projectile update
        projectiles.forEach(p -> p.update(enemyManager.getEnemies(), deltaTime, now));
        projectiles.removeIf(p -> !p.isActive());

        // Animation update
        animationTimer += deltaTime;
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;
            animationTimer -= frameDuration;
        }
    }

    private void shootProjectiles(EnemyManager enemyManager) {
        if (System.nanoTime() - lastShotTime < SHOOT_INTERVAL) return;

        Enemy nearestEnemy = enemyManager.getNearestEnemy(getX(), getY());
        if (nearestEnemy != null) {
            Projectile pt;
            switch (powerType) {
                case Fireball:
                    pt = new FireballProjectile(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
                    break;
                case Energyball:
                    pt = new BigEnergyBall(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
                    break;
                case Arrow:
                    pt = new Arrow(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected power type: " + powerType);
            }
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
            	Projectile ultimate = new FlameKaiser(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
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
            gc.drawImage(frame, 0, 0, drawWidth, drawHeight);
        } else {
            gc.drawImage(frame, getX() - drawWidth / 2, getY() - drawHeight / 2, drawWidth, drawHeight);
        }

        gc.restore();
        super.drawHealthBar(gc);
        projectiles.forEach(p -> p.draw(gc));
    }

    public void loadSpriteSheet(String animationType) {
    	// prevent load duplicate image 
        if (animationType.equals(currentAnimation)) return; 

        currentAnimation = animationType;
        String spritePath = (animationType.equals("run"))
                ? "player/redHoodBoy/redHoodRun.png"
                : "player/redHoodBoy/redHoodIdle.png";

        spriteSheet = new Image(ClassLoader.getSystemResourceAsStream(spritePath));

        totalFrames = (animationType.equals("run")) ? 8 : 2; 
        frameWidth = spriteSheet.getWidth() / totalFrames;
        frameHeight = spriteSheet.getHeight();

        animationFrames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            animationFrames[i] = new WritableImage(spriteSheet.getPixelReader(),
            		(int) (i * frameWidth), 0, (int) frameWidth, (int) frameHeight);
        }

        currentFrame = 0;
    }
    
    @Override
    public Image getIdleFrame() {
        loadSpriteSheet("idle");
        return (animationFrames.length > 0) ? animationFrames[0] : null;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}
