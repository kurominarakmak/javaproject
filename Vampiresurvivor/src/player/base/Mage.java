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
import entity.projectile.Vortex;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic.EnemyManager;
import logic.Power;

public class Mage extends Player {
	//player ตัวนี้ มีความสามารถยิงเวท *เดี๋ยวค่อยทำเป็น interface ในอนาคต
	private boolean isMove = false;
    private boolean isRight = true;
	private List<Projectile> projectiles = new ArrayList<>(); //เก็บเม็ดกระสุนที่ยิงไป
	private long lastShotTime = System.nanoTime();
	private static final long SHOOT_INTERVAL = 700_000_000L; //ยิงกระสุนทุกๆ x วิ
	private Power powerType = Power.Energyball;
	private Power ultimateType = Power.Vortex;
    private boolean isUltiCooldown = false;
	
	//animation
	private Image spriteSheet;
    private Image[] animationFrames;
    private int totalFrames;
    private double frameWidth;
    private double frameHeight;
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.1;
    private String currentAnimation = "";
	
	

	
	public Mage(double x, double y) {
		//spawnposition [x,y], maxHP, speed, armor
		super(x, y,120,1.5,15);
        setDesc("A girl has fall in to magic space hole while fighting with Darklord in another WORLD!!!");
        setDamage(40.00);
		setName("Mage");
		loadSpriteSheet("idle");
		
		
		
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

        Enemy nearestEnemy = enemyManager.getNearestEnemy(getX(),getY()); //หาว่าตัวไหนใกล้สุด
        if (nearestEnemy != null) {
        	//ถ้ามีตัวใกล้ ก็จะยิง และเพิ่มกระสุนเข้าลิสต์
        	Projectile pt ;
        	switch (powerType) {
			case Fireball  : {
				pt = new FireballProjectile(getX(),getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
				break;
			}
			case Energyball :{
				pt = new BigEnergyBall(getX(),getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
				break;
				
			}
			case Arrow :{
				pt = new Arrow(getX(),getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
				break;
				
			}
			default:
				throw new IllegalArgumentException("Unexpected value:xx");
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
            	Projectile ultimate = new Vortex(getX(), getY(), nearestEnemy.getX(), nearestEnemy.getY(), System.nanoTime());
            	projectiles.add(ultimate);
            	ultimateCooldown.start();
            	
            }else {
            	return;
            }
            	
            
        
    }
	
	public void draw(GraphicsContext gc) {
		gc.save();

        Image frame = animationFrames[currentFrame];
        double drawWidth = frameWidth * 2;
        double drawHeight = frameHeight * 2;

        if (!isRight) {
            gc.translate(getX()+20 , getY()-30);
            gc.scale(-1, 1);
            gc.drawImage(frame, 0, 0, drawWidth-20, drawHeight-20);
        } else if (isRight){
            gc.drawImage(frame, (getX() - drawWidth / 2)+20, (getY() - drawHeight / 2)+10 , drawWidth-20, drawHeight-20);
        }

        gc.restore();
        super.drawHealthBar(gc);
        projectiles.forEach(p -> p.draw(gc));
	}

		
	
	
	public void loadSpriteSheet(String animationType) {
        if (animationType.equals(currentAnimation)) return; 

        currentAnimation = animationType;
        String spritePath = (animationType.equals("idle"))
                ? "player/mageGirl/mageIdle.png"
                : "player/mageGirl/mageRunRight.png";

        spriteSheet = new Image(ClassLoader.getSystemResourceAsStream(spritePath));

        totalFrames = (animationType.equals("run")) ? 8 : 8; 
        frameWidth = spriteSheet.getWidth()/totalFrames;
        frameHeight = spriteSheet.getHeight();

        animationFrames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            animationFrames[i] = new WritableImage(spriteSheet.getPixelReader(),
            		(int) (i * frameWidth), 0, (int) frameWidth, (int) frameHeight);
        }
//        Reset frame index when switching animation
//        currentFrame = 0; 
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
