package item.base;

public abstract class Weapon extends Item {
	private final Double MIN_DAMAGE = 1.00;
	private final Double MIN_COOLDOWN = 1.00;
	private Double damage;
	private Double cooldownTime;
	private boolean isCooldown;
	
	protected Weapon(String name) {
		super(name);
	}
	
	public Double getDamage() {
		return damage;
	}

	public void setDamage(Double damage) {
		this.damage = damage;
		if (damage <MIN_DAMAGE) {
			this.damage = MIN_DAMAGE;
		}
	}

	public Double getCooldownTime() {
		return cooldownTime;
	}

	public void setCooldownTime(Double cooldownTime) {
		this.cooldownTime = cooldownTime;
		if (cooldownTime <=MIN_COOLDOWN) {
			this.cooldownTime =MIN_COOLDOWN;
		}
	}

	public boolean isCooldown() {
		return isCooldown;
	}

	public void setCooldown(boolean isCooldown) {
		this.isCooldown = isCooldown;
	}

	
	
	
	

}
