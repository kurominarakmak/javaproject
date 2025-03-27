package scene;

import java.util.ArrayList;
import java.util.List;

import application.Main;
import audio.BGM;
import audio.NameBGM;
import audio.NameSE;
import audio.SE;
import item.accessory.RoboHead;
import item.base.Item;
import stage.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import logic.GameScene;
import player.base.ChickBoy;
import player.base.Mage;
import player.base.Player;
import player.base.RedHoodBoy;
import utils.BackgroundManager;
import utils.animation.DiagonalLines;
import utils.animation.FallingTriangles;

public class StageScene {
    private Main mainApp;
    
    private Label stageNameLabel = new Label();
    private Label descriptionLabel = new Label();
    private ImageView stageImage = new ImageView();
    
    
    private FallingTriangles fallingTriangles;
	private DiagonalLines diagonalLines;
	
	public static List<Stage> stageList = new ArrayList<>();
	private static int currentLevel = 0;
    
	//เก็บด่านที่เลือก
	public static String backgroundImagePath = "/Background/Map1Background.png";
    
	public StageScene(Main mainApp) {		
        this.mainApp = mainApp;
        allLevel();
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


        //Root
        // Title
        Label title = new Label("STAGE");
        title.getStyleClass().add("title-sub");

        // Image Box (Placeholder)
        VBox imageContainer = new VBox(stageImage);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefSize(500, 208);
        imageContainer.setMinSize(500, 208);
        imageContainer.setMaxSize(500, 208);
        imageContainer.getStyleClass().add("grid-box");
        
        stageNameLabel.getStyleClass().add("text-sub-24");
        
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add("text-sub-18");

        VBox stageDetails = new VBox(16, stageNameLabel, descriptionLabel);
        stageDetails.setAlignment(Pos.TOP_LEFT);
        stageDetails.setPrefSize(500, 152);
        stageDetails.setMinSize(500, 152);
        stageDetails.setMaxSize(500, 152);
        stageDetails.getStyleClass().add("grid-box");

        VBox centerContainer = new VBox(12, imageContainer, stageDetails);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        
        
        // ปุ่มซ้าย-ขวาเปลี่ยนด่าน
        Button leftButton = new Button("<");
        leftButton.getStyleClass().add("button-select");
        
        Button rightButton = new Button(">");
        rightButton.getStyleClass().add("button-select");

        HBox navigationBox = new HBox(20, leftButton, centerContainer, rightButton);
        navigationBox.setAlignment(Pos.CENTER);
        

        // 
        Button playButton = new Button("PLAY");
        playButton.setPrefSize(120, 40);
        playButton.getStyleClass().add("button-menu");
        
        Button backButton = new Button("BACK");
        backButton.setPrefSize(120, 40);
        backButton.getStyleClass().add("button-menu");
        
        HBox bottomBar = new HBox(32, backButton, playButton);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(0, 0, 42, 0));

        
        
        // หน้าแรก
        stageNameLabel.setText(stageList.get(0).getName());
        descriptionLabel.setText("DESCRIPTION: " + stageList.get(0).getDesc());
        stageImage.setImage(stageList.get(0).getImage());
        
        
        // จัดวาง UI
        root.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setCenter(navigationBox);
        root.setBottom(bottomBar);
        
        
        
        leftButton.setOnAction(e -> {
        	changeLevel(-1);
        	SE.playSE(NameSE.SND_CHARSELECTWOOSH);
        });
        leftButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        rightButton.setOnAction(e -> {
        	changeLevel(1);
        	SE.playSE(NameSE.SND_CHARSELECTWOOSH);
        });
        rightButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        playButton.setOnAction(e -> {
        	GameScene.getInstance().refreshStage();
        	mainApp.showGame();
            
            SE.playSE(NameSE.SND_HAATOPROJ);
        });
        playButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        backButton.setOnAction(e -> {
        	mainApp.showCharacter();
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        backButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        


        return new BorderPane(mainRoot);
    }

    
    
    // เปลี่ยนด่าน
    private void changeLevel(int direction) {
        currentLevel += direction;
        if (currentLevel < 0) currentLevel = stageList.size() - 1;
        if (currentLevel >= stageList.size()) currentLevel = 0;
        updateStageDisplay();
    }
    
    private void updateStageDisplay() {
    	Stage selectedStage = stageList.get(currentLevel);
    	stageNameLabel.setText(selectedStage.getName());
        descriptionLabel.setText("DESCRIPTION: " + selectedStage.getDesc());
        stageImage.setImage(stageList.get(currentLevel).getImage());
    }

    
    private void allLevel() {
    	stageList.add(new AridZone());
    	stageList.add(new IDUNNO());
    }
    
    public static Stage getSelectedStage() {
        return newStageInstance(); 
    }

    private static Stage newStageInstance() {
        Stage baseStage = stageList.get(currentLevel);

        // Create a new instance based on the selected character
        if (baseStage instanceof AridZone) {
            return new AridZone();
        } else if (baseStage instanceof IDUNNO) {
            return new IDUNNO();
        } else {
            throw new IllegalStateException("Unknown character type");
        }
    }
}
