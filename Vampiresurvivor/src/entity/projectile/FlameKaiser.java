package entity.projectile;

import java.util.Iterator;
import java.util.List;
import entity.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class FlameKaiser extends Projectile {
    private double deltaTimeFix = 0.01667;
    private Image[] frames;
    private int totalFrames = 60; // Adjust based on the number of PNG frames
    private int currentFrame = 0;
    private double animationTimer = 0;
    private double frameDuration = 0.05; // Adjust for smooth animation
    private double angle;
    private double frameWidth;
    private double frameHeight;

    private double stopX; 
    private double stopY; 
    private boolean hasStopped = false; 

    public FlameKaiser(double x, double y, double targetX, double targetY, long now) {
        super(x, y, targetX, targetY, now);
        setDAMAGE(100);
        setLIFETIME(3_000_000_000L);
        setSpeed(3);

        // Load frames dynamically
        frames = new Image[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            String path = String.format("projectiles/magma/Effect_Magma_1_%03d.png", i);
            frames[i] = new Image(ClassLoader.getSystemResourceAsStream(path));
        }

        // Get frame size from first image
        frameWidth = frames[0].getWidth();
        frameHeight = frames[0].getHeight();

        // Calculate angle
        double dx = targetX - x;
        double dy = targetY - y;
        angle = Math.toDegrees(Math.atan2(dy, dx)) + 90;

        // **Calculate stopping position (300 pixels past the target)**
        double length = Math.sqrt(dx * dx + dy * dy);
        stopX = targetX + (dx / length) * 300; // Move 300 pixels past
        stopY = targetY + (dy / length) * 300;
    }
    
    @Override
	public boolean checkCollision(Enemy enemy) {
        return Math.hypot(enemy.getX() - x, enemy.getY() - y) < 50;
    }

    public void update(List<Enemy> enemies, double deltaTime, long now) {
        if (!isActive) return;

        if (!hasStopped) { // Only move if it hasn't reached the stopping point
            double dx = stopX - x;
            double dy = stopY - y;
            double length = Math.sqrt(dx * dx + dy * dy);

            if (length > 3) { // Stop if within a small distance
                dx /= length;
                dy /= length;
                x += dx * speed;
                y += dy * speed;
            } else {
                hasStopped = true; // Mark projectile as stopped
            }
        }

        // Check collision
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (checkCollision(enemy)) {
                enemy.takeDamage(DAMAGE);
                if (enemy.isDead()) {
                    iterator.remove();
                }
                break;
            }
        }

        // Deactivate if lifetime expired
        if (now - spawnTime > LIFETIME) {
            isActive = false;
        }

        // Update animation (keep playing even after stopping)
        animationTimer += deltaTime;
        if (animationTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;
            animationTimer = 0;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (isActive) {
            gc.save();
            gc.translate(x, y);
            gc.rotate(angle);
            Image currentImage = frames[currentFrame];

            // Center the projectile
            gc.drawImage(currentImage, -50, -15, 106, 67);

            gc.restore();
        }
    }
}
