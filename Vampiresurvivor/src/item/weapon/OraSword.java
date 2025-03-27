package item.weapon;

import item.base.Weapon;
import item.healtItem.CouchPotato;
import item.healtItem.Heart;
import javafx.scene.image.Image;

public class OraSword  extends Weapon{
	
	public OraSword() {
		super("OraSword");
		setCooldown(false);
		setEffect(20.0);
		setCooldownTime(1.5);
		setStatPerLevel(4.5);
		setItemDescription("An axe that found on Holy Yordle's Clif");
		setPrice(150);
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/OraSword.png")));

		
	}
	
}
