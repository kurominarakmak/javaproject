package item.base;

import java.util.Objects;

import item.weapon.OraSword;
import javafx.scene.image.Image;

public abstract class Item {
	private static Item instance;
	private static Double MIN_EFFECT = 0.0;
	private Image img ;
	private static Integer MAX_LEVEL = 5;
	private Integer level;
	private Integer price;
	private String name;
	private String description;
	private Double effect;
	private Double statPerLevel;
	

	//constructor
	protected Item(String name){
		setItemName(name);
		level = 0;
		instance = this;
		
	}
	
	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public String getItemName() {
		return name;
	}

	public void setItemName(String name) {
		this.name = name;
	}

	public String getItemDescription() {
		return description;
	}

	public void setItemDescription(String description) {
		this.description = description;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getEffect() {
		return effect;
	}

	public void setEffect(Double effect) {
		this.effect = effect >= MIN_EFFECT ? effect : MIN_EFFECT;
	}

	public Double getStatPerLevel() {
		return statPerLevel;
	}

	public void setStatPerLevel(Double statPerLevel) {
		this.statPerLevel = statPerLevel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static Item getInstance() {
		return instance;
	}

	public void levelUp() {
		level++;
		effect+= statPerLevel;
		
	}

	
	
	



	
}
