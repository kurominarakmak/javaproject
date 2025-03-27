package scene.selector.item;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import player.base.Player;
import item.accessory.RoboHead;
import item.armor.Suit;
import item.base.Item;
import item.healtItem.*;
import item.weapon.OraSword;
import item.weapon.Envil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



public class ItemSelectorPane extends VBox {
	private final Integer MIN_SCOPE = 1;
	private final Integer MAX_SCOPE = 6;
	private Integer randomNumberItem ;
	private ArrayList<Item> items ;
	private static ItemSelectorPane instance;
	private Player player;
	
	private ItemSelectorPane(Player player) {
		
		super();
		this.player = player;
    	setPrefWidth(300);
    	setPrefHeight(260);
    	setAlignment(Pos.TOP_CENTER);
    	setSpacing(10);
    	
    	BackgroundFill bgFill = new BackgroundFill(Color.web("#EEF7FF"),null,null);
    	Background bg = new Background(bgFill);
    	setBackground(bg);
    	setPadding(new Insets(10,0,10,0));
    	
    	initializeItem();
    	
    	
    		
    	}
	
	public void refreshItem() {
		this.getChildren().clear();
		initializeItem();
		
	}
	
	public void initializeItem() {
		
		for (int itemI = 1 ; itemI <= 3; itemI++ ) {
    		randomNumberItem = ThreadLocalRandom.current().nextInt(MIN_SCOPE, MAX_SCOPE + 1);
    		Item newItem;
    		switch (randomNumberItem) {
			case 1: {
				newItem = new Envil();
				break;
			}
			case 2: {
				newItem = new CouchPotato();
				break;
			}
			case 3: {
				newItem = new LuckyBox();
				break;
			}
			case 4: {
				newItem = new RoboHead();
				break;
			}
			case 5: {
				newItem = new OraSword();
				break;
			}
			case 6: {
				newItem = new Suit();
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + itemI);
			}
    		getChildren().add(new ItemButtonPane(newItem, player));
    		
    		
			};
	}
	
	 public static ItemSelectorPane getInstance(Player player) {
	        if (instance == null) {
	            instance = new ItemSelectorPane(player);
	        }
	        return instance;
	    }
	
	
}