package item.accessory;

import item.base.Accessory;
import item.weapon.OraSword;
import javafx.scene.image.Image;

public class RoboHead extends Accessory{
	
	public RoboHead() {
		super("Robot Head");
		setEffect(0.25);
		setStatPerLevel(0.25);
		setPrice(100);
		setItemDescription("Wear it!! you must be so cool >< ");
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/RoboHead.png")));
	}
	

}
