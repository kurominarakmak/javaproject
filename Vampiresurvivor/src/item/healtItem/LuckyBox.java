package item.healtItem;

import item.base.HealthItem;
import javafx.scene.image.Image;

public class LuckyBox extends HealthItem{
	
	public LuckyBox() {
		super("LuckyBox");
		setEffect(10.00);
		setPrice(20);
		setStatPerLevel(2.25);
		setItemDescription("What is inside??");
		setImg(new Image(ClassLoader.getSystemResourceAsStream("items/luckyBox.png")));
	}

	
}
