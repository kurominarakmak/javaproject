package logic;


public class Health {
    private double maxHealth;
    private double currentHealth;

    public Health(double maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public double getHealth() {
        return currentHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(double damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }
}
