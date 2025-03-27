package scene;

import application.Main;

import audio.BGM;
import audio.NameBGM;
import audio.NameSE;
import audio.SE;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.GameScene;
import player.base.Player;
import stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import gameLoader.*;

public class GameOverScreen extends StackPane {
	private GameScene gameScene;
    private Main mainApp;
    private boolean active = false;
    
    private Label scoreLabel;
    
    private double elapsedTime;
    
    public static int playerCoins = GameLoader.loadCoins();

    public GameOverScreen(GameScene gameScene, Main mainApp) {
        this.gameScene = gameScene;
        this.mainApp = mainApp;
        getLayout();
    }

    private void getLayout() {
        Rectangle background = new Rectangle(GameScene.WIDTH, GameScene.HEIGHT, Color.BLACK);
        background.setOpacity(0.7);

        Label gameOverLabel = new Label("Game Over");
        gameOverLabel.getStyleClass().add("title-sub");
        
        scoreLabel = new Label("Score: ");
        scoreLabel.getStyleClass().add("text-sub-32");
        scoreLabel.setPadding(new Insets(0, 0, 12, 0));

        Button retryButton = new Button("Retry");
        retryButton.setPrefSize(120, 40);
        retryButton.getStyleClass().add("button-menu");
        
        Button homeButton = new Button("Home");
        homeButton.setPrefSize(120, 40);
        homeButton.getStyleClass().add("button-menu");
        
        VBox menuBox = new VBox(24, gameOverLabel, scoreLabel, retryButton, homeButton);
        menuBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(background, menuBox);
        this.setVisible(false);
        
        
        
        retryButton.setOnAction(e -> {
        	Player newPlayer = CharacterScene.getSelectedCharacter();
        	gameScene.setPlayer(newPlayer);
        	
        	hide();
        	mainApp.showGame();
        	
        	SE.playSE(NameSE.SND_MENU_CONFIRM);
        });
        retryButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
        homeButton.setOnAction(e -> {
        	Player newPlayer = CharacterScene.getSelectedCharacter();
        	gameScene.setPlayer(newPlayer);
        	hide();
        	mainApp.showHome();
        	
        	SE.playSE(NameSE.SND_MENU_BACK);
        });
        homeButton.setOnMouseEntered(e -> {
        	SE.playSE(NameSE.SND_MENU_SELECT);
        });
    }
    
    public void update() {
    	scoreLabel.setText(String.format("Score: %d", (int) Math.floor(elapsedTime)));
    	
        if (active) {
            return;
        }
    }
    
    public void show() {
        if (gameScene.getLevelUpMenu().isLevelUpActive()) {
            gameScene.getLevelUpMenu().setVisible(false);
        }
        
        playerCoins += (int) Math.floor(elapsedTime);
        GameLoader.saveGame(playerCoins, null);
        
        active = true;
        gameScene.pauseGame();
        this.setVisible(true);
        
        BGM.playBGM(NameBGM.BGM_GAMEOVER);
    }
    
    public void hide() {
        active = false;
        this.setVisible(false);
    }

    public boolean isActive() {
        return active;
    }
    
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
