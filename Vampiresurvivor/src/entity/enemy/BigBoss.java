package entity.enemy;

import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logic.GameScene;
import logic.enemyMovable;
import player.base.Player;

public class BigBoss extends Enemy implements enemyMovable {
    private static final double DEFAULT_HEALTH = 10000; // Very high health
    private static final double DEFAULT_DAMAGE = 100; // High damage
    private static final double DEFAULT_SPEED = 0.95; // Slower than MiniBoss
    private static final double SPECIAL_ATTACK_CHANCE = 0.1; // 10% chance to deal 1 million damage
    private static final double STOP_TIME_CHANCE = 0.11; // 11% chance to stop time
    private static final int TREASURE_VALUE = 1000; // Value of treasure dropped

    private static boolean timeStopActive = false;
    private static int timeStopDuration = 0;
    private static final int TIME_STOP_DURATION = 180; // Stop time for 3 second
    private boolean isMoving = false;
    private boolean isAttack = false;
    private boolean isCooldownActive = false; // Add cooldown flag for Time Stop

    private boolean isAttacking = false;
    private int attackAnimationFrame = 0;
    private static final int ATTACK_ANIMATION_DURATION = 5; // Number of frames in attack animation
    private Image[] attackFrames;
    private Image bookImage;
    
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
    
    public BigBoss(double x, double y, double health, double damage, double speed) {
        super(x, y, DEFAULT_HEALTH, DEFAULT_DAMAGE, DEFAULT_SPEED);
        loadSpriteSheet("idle");
        loadBookImage();
        stopTime();
    }
    
    private void loadBookImage() {
        bookImage = new Image(ClassLoader.getSystemResourceAsStream("bigBoss/SpriteInHand.png"));
    }
    
    private void loadSpriteSheet(String animationType) {
        if (animationType.equals(currentAnimation)) return; 

        currentAnimation = animationType;
        String spritePath = "";
        
        switch (animationType) {
            case "idle":
                spritePath = "bigBoss/BigBossIdle.png";
                totalFrames = 6; //Number of frame in idle animation
                break;
            case "walk":
                spritePath = "bigBoss/BigBossWalk.png";
                totalFrames = 8; //Number of frame in walk animation
                break;
            case "attack":
                spritePath = "bigBoss/BigBossAttack.png";
                totalFrames = 4; //Number of frame in attack animation
                break;
            default:
                spritePath = "bigBoss/BigBossIdle.png";
                totalFrames = 6; //Number of frame in idle animation
        }

        spriteSheet = new Image(ClassLoader.getSystemResourceAsStream(spritePath));

        // Use the entire width of the sprite sheet, not just a single frame
        frameWidth = spriteSheet.getWidth() / totalFrames;
        frameHeight = spriteSheet.getHeight();

        animationFrames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            animationFrames[i] = new WritableImage(spriteSheet.getPixelReader(),
                    (int)(i * frameWidth), 0, (int)frameWidth, (int)frameHeight);
        }

