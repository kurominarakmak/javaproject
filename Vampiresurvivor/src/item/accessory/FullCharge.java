package item.accessory;

import item.base.Accessory;
import javafx.scene.image.Image;

public class FullCharge extends Accessory{
	
	
	public FullCharge() {
		super("Full Charge");
		setPrice(180);
		setEffect(25.00);
		setStatPerLevel(3.25);
		setItemDescription("Your Body are gotten HEATTTTING UPPP");
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/fullCharge.png")));
	}
	
}
