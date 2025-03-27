package scene;

import application.Main;
import audio.BGM;
import audio.NameSE;
import audio.SE;
import entity.enemy.BigBoss;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.GameScene;
import player.base.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import java.util.Set;

public class PauseMenu extends StackPane {
    private GameScene gameScene;
    private Main mainApp;
    private boolean escPressed = false;

    public PauseMenu(GameScene gameScene, Main mainApp) {
        this.gameScene = gameScene;
        this.mainApp = mainApp;
        getLayout();
        
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (this.isVisible()) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });
    }
    
    public void update(Set<KeyCode> keys) {
    	if (keys.contains(KeyCode.ESCAPE)) {
            if (!escPressed) {
                escPressed = true;
                if (this.isVisible()) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        } else {
            escPressed = false;
        }
    }

    private void getLayout() {
        Rectangle background = new Rectangle(GameScene.WIDTH, GameScene.HEIGHT, Color.rgb(0, 0, 0, 0.5));
        
        Label title = new Label("PAUSE");
        title.setPadding(new Insets(16, 0, 96, 0));
        title.getStyleClass().add("title-sub-shadow");

        Button resumeButton = new Button("Resume");
        resumeButton.setPrefSize(120, 40);
        resumeButton.getStyleClass().add("button-menu");
         
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(120, 40);
        exitButton.getStyleClass().add("button-menu");
        
        VBox menuBox = new VBox(24, title, resumeButton, exitButton);
        menuBox.getStyleClass().add("grid-box-skyblue");
        menuBox.setPrefSize(300, 328);
        menuBox.setMinSize(300, 328);
        menuBox.setMaxSize(300, 328);
        menuBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(background, menuBox);
        this.setVisible(false); // เริ่มต้นซ่อนเมนู
        
        // Button
        resumeButton.setOnAction(e -> {
        	resumeGame();
        	
        	SE.playSE(NameSE.SND_MENU_CONFIRM);
        });
        resumeButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        
        exitButton.setOnAction(e -> {
        	BigBoss.resetTimeStop();
        	Player newPlayer = CharacterScene.getSelectedCharacter();
        	gameScene.setPlayer(newPlayer);
        	
        	mainApp.showHome();
        	
        	SE.playSE(NameSE.SND_MENU_BACK);
        }); 
        exitButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
    }

    public void pauseGame() {
        gameScene.pauseGame();
        this.setVisible(true);
        this.requestFocus();
        
        SE.playSE(NameSE.SND_OPENMENU);
        BGM.setVolumeBGM(0.2);
    }

    public void resumeGame() {
        gameScene.resumeGame();
        this.setVisible(false);
        
        SE.playSE(NameSE.SND_MENU_BACK);
        BGM.setVolumeBGM(0.5);
    }
}