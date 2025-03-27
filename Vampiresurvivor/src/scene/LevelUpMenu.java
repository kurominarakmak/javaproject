package scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import player.base.Player;
import item.accessory.RoboHead;
import item.armor.Suit;
import item.base.Item;
import item.healtItem.*;
import item.weapon.OraSword;
import item.weapon.Envil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.Main;
import audio.BGM;
import audio.NameSE;
import audio.SE;
import gameLoader.GameLoader;
import logic.GameScene;

public class LevelUpMenu extends StackPane {
	private GameScene gameScene;
	private Main mainApp;
    private Player player ;
    private List<Item> availableItems;
 

    public LevelUpMenu(GameScene gameScene, Main mainApp, Player player) {
    	this.player = player;
    	this.gameScene = gameScene;
    	this.mainApp = mainApp;
    	
        this.availableItems = getRandomItems(3);
        
        this.setVisible(false);
        getLayout();
    }

    private void getLayout() {
    	
        Rectangle background = new Rectangle(800, 600, Color.rgb(0, 0, 0, 0.7));
        
        Label title = new Label("Choose an Item");
        title.getStyleClass().add("title-sub");

        VBox itemContainer = new VBox(16);
        itemContainer.setAlignment(Pos.CENTER);

        for (Item item : availableItems) {
            itemContainer.getChildren().add(createItemCard(item));
        }

        Button closeButton = new Button("Cancel");
        closeButton.setPrefSize(120, 40);
        closeButton.getStyleClass().add("button-menu");

        VBox root = new VBox(24, title, itemContainer, closeButton);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(800, 600);
        root.setMinSize(800, 600);
        root.setMaxSize(800, 600);
        
        this.getChildren().addAll(background, root);

        
        closeButton.setOnAction(e -> {
        	close();
        	
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        closeButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
    }

    private Button createItemCard(Item item) {

        // Image
        ImageView itemImage = new ImageView(item.getImg());
        itemImage.setFitWidth(50);
        itemImage.setFitHeight(50);
        itemImage.setPreserveRatio(false);
        
        StackPane imageContainer = new StackPane();
        imageContainer.getStyleClass().add("grid-box");
        imageContainer.getChildren().add(itemImage);
        
        // Item Texts
        Text itemName = new Text(item.getItemName());
        itemName.setFill(Color.rgb(255, 255, 255));
        itemName.getStyleClass().add("text-sub-24");

        Text itemDescription = new Text(item.getItemDescription());
        itemDescription.setFill(Color.rgb(255, 255, 255));
        itemDescription.setWrappingWidth(350);
        itemDescription.getStyleClass().add("text-sub-18");

        
        VBox textContainer = new VBox(itemName, itemDescription);
        textContainer.setPadding(new Insets(0, 16, 0, 0));
        textContainer.setAlignment(Pos.CENTER_LEFT);
        
        HBox itemBox = new HBox(16, imageContainer, textContainer);
        itemBox.setAlignment(Pos.CENTER_LEFT);
        
        Button card = new Button();
        card.setPrefSize(500, 100);
        card.setMinSize(500, 100);
        card.setMaxSize(500, 100);
        card.getStyleClass().add("button-levelup");
        card.setGraphic(itemBox); 
        
        
        
        
        card.setOnAction(e -> {
            Player.getInstance().upgradeItems(item);
            close();
            
            SE.playSE(NameSE.SND_MENU_CONFIRM);
        });
        card.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });

        return card;
    }

    private List<Item> getRandomItems(int count) {
        List<Item> allItems = ShopScene.itemList;

        List<Item> randomItems = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            randomItems.add(allItems.get(random.nextInt(allItems.size())));
        }

        return randomItems;
    }
    
    
    public void show() {
    	this.setVisible(true);
    	gameScene.pauseGame();
    	
    	SE.playSE(NameSE.SND_DELIVERY);
    	BGM.setVolumeBGM(0.2);
    }
    
    public void close() {
    	player.isLevelup = false;
    	setVisible(false);
    	getChildren().clear();
    	availableItems.clear();
    	availableItems = getRandomItems(3);
    	getLayout();
    	gameScene.resumeGame();
    	
    	BGM.setVolumeBGM(0.5);
    }
    
    public boolean isLevelUpActive() {
        return this.isVisible();
    }
    
}
