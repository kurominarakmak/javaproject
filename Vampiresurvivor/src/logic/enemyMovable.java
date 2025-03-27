package logic;

import java.util.List;

import entity.enemy.Enemy;
import player.base.Player;

public interface enemyMovable {
    // For Enemy: Updating position based on game logic (e.g., moving towards player)
    void update(Player player, List<Enemy> enemies);
}