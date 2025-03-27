package scene.selector.item;

import item.base.Item;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import player.base.Player;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;


public class ItemButtonPane extends HBox{
	private Item item;
	private Player player;
	public ItemButtonPane (Item item, Player player) {
		
		super();
		this.item = item;
		this.player = player;
		setPrefHeight(70);
		setMaxHeight(70);
		setPrefWidth(150);
		setMaxWidth(150);
		setBackground(new Background(new BackgroundFill(Color.web("#CDE8E5"), new CornerRadii(10), null)));
        setOnMouseEntered(e -> this.setBackground(new Background(new BackgroundFill(Color.web("#7AB2B2"), new CornerRadii(10), null))));
        setOnMouseExited(e -> this.setBackground(new Background(new BackgroundFill(Color.web("#CDE8E5"), new CornerRadii(10), null))));
        setSpacing(8);
		setAlignment(Pos.CENTER);
		
		
		initializeItem();
		this.setOnMouseClicked(event -> {
            if (player != null) {
                player.getBackpack().add(item); // Add item to inventory
                System.out.println("Added " + item.getItemName() + " to inventory!");
                
                // Close item selection window
                ItemSelectorStage.getInstance(player).close();
            }
        });
		
		
	}
	
	
	public void initializeItem() {
	    if (item == null || item.getImg() == null) {
	        System.out.println("Error: Item or item image is null!");
	        return;
	    }

	    ImageView imageView = new ImageView(item.getImg());
	    imageView.setFitWidth(30); // Ensure it scales properly
	    imageView.setFitHeight(50);

	    Text titleText = new Text(this.item.getItemName());
	    titleText.setFont(Font.font("Tohama", FontWeight.BOLD, 16));

	    this.getChildren().addAll(imageView, titleText);
	}
	
	
	
}