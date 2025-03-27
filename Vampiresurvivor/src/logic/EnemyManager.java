package logic;

import javafx.scene.canvas.GraphicsContext;
import player.base.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import entity.enemy.BigBoss;
import entity.enemy.Enemy;
import entity.enemy.MiniBoss;
import entity.enemy.Tengu;
import item.base.ExpOrb;

public class EnemyManager {
    private List<Enemy> enemies = new ArrayList<>();
    private List<ExpOrb> expOrbs = new ArrayList<>();
    private static final long SPAWN_INTERVAL = 5_000_000_000L; // 5 seconds between basic spawns
    private static final int SPAWN_DISTANCE = 800;
    private static final int ENEMY_LIMIT = 100;
    private long gameStartTime = 0;  // When the game started
    private long lastLevel1Time = 0;
    private static final long LEVEL_2_INTERVAL = 10_000_000_000L; // 20 seconds
    private static final long LEVEL_3_INTERVAL = 15_000_000_000L; // 40 seconds
    private static final long LEVEL_4_INTERVAL = 20_000_000_000L; // 60 seconds
    private Random random = new Random();
    private GameScene gameScene;
    private int waveNumber = 1;
    private boolean level2Spawned = false;
    private boolean level3Spawned = false;
    private boolean level4Spawned = false;
    public EnemyManager(GameScene gameScene) {
        this.gameScene = gameScene;
        this.gameStartTime = System.nanoTime();
        this.lastLevel1Time = this.gameStartTime;
    }

    public void update(long now, Player player, double deltaTime) {
        long gameElapsedTime = now - gameStartTime;  // Time since game started
        
//        System.out.println("Game Elapsed Time: " + gameElapsedTime / 1_000_000_000 + " seconds");
//        System.out.println("Enemy Count: " + enemies.size());

        // Level 1 enemies - only spawn at regular intervals if not in the middle of a level wave
        long timeSinceLastLevel1 = now - lastLevel1Time;
        if (timeSinceLastLevel1 >= SPAWN_INTERVAL && enemies.size() < ENEMY_LIMIT) {
            System.out.println("Spawning regular Level 1 enemies");
            spawnLevel1Enemies(player);
            lastLevel1Time = now;  // Update last spawn time
        }

        // Level 2 wave at 20 seconds - exactly 40 enemies in total
        if (gameElapsedTime >= LEVEL_2_INTERVAL && !level2Spawned && enemies.size() < ENEMY_LIMIT) {
            System.out.println("Spawning Level 2 wave");
            // Clear existing enemies first to ensure we have exactly 40
            spawnLevel2Enemies(player);
            level2Spawned = true;
        }

        // Level 3 wave at 40 seconds
        if (gameElapsedTime >= LEVEL_3_INTERVAL && !level3Spawned && enemies.size() < ENEMY_LIMIT) {
            System.out.println("Spawning Level 3 wave");
            spawnLevel3Enemies(player);
            level3Spawned = true;
        }

        // Level 4 wave at 60 seconds
        if (gameElapsedTime >= LEVEL_4_INTERVAL && !level4Spawned && enemies.size() < ENEMY_LIMIT) {
            System.out.println("Spawning Level 4 wave");
            spawnLevel4Enemies(player);
            level4Spawned = true;
        }

        // Reset wave cycle after a full minute
        if (gameElapsedTime >= LEVEL_4_INTERVAL + 5_000_000_000L) { // Add 5 seconds buffer
            gameStartTime = now;
            lastLevel1Time = now;
            level2Spawned = false;
            level3Spawned = false;
            level4Spawned = false;
            waveNumber++;
            System.out.println("Starting new wave: " + waveNumber);
        }

        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.update(player, enemies);
        }

        // Check for enemy death and drop ExpOrbs
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (enemy.shouldBeRemoved()) {
                System.out.println("Enemy Died -> Dropping ExpOrb at: " + enemy.getX() + ", " + enemy.getY());
                expOrbs.add(new ExpOrb(enemy.getX(), enemy.getY(), gameScene));
                iterator.remove();
            }
        }

        // Update ExpOrbs
        Iterator<ExpOrb> orbIterator = expOrbs.iterator();
        while (orbIterator.hasNext()) {
            ExpOrb orb = orbIterator.next();
            orb.update(player);
            if (orb.isCollected()) {
                orbIterator.remove();
            }
        }
    }

    // Spawns enemies for Level 1 (Every 5 seconds)
    private void spawnLevel1Enemies(Player player) {
        System.out.println("Creating 10 Level 1 enemies");
        for (int i = 0; i < 10; i++) { // 10 enemies from both sides
            boolean spawnLeft = random.nextBoolean();
            double x = player.getX() + (spawnLeft ? -SPAWN_DISTANCE : SPAWN_DISTANCE);
            double y = player.getY() + random.nextInt(600) - 300;
            enemies.add(new Tengu(x, y, 50, 5, 1)); // Basic enemies
        }
    }

    // Spawns exactly 40 enemies for Level 2 (at 20 seconds)
    private void spawnLevel2Enemies(Player player) {
        System.out.println("Creating exactly 40 enemies for Level 2");
        
        // Spawn exactly 40 enemies for level 2
        for (int i = 0; i < 40; i++) {
            boolean spawnLeft = random.nextBoolean();
            double x = player.getX() + (spawnLeft ? -SPAWN_DISTANCE : SPAWN_DISTANCE);
            double y = player.getY() + random.nextInt(600) - 300;
            enemies.add(new Tengu(x, y, 50, 5, 1)); // Basic enemies
        }
        
        System.out.println("Total enemies after Level 2 spawn: " + enemies.size());
    }

    // Spawns enemies for Level 3 (At 40 seconds) + MiniBoss
    private void spawnLevel3Enemies(Player player) {
        System.out.println("Creating MiniBoss for Level 3");
        
        // Add MiniBoss
        double x = player.getX() + random.nextInt(600) - 300;
        double y = player.getY() + random.nextInt(600) - 300;

        MiniBoss miniBoss = new MiniBoss(x, y);
        enemies.add(miniBoss); // Add MiniBoss to the enemies list
        
        System.out.println("Total enemies after Level 3 spawn: " + enemies.size());
    }

    // Spawns enemies for Level 4 (At 60 seconds) + BigBoss
    private void spawnLevel4Enemies(Player player) {
        System.out.println("Creating BigBoss for Level 4");
        
        // Add BigBoss
        double x = player.getX() + random.nextInt(600) - 300;
        double y = player.getY() + random.nextInt(600) - 300;
        
        enemies.add(new BigBoss(x,y,1000,100,0.8)); // Add BigBoss to the enemies list
        
        System.out.println("Total enemies after Level 4 spawn: " + enemies.size());
    }

    public Enemy getNearestEnemy(double px, double py) {
        Enemy nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            double dx = enemy.getX() - px;
            double dy = enemy.getY() - py;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }
        return nearest;
    }

    public void draw(GraphicsContext gc) {
        for (Enemy enemy : enemies) {
            enemy.draw(gc);
        }
        for (ExpOrb orb : expOrbs) {
            orb.draw(gc);
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}