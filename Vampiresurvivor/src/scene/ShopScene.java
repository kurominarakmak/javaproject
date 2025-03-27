package scene;

import item.base.*;
import item.healtItem.CouchPotato;
import item.healtItem.LuckyBox;
import item.healtItem.Heart;
import item.weapon.OraSword;
import item.weapon.Envil;
import application.Main;
import item.accessory.FullCharge;
import item.accessory.RoboHead;
import item.armor.Suit;
import audio.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.BackgroundManager;
import utils.animation.DiagonalLines;
import utils.animation.FallingTriangles;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import gameLoader.*;

public class ShopScene {
	private Main mainApp;
	
    public static int playerCoins = GameLoader.loadCoins();
    private ImageView itemImage = new ImageView();
    private Label itemImageLabel = new Label();
    private Label titleLabel = new Label("SHOP");
    private Label coinTitleLabel = new Label("COIN:");
    private Label coinLabel = new Label("" + playerCoins);
    private Label itemNameLabel = new Label("");
    private Label itemPriceLabel = new Label("");
    private Label itemLevelLabel = new Label("");
    private Label itemDescLabel = new Label("");

    public static List<Item> itemList = GameLoader.loadItemLevels();
    private Map<Item, List<Rectangle>> levelBlocksMap = new HashMap<>();
    
    private FallingTriangles fallingTriangles;
    private DiagonalLines diagonalLines;

    public ShopScene(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    public BorderPane getLayout() {
    	//Root
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(24, 48, 24, 48));
        
        Pane backGround = new Pane();
        backGround.setBackground(BackgroundManager.createBackground("bg_blue"));

        Pane fallingLayer = new Pane();
        fallingTriangles = new FallingTriangles(fallingLayer);
        
        Pane diagonalLinesPane = new Pane();
        diagonalLines = new DiagonalLines(diagonalLinesPane);
        diagonalLinesPane.setOpacity(0.3);
        
        
        StackPane mainRoot = new StackPane();
        mainRoot.getChildren().addAll(backGround, diagonalLinesPane, fallingLayer, root);
        
        
        
        //Root   
        titleLabel.getStyleClass().add("title-sub");
        
        coinTitleLabel.getStyleClass().add("title-coin-label");
        
        coinLabel.setMinSize(200, 50);
        coinLabel.setAlignment(Pos.CENTER_RIGHT);
        coinLabel.getStyleClass().add("coin-label");
        
        Region spacerTitle = new Region();
        HBox.setHgrow(spacerTitle, Priority.ALWAYS);
        
        //ยัดลง topbar
        HBox topBar = new HBox(titleLabel, spacerTitle, coinTitleLabel, coinLabel);
        topBar.setAlignment(Pos.CENTER);
		

        // GridPane สำหรับไอเท็ม
        GridPane itemGrid = new GridPane();
        itemGrid.setMaxSize(320, 420);
        itemGrid.setHgap(16);
        itemGrid.setVgap(16);
        itemGrid.setPadding(new Insets(10));
        itemGrid.getStyleClass().add("grid-box");

        

        //รายละเอียดไอเท็มฝั่งช้าย
        for (int i = 0; i < itemList.size(); i++) {
        	Item item = itemList.get(i);

            ImageView itemImage = new ImageView(item.getImg());
            itemImage.setFitHeight(50);
            itemImage.setFitWidth(50);
            itemImage.setPreserveRatio(false);
            
            Button itemButton = new Button();
            itemButton.setMinSize(50, 70);
            itemButton.setGraphic(itemImage);
            itemButton.getStyleClass().add("button-item");

        	// HBox สำหรับใส่บล็อก Level
            HBox levelBox = new HBox(2);
            List<Rectangle> levelBlocks = new ArrayList<>();
            levelBox.setSpacing(1);
            
            for (int j = 0; j < 5; j++) {
                Rectangle block = new Rectangle(12, 12, Color.rgb(114, 114, 114));
                levelBlocks.add(block);
                levelBox.getChildren().add(block);
            }
            
            // เก็บบล็อกของไอเท็มใน Map
            levelBlocksMap.put(item, levelBlocks);
            
            VBox itemContainer = new VBox(6, itemButton, levelBox);
            itemContainer.setAlignment(Pos.CENTER);
            itemContainer.getStyleClass().add("item-container");
            
            // ใส่ VBox ลงใน GridPane
            itemGrid.add(itemContainer, i % 4, i / 4);

            
            
            // คลิกเพื่อซื้อ
            itemButton.setOnAction(e -> {
            	buyItem(item);
            	SE.playSE(NameSE.SND_BUYWORKER);
            });
            
            // แสดงรายละเอียดเมื่อ hover
            itemButton.setOnMouseEntered(e -> {
            	showItemDetails(item);
            	SE.playSE(NameSE.SND_MENU_SELECT);
            });
            itemButton.setOnMouseExited(e -> {
            	showNoting();
            });
        }
        
        for (Item item : itemList) {
        	List<Rectangle> levelBlocks = levelBlocksMap.get(item);
            if (levelBlocks != null) {
                int level = Math.min(item.getLevel(), 5); // จำกัดสูงสุด 5 บล็อก
                for (int i = 0; i < level; i++) {
                    levelBlocks.get(i).setFill(Color.rgb(49, 224, 255)); // เปลี่ยนเป็นสีเขียว
                }
            }
        	
        }

        // รายละเอียดไอเท็มฝั่งขวา
        itemImage.setFitHeight(40);
        itemImage.setFitWidth(40);
        itemImage.setPreserveRatio(false);
        
        itemImageLabel.setMinSize(50, 70);
        itemImageLabel.setGraphic(itemImage);
        itemImageLabel.getStyleClass().add("item-image-details");
        
        itemNameLabel.setWrapText(true);
        itemNameLabel.setMaxWidth(160);
        itemNameLabel.getStyleClass().add("text-sub-18");
        
        itemPriceLabel.getStyleClass().add("text-sub-18");
        
        itemLevelLabel.getStyleClass().add("text-sub-18");
        
        itemDescLabel.getStyleClass().add("text-sub-18");
        itemDescLabel.setWrapText(true);
        itemDescLabel.setMaxWidth(310);
        
        VBox detailsName = new VBox(10, itemNameLabel, itemPriceLabel, itemLevelLabel);
        HBox detailsImage = new HBox(16, itemImageLabel, detailsName);
        VBox detailsDesc = new VBox(10, detailsImage, itemDescLabel);
        VBox detailsBox = new VBox(10, detailsDesc);
        detailsBox.getStyleClass().add("item-details");

        BorderPane itemDetails = new BorderPane();
        itemDetails.setCenter(detailsBox);
        itemDetails.setPrefWidth(310);
        itemDetails.setMaxWidth(Double.MAX_VALUE);
        itemDetails.setPadding(new Insets(20));
        itemDetails.getStyleClass().add("grid-box");
        
        
        // ปุ่มย้อนกลับ
        Region spacerBack = new Region();
        HBox.setHgrow(spacerBack, Priority.ALWAYS);
        
        Button backButton = new Button("BACK");
        backButton.setPrefSize(120, 40);
        backButton.getStyleClass().add("button-menu");
        
        HBox bottomBar = new HBox(spacerBack, backButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(16, 0, 16, 0));
        

        // จัดวาง UI
        root.setTop(topBar);
        root.setLeft(itemGrid);
        root.setRight(itemDetails);
        root.setBottom(bottomBar);
        
        
        // button
        backButton.setOnAction(e -> {
        	mainApp.showHome();
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        backButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });

        return new BorderPane(mainRoot);
    }
    
