package item.weapon;

import java.util.ArrayList;

import item.base.Item;
import item.base.Weapon;
import item.healtItem.CouchPotato;
import item.healtItem.Heart;
import itemAbility.Upgradable;
import javafx.scene.image.Image;

public class Envil extends Weapon {
	
	

	public Envil() {
		super("Envil");
		setItemDescription("I promise i won't break yor Weapon ^3^ (..........maybe)");
		setEffect(30.0);
		setPrice(180);
		setCooldownTime(2.5);
		setStatPerLevel(3.25);
		setCooldown(false);
		setDamage(10.00);
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/upDamage.png")));
		
	}


}
