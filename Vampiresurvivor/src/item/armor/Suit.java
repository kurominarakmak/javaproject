package item.armor;

import item.base.Armor;
import item.weapon.OraSword;
import item.weapon.Envil;
import javafx.scene.image.Image;

public class Suit extends Armor {
	
	public Suit() {
		super("Suit");
		this.setItemDescription("This suit borrow from yagu");
		this.setEffect(10.0);
		setStatPerLevel(5.0);
		setPrice(100);
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/suit.png")));

	}


}