        currentFrame = 0; // Reset frame index when switching animation
    }


    @Override
    public void update(Player player, List<Enemy> enemies) {
        // Update time stop effect if active
        if (timeStopActive) {
            timeStopDuration--;
            if (timeStopDuration <= 0) {
                timeStopActive = false;
                System.out.println("Time resumes flowing normally!");
            }
        }
        Thread cooldownAttack = new Thread(()->{
    		try {
				Thread.sleep(2000);
				isAttack=false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	});
        
        // Only the BigBoss moves during time stop
        if (!timeStopActive || this instanceof BigBoss) {
            // Calculate direction to player
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double length = Math.sqrt(dx * dx + dy * dy);
            
            isMoving = false;

            if (length > 0) { // Avoids division by zero
                dx /= length;
                dy /= length;
            } 
            
            x += dx * speed;
            y += dy * speed;

            // Check for collision with player
            if (Math.hypot(player.getX() - x, player.getY() - y) < 30) { // Bigger collision radius for boss
                // Attack cooldown system
                if (!isAttack) {
                    // Trigger attack animation
                    startAttackAnimation(player);
                    cooldownAttack.start();
                }
            }
            
            // Check if boss is dead
            if (health <= 0) {
            	BigBoss.resetTimeStop();
                dropTreasure();
                markedForRemoval = true;
            }
        }
        
        // Animation update
        animationTimer += deltaTime;

        if (animationTimer >= frameDuration) {
            animationTimer = 0; // Reset timer

            if (isAttacking) {
                if (!currentAnimation.equals("attack")) loadSpriteSheet("attack");
            } else if (isMoving) {
                if (!currentAnimation.equals("walk")) loadSpriteSheet("walk");
            } else {
                if (!currentAnimation.equals("idle")) loadSpriteSheet("idle");
            }

            currentFrame = (currentFrame + 1) % totalFrames; // Cycle through frames
        }
    }

    private void startAttackAnimation(Player player) {
        isAttacking = true;
        isAttack = true;
        attackAnimationFrame = 0;
        
        // Set attack frames
        setFrames(attackFrames);
        currentFrame = 0;
        
        // Apply damage during attack
        player.takeDamage(damage);
        
        // Perform special attack
        performSpecialAttack(player);
        
        // Cooldown thread
        Thread cooldownAttack = new Thread(() -> {
            try {
                Thread.sleep(3000); // Longer cooldown for boss (3 seconds)
                isAttack = false;
                isAttacking = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        cooldownAttack.start();
    }

 

    @Override
    public void draw(GraphicsContext gc) {
    	
        // Draw the boss image using current sprite sheet frame
        Image currentImage = animationFrames[currentFrame];
        // Draw sprite with scaling
        double drawWidth = frameWidth * 2;
        double drawHeight = frameHeight * 2;
        gc.save();
        if (isTakingDamage && (System.nanoTime() - damageEffectStartTime < DAMAGE_EFFECT_DURATION)) {
		    gc.setGlobalBlendMode(javafx.scene.effect.BlendMode.SCREEN); 
		}
        gc.drawImage(currentImage, x - drawWidth / 2, y - drawHeight / 2, drawWidth, drawHeight);
        
        gc.restore();
		gc.setGlobalBlendMode(null);
		
        // Draw time stop effect if active
        if (timeStopActive) {
            gc.setGlobalAlpha(0.3);
            gc.setFill(Color.CYAN);
            gc.fillRect(0, 0, GameScene.GAME_WIDTH, GameScene.GAME_HEIGHT);
            gc.setGlobalAlpha(1.0);
            
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(3);
            gc.strokeOval(x - 100, y - 70, 200, 140);
            
            gc.setFill(Color.YELLOW);
            gc.fillText("TIME STOPPED", x - 50, y - 80);
            
            double bookX = x - bookImage.getWidth() * 3 / 2; // Center the book image above the BigBoss
            double bookY = y - bookImage.getHeight() * 3 - 20; // Adjust Y to position above the head
            gc.drawImage(bookImage, bookX, bookY);
            
        }
    }
    private void performSpecialAttack(Player player) {
        Random rand = new Random();

        // 10% chance to deal 1 million damage
        if (rand.nextDouble() < SPECIAL_ATTACK_CHANCE) {
            System.out.println("BigBoss performs a special attack! 1 million damage!");
            player.takeDamage(1_000_000);
        }

        // 20% chance to stop time (only if not already stopped)
        if (rand.nextDouble() < STOP_TIME_CHANCE && !timeStopActive && !isCooldownActive) {
            System.out.println("BigBoss stops time!");
            stopTime();
        }
    }

    private void stopTime() {
        timeStopActive = true;
        timeStopDuration = TIME_STOP_DURATION;
        isCooldownActive = true;
        Thread cooldownThread = new Thread(() -> {
            try {
                Thread.sleep(10000); // 10 seconds cooldown before the ability can be used again
                isCooldownActive = false;
                System.out.println("Time Stop is ready again!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        cooldownThread.start();
        
        Thread timeStopThread = new Thread(() -> {
            try {
                Thread.sleep(3000); // 10 seconds duration for time stop
                timeStopActive = false; // Reset time stop
                System.out.println("Time resumes flowing normally!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timeStopThread.start();

        System.out.println("Time stopped! Everything freezes except the BigBoss!");
    }

    // Static method to check if time is currently stopped
    public static boolean isTimeStopActive() {
        return timeStopActive;
    }
    
    protected double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    // Static method to get remaining time stop duration
    public static int getTimeStopDuration() {
        return timeStopDuration;
    }
    
    public static void resetTimeStop() {
        timeStopActive = false;
        timeStopDuration = 0;
    }

    private void dropTreasure() {
        Random rand = new Random();
        if (rand.nextDouble() < 0.5) { // 50% chance to drop treasure
            System.out.println("BigBoss dropped a treasure worth " + TREASURE_VALUE + " points!");
        }
    }
}