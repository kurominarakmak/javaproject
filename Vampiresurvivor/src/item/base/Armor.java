package item.base;

public abstract class Armor extends Item{
	private Double armorPoint;
	
	protected Armor(String name) {
		super(name);
	}
	
	protected Double getArmorPoint() {
		return armorPoint;
	}

	protected void setArmorPoint(Double armorPoint) {
		this.armorPoint = armorPoint;
	}

	

}
