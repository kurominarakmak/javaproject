package item.healtItem;

import item.base.HealthItem;
import javafx.scene.image.Image;

public class CouchPotato extends HealthItem {
	
	public CouchPotato() {
		super("CouchPotato");
		setItemDescription("The tranfer famous product from NIHON!!");
		setEffect(5.00);
		setStatPerLevel(1.25);
		setPrice(20);
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/pota.png")));
	}
	
}
