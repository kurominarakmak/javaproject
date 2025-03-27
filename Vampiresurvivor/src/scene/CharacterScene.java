package scene;

import java.util.ArrayList;
import java.util.List;

import application.Main;
import audio.NameSE;
import audio.SE;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import logic.GameScene;
import utils.BackgroundManager;
import utils.animation.DiagonalLines;
import utils.animation.FallingTriangles;

import player.base.*;

public class CharacterScene {
    private Main mainApp;
    
    private FallingTriangles fallingTriangles;
	private DiagonalLines diagonalLines;
	
	private ImageView characterImage;
    private Label nameLabel, hpLabel, defLabel, atkLabel, spdLabel, descLabel;
    private Image spriteSheet;
    private Image[] animationFrames;
    private static int currentCharacterIndex = 0;
    
    private static List<Player> characterList = new ArrayList<>();
    
    // Default Character
    private static Player selectedCharacter = new ChickBoy(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2); 


    public CharacterScene(Main mainApp) {
        this.mainApp = mainApp;
        loadCharacters();
    }

    public BorderPane getLayout() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(24, 0, 0, 0));
        
        Pane backGround = new Pane();
        backGround.setBackground(BackgroundManager.createBackground("bg_blue"));
        
        Pane fallingLayer = new Pane();
        fallingTriangles = new FallingTriangles(fallingLayer);
        
        Pane diagonalLinesPane = new Pane();
        diagonalLines = new DiagonalLines(diagonalLinesPane);
        diagonalLinesPane.setOpacity(0.3);
        
        StackPane mainRoot = new StackPane();
        mainRoot.getChildren().addAll(backGround, diagonalLinesPane, fallingLayer, root);
        
        
        
        loadCharacters();
        
        
        
        // root
        Label title = new Label("CHARACTER");
        title.getStyleClass().add("title-sub");
        
        
        
        // Character Image
        characterImage = new ImageView();
        characterImage.setFitWidth(160);
        characterImage.setFitHeight(200);
        characterImage.setPreserveRatio(true);
        characterImage.setImage(loadCharacterSprite((ChickBoy) characterList.get(currentCharacterIndex)));

        VBox imageContainer = new VBox(characterImage);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinSize(200, 367);
        imageContainer.setMaxSize(200, 367);
        imageContainer.getStyleClass().add("grid-box");

        // Character Info Box
        nameLabel = new Label(characterList.get(0).getName());
        hpLabel = new Label("HP: " + characterList.get(0).getMaxHealth());
        defLabel = new Label("DEF: " + characterList.get(0).getArmor());
        atkLabel = new Label("ATK: " + characterList.get(0).getDamage());
        spdLabel = new Label("SPD: " + characterList.get(0).getSpeed());
        descLabel = new Label("DESCRIPTION: " + characterList.get(0).getDesc());

        nameLabel.getStyleClass().add("text-sub-24");
        hpLabel.getStyleClass().add("text-sub-18");
        defLabel.getStyleClass().add("text-sub-18");
        atkLabel.getStyleClass().add("text-sub-18");
        spdLabel.getStyleClass().add("text-sub-18");
        descLabel.getStyleClass().add("text-sub-18");
        descLabel.setWrapText(true);

        VBox characterStats = new VBox(10, nameLabel, hpLabel, defLabel, atkLabel, spdLabel, descLabel);
        characterStats.setAlignment(Pos.TOP_LEFT);
        characterStats.setPadding(new Insets(16));
        characterStats.setMinSize(288, 367);
        characterStats.setMaxSize(288, 367);
        characterStats.getStyleClass().add("grid-box");

        
        HBox characterDisplay = new HBox(12, imageContainer, characterStats);
        characterDisplay.setAlignment(Pos.TOP_CENTER);

        
        
        Button leftButton = new Button("<");
        leftButton.getStyleClass().add("button-select");
        
        Button rightButton = new Button(">");
        rightButton.getStyleClass().add("button-select");

        HBox navigationBox = new HBox(20, leftButton, characterDisplay, rightButton);
        navigationBox.setAlignment(Pos.CENTER);

        
        
        Button selectButton = new Button("SELECT");
        selectButton.setPrefSize(120, 40);
        selectButton.getStyleClass().add("button-menu");
        
        Button backButton = new Button("BACK");
        backButton.setPrefSize(120, 40);
        backButton.getStyleClass().add("button-menu");
        
        HBox bottomBar = new HBox(32, backButton, selectButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(0, 0, 42, 0));
        
        
        
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setCenter(navigationBox);
        root.setBottom(bottomBar);
        
        
        
        leftButton.setOnAction(e -> {
        	changeCharacter(-1);
        	SE.playSE(NameSE.SND_CHARSELECTWOOSH);
        });
        leftButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        rightButton.setOnAction(e -> {
        	changeCharacter(1);
        	SE.playSE(NameSE.SND_CHARSELECTWOOSH);
        });
        rightButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        selectButton.setOnAction(e -> {
        	selectCharacter();
        	GameScene.lastTimeUpdate = System.nanoTime();
        	mainApp.showStage();
        	SE.playSE(NameSE.SND_MANAGELEVEL);
        });
        selectButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        backButton.setOnAction(e -> {
        	mainApp.showHome();
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        backButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });

        return new BorderPane(mainRoot);
    }
    
    private void loadCharacters() {
        characterList.add(new ChickBoy(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2));
        characterList.add(new Mage(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2));
        characterList.add(new RedHoodBoy(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2));
    }

    private void changeCharacter(int direction) {
        currentCharacterIndex += direction;
        if (currentCharacterIndex < 0) currentCharacterIndex = characterList.size() - 1;
        if (currentCharacterIndex >= characterList.size()) currentCharacterIndex = 0;
        updateCharacterDisplay();
    }

    private void updateCharacterDisplay() {
        Player character = characterList.get(currentCharacterIndex);
        nameLabel.setText(character.getName());
        hpLabel.setText("HP: " + character.getMaxHealth());
        defLabel.setText("DEF: " + character.getArmor());
        atkLabel.setText("ATK: " + character.getDamage());
        spdLabel.setText("SPD: " + character.getSpeed());
        descLabel.setText("DESCRIPTION:" + character.getDesc());

        if (character instanceof ChickBoy) {
            loadCharacterSprite((ChickBoy) character);
        } else if (character instanceof RedHoodBoy) {
            loadCharacterSprite((RedHoodBoy) character);
        } else if (character instanceof Mage) {
            loadCharacterSprite((Mage) character);
        } else {
            System.out.println("Error: Unknown character type!");
        }
    }
    
    private Image loadCharacterSprite(Player player) { 	
        if (player instanceof ChickBoy) {
            ((ChickBoy) player).loadSpriteSheet("idle");
            characterImage.setImage(((ChickBoy) player).getIdleFrame());
            return ((ChickBoy) player).getIdleFrame();
            
        } else if (player instanceof RedHoodBoy) {
            ((RedHoodBoy) player).loadSpriteSheet("idle");
            characterImage.setImage(((RedHoodBoy) player).getIdleFrame());
            return ((RedHoodBoy) player).getIdleFrame();
            
        } else if (player instanceof Mage) {
            ((Mage) player).loadSpriteSheet("idle");
            characterImage.setImage(((Mage) player).getIdleFrame());
            return ((Mage) player).getIdleFrame();
            
        }
        
        return null;
    }
    
    private void selectCharacter() {
        selectedCharacter = characterList.get(currentCharacterIndex);
        System.out.println("Selected Character: " + selectedCharacter.getName());
    }
    
    public static Player getSelectedCharacter() {
        return newPlayerInstance(); 
    }

    private static Player newPlayerInstance() {
        Player basePlayer = characterList.get(currentCharacterIndex);

        // Create a new instance based on the selected character
        if (basePlayer instanceof ChickBoy) {
            return new ChickBoy(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2);
        } else if (basePlayer instanceof Mage) {
            return new Mage(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2);
        } else if (basePlayer instanceof RedHoodBoy) {
            return new RedHoodBoy(GameScene.GAME_WIDTH / 2, GameScene.GAME_HEIGHT / 2);
        } else {
            throw new IllegalStateException("Unknown character type");
        }
    }

    
}