    public void updateCoins() {
//        playerCoins = GameLoader.loadCoins();
        coinLabel.setText("" + playerCoins);
    }


    private void showItemDetails(Item item) {
    	itemImage.setImage(item.getImg());
    	
    	
        itemNameLabel.setText(item.getName());
        itemPriceLabel.setText("PRICE: " + item.getPrice());
        if(item.getLevel() == 5) {
        	itemLevelLabel.setText("LEVEL: MAX");
        } else {
        	itemLevelLabel.setText("LEVEL: " + item.getLevel());
        }
        itemDescLabel.setText(item.getItemDescription());
    }
    
    private void showNoting() {
    	itemImage.setImage(null);
    	itemNameLabel.setText("");
        itemPriceLabel.setText("");
        itemLevelLabel.setText("");
        itemDescLabel.setText("");
    }

    private void buyItem(Item item) {
    	if (item.getLevel() == 5) {
    		SE.playSE(NameSE.SND_FAILED);
    		
    	} else if (playerCoins >= item.getPrice()) {
            playerCoins -= item.getPrice(); 
            item.levelUp();
            GameLoader.saveGame(playerCoins, itemList);
            coinLabel.setText("" + playerCoins);
            
            List<Rectangle> levelBlocks = levelBlocksMap.get(item);
            if (levelBlocks != null) {
                int level = Math.min(item.getLevel(), 5); // จำกัดสูงสุด 5 บล็อก
                for (int i = 0; i < level; i++) {
                    levelBlocks.get(i).setFill(Color.rgb(49, 224, 255)); // เปลี่ยนเป็นสีเขียว
                }
            }
            
            showItemDetails(item);
            
            SE.playSE(NameSE.SND_BUYWORKER);
        } else {
            itemDescLabel.setText("Not enough coins!");
        }
    }
    
}
