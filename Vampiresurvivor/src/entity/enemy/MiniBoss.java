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

public class MiniBoss extends Enemy implements enemyMovable {
    private static final double DEFAULT_HEALTH = 300;
    private static final double DEFAULT_DAMAGE = 50;
    private static final double DEFAULT_SPEED = 0.95;
    private static final double TREASURE_DROP_CHANCE = 0.3; // 30% chance to drop
    
    private boolean isAttack = false;
    
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
    
    public MiniBoss(double x, double y) {
        super(x, y, DEFAULT_HEALTH, DEFAULT_DAMAGE, DEFAULT_SPEED);
        loadSpriteSheet("idle");
    }

    private void loadSpriteSheet(String animationType) {
        if (animationType.equals(currentAnimation)) return; // No need to reload the same animation

        currentAnimation = animationType;
        String spritePath = "";

        switch (animationType) {
            case "idle":
                spritePath = "boss/Idle.png";
                totalFrames = 6; // Number of frames in idle animation
                break;
            case "walk":
                spritePath = "boss/Walk.png";
                totalFrames = 12; // Number of frames in walk animation
                break;
            case "attack":
                spritePath = "boss/Attack.png";
                totalFrames = 5; // Number of frames in attack animation
                break;
            default:
                spritePath = "boss/Idle.png";
                totalFrames = 6; // Number of frames in idle animation
        }

        spriteSheet = new Image(ClassLoader.getSystemResourceAsStream(spritePath));

        // Divide sprite sheet into individual frames
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
        // Skip updates during time stop (if BigBoss time stop feature is used)
        if (BigBoss.isTimeStopActive()) {
            return;
        }
        
        // Calculate direction to player
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }
        
        // Move towards player
        x += dx * speed;
        y += dy * speed;
        
        // Prevent going out of bounds
        x = clamp(x, GameScene.BORDER_SIZE, GameScene.GAME_WIDTH - GameScene.BORDER_SIZE);
        y = clamp(y, GameScene.BORDER_SIZE, GameScene.GAME_HEIGHT - GameScene.BORDER_SIZE);
        
        // Check for collision with player
        if (Math.hypot(player.getX() - x, player.getY() - y) < 40) {
            // Attack cooldown system
            if (!isAttack) {
                player.takeDamage(DEFAULT_DAMAGE);
                isAttack = true;
                
                // Cooldown thread
                Thread cooldownAttack = new Thread(() -> {
                    try {
                        Thread.sleep(1500); // 1.5 second cooldown
                        isAttack = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                cooldownAttack.start();
            }
        }
        
        // Check if mini boss is dead
        if (isDead()) {
            dropTreasure();
            markedForRemoval = true;
        }
        
        // Animation update
        animationTimer += deltaTime;
        if (animationTimer >= frameDuration) {
            animationTimer = 0; // Reset timer

            if (isAttack) {
                if (!currentAnimation.equals("attack")) loadSpriteSheet("attack");
            } else if (dx != 0 || dy != 0) {
                if (!currentAnimation.equals("walk")) loadSpriteSheet("walk");
            } else {
                if (!currentAnimation.equals("idle")) loadSpriteSheet("idle");
            }

            currentFrame = (currentFrame + 1) % totalFrames; // Cycle through frames
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Draw the mini boss image using current sprite sheet frame
    	gc.save();
		if (isTakingDamage && (System.nanoTime() - damageEffectStartTime < DAMAGE_EFFECT_DURATION)) {
		    gc.setGlobalBlendMode(javafx.scene.effect.BlendMode.SCREEN); 
		}
		
        Image currentImage = animationFrames[currentFrame];
        
        // Draw sprite with scaling
        double drawWidth = frameWidth * 2;
        double drawHeight = frameHeight * 2;
        
        gc.drawImage(currentImage, x - drawWidth / 2, y - drawHeight / 2, drawWidth, drawHeight);
        gc.restore();
		gc.setGlobalBlendMode(null); // Reset after rendering
        // Visual indicator for attack cooldown
        if (isAttack) {
            gc.setStroke(Color.ORANGE);
            gc.strokeOval(x - 35, y - 35, 70, 70);
        }
        
        
    }

    private void dropTreasure() {
        Random rand = new Random();
        if (rand.nextDouble() < TREASURE_DROP_CHANCE) {
            System.out.println("MiniBoss dropped a treasure!");
            // For actual implementation, you'd create and add a treasure object here
        }
    }

    // Check wether the miniboss is dead or not
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    // Clamp method to keep the enemy within bounds
    protected double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
