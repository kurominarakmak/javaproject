package application;

import logic.GameScene;
import scene.*;
import audio.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;


public class Main extends Application {
	private Stage primaryStage;
    private Scene homeScene, shopScene, stageScene, characterScene, gameScene;
    private ShopScene shopLayout;
    private MediaPlayer mediaPlayer;
    
    private boolean isPlay = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;


        // Layout
        HomeScene homeLayout = new HomeScene(this);
        shopLayout = new ShopScene(this);
        StageScene stageLayout = new StageScene(this);
        CharacterScene characterLayout = new CharacterScene(this);
        GameScene gameInstance = new GameScene(this);

        // Scene
        homeScene = new Scene(homeLayout.getLayout(), 800, 600);
        shopScene = new Scene(shopLayout.getLayout(), 800, 600);
        stageScene = new Scene(stageLayout.getLayout(), 800, 600);
        characterScene = new Scene(characterLayout.getLayout(), 800, 600);
        gameScene = gameInstance.getScene();

        
        homeScene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());
        shopScene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());
        stageScene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());
        characterScene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());

        
        primaryStage.setTitle("WWW!!! If not today, then when? And those coming after me will drag me to write JavaFX no matter what!");
        primaryStage.setScene(homeScene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        BGM.playBGM(NameBGM.BGM_SSS);

    }

    public void showHome() {
        primaryStage.setScene(homeScene);
           
        if (isPlay) {
        	BGM.playBGM(NameBGM.BGM_SSS);
        	isPlay = false;
        }
    }

    public void showShop() {
    	shopLayout.updateCoins();
        primaryStage.setScene(shopScene);
    }
    
    public void showStage() {
        primaryStage.setScene(stageScene);
    }
    
    public void showCharacter() {
        primaryStage.setScene(characterScene);
    }
    
    public void showGame() {
        GameScene sceneInstance = GameScene.getInstance();
        sceneInstance.setStartTime(System.nanoTime());
        sceneInstance.setStart(true);
     
        primaryStage.setScene(sceneInstance.getScene());
        
        isPlay = true;
    }
    

 
    public static void main(String[] args) {
        launch(args);
    }
}
