package scene;

import application.Main;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;
import utils.BackgroundManager;
import utils.animation.DiagonalLines;
import utils.animation.FallingTriangles;
import audio.*;

public class HomeScene {
    private Main mainApp;
    
    private Pane title = new Pane();
    private Button startButton = new Button("START");
    private Button shopButton = new Button("SHOP");
    private Button quitButton = new Button("QUIT");
    private HBox buttonBox = new HBox(48, startButton, shopButton, quitButton);
    
    
    private FallingTriangles fallingTriangles;
    private DiagonalLines diagonalLines;
    
    private double animationTime = 3.0;

    public HomeScene(Main mainApp) {
        this.mainApp = mainApp;
    }

    public BorderPane getLayout() {
        BorderPane root = new BorderPane();
        
        Pane backGround = new Pane();
        backGround.setBackground(BackgroundManager.createBackground("bg_blue"));
        
        title.setBackground(BackgroundManager.createBackground("title_name"));
        
        Pane fallingLayer = new Pane();
        fallingTriangles = new FallingTriangles(fallingLayer);
        
        Pane diagonalLinesPane = new Pane();
        diagonalLines = new DiagonalLines(diagonalLinesPane);
        diagonalLinesPane.setOpacity(0.3);
        
        
        StackPane mainRoot = new StackPane();
        mainRoot.getChildren().addAll(backGround, diagonalLinesPane, fallingLayer, title, root);
        
        
        
        //Root
        Label label = new Label("");
        label.getStyleClass().add("title");

        startButton.getStyleClass().add("button-menu");
        shopButton.getStyleClass().add("button-menu");
        quitButton.getStyleClass().add("button-menu");
        buttonBox.setAlignment(Pos.CENTER);
        
        Label footer = new Label("by Thon Mot");
        footer.getStyleClass().add("footer");
        
        root.setTop(label);
        root.setCenter(buttonBox);
        root.setBottom(footer);
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane.setAlignment(footer, Pos.CENTER);
        
        playAnimation();
        
        
        
        startButton.setOnAction(e -> {
        	mainApp.showCharacter();
        	SE.playSE(NameSE.SND_MENU_CONFIRM);
        });
        startButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        shopButton.setOnAction(e -> {
        	mainApp.showShop();
        	SE.playSE(NameSE.SND_MENU_CONFIRM);
        });
        shopButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });

        quitButton.setOnAction(e -> {
        	System.exit(0);
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        quitButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });

        return new BorderPane(mainRoot);
    }
    
    public void slideTitleDown(Pane pane) {
        pane.setTranslateY(-800);
        
        TranslateTransition transitionDown = new TranslateTransition(Duration.seconds(animationTime), pane);
        transitionDown.setToY(0);
        transitionDown.setInterpolator(Interpolator.EASE_OUT);
        transitionDown.play();
    }
    
    public void slideTitleUp(HBox hBox) {
    	hBox.setTranslateY(1600);
        
        TranslateTransition transitionUp = new TranslateTransition(Duration.seconds(animationTime), hBox);
        transitionUp.setToY(0);
        transitionUp.setInterpolator(Interpolator.EASE_OUT);
        transitionUp.play();
    }
    
    public void playAnimation() {
    	slideTitleDown(title);
        slideTitleUp(buttonBox);
    }
}
