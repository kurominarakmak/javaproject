package item.healtItem;

import item.base.HealthItem;
import javafx.scene.image.Image;

public class Heart extends HealthItem{
	
	public Heart() {
		super("Heart");
		setItemDescription("Heart that compress mana from another world");
		setEffect(30.00);
		setStatPerLevel(3.25);
		setPrice(50);
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/heart.png")));

	}
	
	
	

}
