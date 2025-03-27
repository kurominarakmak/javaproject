package entity.projectile;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.List;

import entity.enemy.Enemy;



public abstract class Projectile {
	protected double x, y;
    protected double speed = 2;
    protected double targetX, targetY;
	protected boolean isActive = true;
    protected double DAMAGE;
    protected long LIFETIME;
	protected long spawnTime; 

    protected Projectile(double x, double y, double targetX, double targetY, long now) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.spawnTime = now; 
    }

    public abstract void update(List<Enemy> enemies,double deltaTime, long now);

    protected boolean checkCollision(Enemy enemy) {
        return Math.hypot(enemy.getX() - x, enemy.getY() - y) < 20;
    }

    public void draw(GraphicsContext gc) {
        if (isActive) {
            gc.setFill(Color.PURPLE);
            gc.fillOval(x - 5, y - 5, 10, 10);
        }
    }
    
    public double getDAMAGE() {
		return DAMAGE;
	}

	public void setDAMAGE(double dAMAGE) {
		DAMAGE = dAMAGE;
	}

	public long getLIFETIME() {
		return LIFETIME;
	}

	public void setLIFETIME(long lIFETIME) {
		LIFETIME = lIFETIME;
	}

    public boolean isActive() {
        return isActive;
    }
    public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public long getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}

	public double getDamage() {
		return DAMAGE;
	}

	public long getLifetime() {
		return LIFETIME;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
