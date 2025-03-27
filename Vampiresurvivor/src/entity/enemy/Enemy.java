package entity.enemy;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logic.CollisionHandler;
import logic.GameScene;
import logic.enemyMovable;
import map.MapObstacle;
import player.base.Player;

import java.util.List;

import audio.NameSE;
import audio.SE;

public abstract class Enemy implements enemyMovable{
    protected double x;
	protected double y;
    protected double speed;
    protected double health;
    protected double maxHealth;
    protected double damage ;
    protected boolean markedForRemoval = false;
    protected boolean isAttack = false;
    protected boolean isTakingDamage = false;
    protected long damageEffectStartTime = 0;
    protected static final long DAMAGE_EFFECT_DURATION = 200_000_000L; 

  
    //animation set
    protected double deltaTime = 0.01667;
    protected Image[] frames;
    protected int currentFrame = 0;
    protected double animationTimer = 0;
    protected double frameDuration;

    protected Enemy(double x, double y,double health ,double damage,double speed) {
    	
        this.x = x;
        this.y = y;
        setHealth(health);
        setDamage(damage);
        setSpeed(speed);
        
    }

    

    @Override
	public abstract void update(Player player, List<Enemy> enemies) ;

    public abstract void draw(GraphicsContext gc);

    //ผลัก player ไม่ให้เดินทับ enemy
    protected void pushPlayer(Player player, double dx, double dy) {
        double pushAmount = 5;
        
        // Store original player position
        double originalX = player.getX();
        double originalY = player.getY();
        
        // Calculate new position
        double newPlayerX = player.getX() + dx * pushAmount;
        double newPlayerY = player.getY() + dy * pushAmount;
        
        // Apply game boundaries
        newPlayerX = clamp(newPlayerX, GameScene.BORDER_SIZE, GameScene.GAME_WIDTH - GameScene.BORDER_SIZE);
        newPlayerY = clamp(newPlayerY, GameScene.BORDER_SIZE, GameScene.GAME_HEIGHT - GameScene.BORDER_SIZE);
        
        // Check specific collision with object 332 and other map obstacles
        if (isValidMove(newPlayerX, newPlayerY)) {
            player.setX(newPlayerX);
            player.setY(newPlayerY);
            return;
        }
        
        // If we can't move diagonally, try separate X and Y movements
        double newXOnly = player.getX() + dx * pushAmount;
        newXOnly = clamp(newXOnly, GameScene.BORDER_SIZE, GameScene.GAME_WIDTH - GameScene.BORDER_SIZE);
        
        if (isValidMove(newXOnly, player.getY())) {
            player.setX(newXOnly);
        }
        
        double newYOnly = player.getY() + dy * pushAmount;
        newYOnly = clamp(newYOnly, GameScene.BORDER_SIZE, GameScene.GAME_HEIGHT - GameScene.BORDER_SIZE);
        
        if (isValidMove(player.getX(), newYOnly)) {
            player.setY(newYOnly);
        }
    }

    // Helper method to verify if a move is valid, with special handling for object 332
    private boolean isValidMove(double x, double y) {
        // First use the existing collision handler
        if (!CollisionHandler.canMove(x, y, MapObstacle.getMapObstacle())) {
            return false;
        }
        
        // Player dimensions (assuming player is represented as a circle/rectangle)
        double playerWidth = 20; // Adjust based on your player size
        double playerHeight = 20; // Adjust based on your player size
        
        // Check all four corners of the player's bounding box
        double[][] checkPoints = {
            {x - playerWidth/2, y - playerHeight/2}, // Top-left
            {x + playerWidth/2, y - playerHeight/2}, // Top-right
            {x - playerWidth/2, y + playerHeight/2}, // Bottom-left
            {x + playerWidth/2, y + playerHeight/2}  // Bottom-right
        };
        
        // Check the tile type at each corner
        for (double[] point : checkPoints) {
            // Convert world coordinates to tile coordinates
            // Assuming 32x32 tile size - adjust based on your game
            int tileX = (int)(point[0] / 32);
            int tileY = (int)(point[1] / 32);
            
            // Get the map from MapObstacle class
            int[][] map = MapObstacle.getMapObstacle();
            
            // Check if coordinates are within bounds
            if (tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
                // Check specifically for tile type 332 which is unwalkable
                if (map[tileY][tileX] == 332) {
                    return false;
                }
            }
        }
        
        return true;
    }

    //ไว้เช็ค ไม่ให้เดินเกินขอบ
    protected double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void takeDamage(double damage) {
    	
    	setHealth(getHealth()-damage) ;
    	
        if (getHealth() < 0) {
        	setHealth(0);
        }
        
        isTakingDamage = true;
        damageEffectStartTime = System.nanoTime();
        
        SE.playSE(NameSE.SND_STOMP);
    }
    
   //สำหรับให้คลาสอื่นๆเรียกใช้ต่อไป    
    public boolean isDead() {
        return markedForRemoval;
    }
    
  //ใช้กับคลาสตัวเองเท่านั้น 
    protected boolean isDead1() {
        return getHealth() <= 0;
    }
    
    public double getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}

	public Image[] getFrames() {
		return frames;
	}

	public void setFrames(Image[] frames) {
		this.frames = frames;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public double getAnimationTimer() {
		return animationTimer;
	}

	public void setAnimationTimer(double animationTimer) {
		this.animationTimer = animationTimer;
	}

	public double getFrameDuration() {
		return frameDuration;
	}

	public void setFrameDuration(double frameDuration) {
		this.frameDuration = frameDuration;
	}
    
    public double getX() { return x; }
    public double getY() { return y; }

    public boolean shouldBeRemoved() { 
        return markedForRemoval; 
    }

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
    
	public double getHealth() { 
		return health;
	}
    public void setHealth(double health) {
    	this.health = Math.max(0, Math.min(100, health));
    }   
    
	public double getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	
	
    
}
