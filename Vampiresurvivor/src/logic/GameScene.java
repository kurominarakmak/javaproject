package logic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.stage.Stage;
import player.base.Player;
import player.base.RedHoodBoy;

import scene.CharacterScene;
import scene.GameOverScreen;
import scene.LevelUpMenu;
import scene.PauseMenu;
import scene.StageScene;
import stage.*;
import scene.StartGameScreen;

import scene.selector.item.ItemSelectorPane;
import scene.selector.item.ItemSelectorStage;
import stage.AridZone;
import stage.IDUNNO;
import player.base.ChickBoy;
import player.base.Mage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import entity.projectile.Projectile;
//import application.Main
import application.Main;
import audio.BGM;
import audio.NameBGM;
import audio.NameSE;
import audio.SE;

//ลบ extends Application
public class GameScene {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int BORDER_SIZE = 0;
	public static final int GAME_WIDTH = 8000;
	public static final int GAME_HEIGHT = 1280;

	private static GameScene instance;
	private Player player;
	private Set<KeyCode> keys = new HashSet<>();
	private EnemyManager enemyManager;
	private AnimationTimer gameLoop;

	private long startTime;
	private long pausedTime = 0;
	private double elapsedTime;
	public static long lastTimeUpdate = System.nanoTime();

	// track FPS
	private long lastFrameTime = System.nanoTime();
	private int frameCount = 0;
	private double FPS = 0;

	// add field Main mainApp
	private static Main mainApp;
	private boolean start = false;

	// add Menu and Screen
	private PauseMenu pauseMenu;
	private GameOverScreen gameOverScreen;
	private LevelUpMenu levelUpMenu;

	// add Background
	private stage.Stage stage;
	private Image backgroundImage;
	private Image borderImageTop;
	private Image borderImageBottom;
	private Image borderImageLeft;
	private Image borderImageRight;

	// game stags
	private GameStats gameStats;

	// SE play
	private boolean isPlaySE = false;

	// add method GameScene(Main mainApp)
	public GameScene(Main mainApp) {
		this.mainApp = mainApp;
		this.stage = StageScene.getSelectedStage();
		this.backgroundImage = new Image(stage.getMapPath());
		this.borderImageTop = new Image(stage.getBorderBase().getTop());
		this.borderImageBottom = new Image(stage.getBorderBase().getBottom());
		this.borderImageLeft = new Image(stage.getBorderBase().getLeft());
		this.borderImageRight = new Image(stage.getBorderBase().getRight());
	}

	// void start to Scene getScene()
	public Scene getScene() {
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

//-------------------------------------------------------------------------------------------/////
		// gameStats and choose character
		setPlayer(CharacterScene.getSelectedCharacter());
		gameStats = new GameStats(player);
		enemyManager = new EnemyManager(this);

		Group root = new Group(canvas, gameStats);
		Scene scene = new Scene(root, WIDTH, HEIGHT);

		scene.getStylesheets().add(getClass().getResource("/style/Style.css").toExternalForm());

//----------------------------------------------------------------------------------//
		// Pause Menu
		pauseMenu = new PauseMenu(this, mainApp);
		root.getChildren().add(pauseMenu);
		pauseMenu.toFront();

//----------------------------------------------------------------------------------//
		// Game Over Scene
		gameOverScreen = new GameOverScreen(this, mainApp);
		root.getChildren().add(gameOverScreen);

//----------------------------------------------------------------------------------//
		// Level Up Menu
		levelUpMenu = new LevelUpMenu(this, mainApp, player);
		root.getChildren().add(levelUpMenu);
		levelUpMenu.toFront();

//----------------------------------------------------------------------------------//
		// receive key press w e a s d NOT DELETE!!!
		scene.setOnKeyPressed(event -> keys.add(event.getCode()));
		scene.setOnKeyReleased(event -> keys.remove(event.getCode()));

//----------------------------------------------------------------------------------//

		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (start) {
					update(now);
					render(gc);
					calculateFps(now);
					gameStats.setFps(FPS);
					gameStats.setElapsedTime(elapsedTime);
					gameOverScreen.setElapsedTime(elapsedTime);
				}
			}
		};

		gameLoop.start();
		playBGM();
		return scene;
	}

	private void update(long now) {
//    	System.out.println("HP : " +player.getHealth()+"|| speed :" + player.getSpeed() + "|| armor : " + player.getArmor());

		double deltaTime = (now - lastTimeUpdate) / 1_000_000_000.0;
		lastTimeUpdate = now;

		if (player.isDead() && !gameOverScreen.isActive()) {
			System.out.println("Game Over");
			gameOverScreen.show();
		}

		if (!gameOverScreen.isActive()) {
			// pause menu
			pauseMenu.update(keys);

			if (Player.isLevelup) {
				System.out.println(Player.isLevelup);
				levelUpMenu.show();
			}

			player.update(keys, GAME_WIDTH, GAME_HEIGHT, BORDER_SIZE, enemyManager, deltaTime, now);
			enemyManager.update(now, player, deltaTime);
			CollisionHandler.handleCollisions(player, enemyManager.getEnemies());
			player.enforceMapBoundaries(GameScene.GAME_WIDTH, GameScene.GAME_HEIGHT, GameScene.BORDER_SIZE);
			Iterator<Projectile> bulletIterator = player.getProjectiles().iterator();
			while (bulletIterator.hasNext()) {
				Projectile bullet = bulletIterator.next();
				bullet.update(enemyManager.getEnemies(), deltaTime, now);

				if (!bullet.isActive()) {
					bulletIterator.remove();

					isPlaySE = false;
				} else if (bullet.isActive() && !isPlaySE) {
					SE.playSE(NameSE.SND_TOWAGUN);
					isPlaySE = true;
				}

				if (player.getProjectiles().isEmpty()) {
					isPlaySE = false;
				}
			}

			elapsedTime = (now - startTime) / 1_000_000_000.0;
		}

		gameStats.update(now);

		gameOverScreen.update();
	}

	private void render(GraphicsContext gc) {
		if (!gameOverScreen.isActive() && start) {
			drawGameScene(gc);
			gameStats.draw(gc);
			// After drawing player and enemies:
		} else if (gameOverScreen.isActive()) {
			gameOverScreen.show();
		}
	}

	private void calculateFps(long now) {
		frameCount++;
		if (now - lastFrameTime >= 1000000000) {
			FPS = frameCount;
			frameCount = 0;
			lastFrameTime = now;
		}
	}

	private void drawGameScene(GraphicsContext gc) {
//-------------------------DEBUG--------------------------------------
//    	if (gc == null) {
//    	    System.out.println("GraphicsContext is null!"); //Check if gc is working
//    }

		double cameraX = player.getX() - WIDTH / 2.0;
		double cameraY = player.getY() - HEIGHT / 2.0;

		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.save();
		gc.translate(-cameraX, -cameraY);

		// draw map
		gc.drawImage(backgroundImage, 0, 0);
		gc.setImageSmoothing(false);
		gc.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth() * 2, backgroundImage.getHeight() * 2);
		gc.drawImage(borderImageTop, 0, -400, borderImageTop.getWidth() * 2, borderImageTop.getHeight() * 2 - 16);
		gc.drawImage(borderImageBottom, 0, GAME_HEIGHT, borderImageBottom.getWidth() * 2,
				borderImageBottom.getHeight() * 2 - 16);
		gc.drawImage(borderImageRight, GAME_WIDTH, -300, borderImageRight.getWidth() * 2 - 16,
				borderImageRight.getHeight() * 2);
		gc.drawImage(borderImageLeft, -400, -300, borderImageLeft.getWidth() * 2 - 16, borderImageLeft.getHeight() * 2);

		player.draw(gc);
		enemyManager.draw(gc);

		// After drawing player and enemies:
		CollisionHandler.drawCollisionBoundaries(gc, player, enemyManager.getEnemies());

		gc.restore();
	}

	public void playerGainExp(int amount) {
		player.gainExp(amount);
//        System.out.println("Current EXP: " + player.getExp()); 
		if (Player.isLevelup) {
//            System.out.println("Level up! Showing Item Selector."); //check is levelup
		}
	}

	public void pauseGame() {
		if (gameLoop != null) {
			gameLoop.stop();
		}
		pausedTime = System.nanoTime();
		start = false;
	}

	public void resumeGame() {
		if (gameLoop != null) {
			lastTimeUpdate = System.nanoTime();
			gameLoop.start();
		}

		long pauseDuration = System.nanoTime() - pausedTime;
		startTime += pauseDuration;

		start = true;
	}

	public void playBGM() {
		if (stage instanceof AridZone) {
			BGM.playBGM(NameBGM.BGM_SUSPECT);
		} else if (stage instanceof IDUNNO) {
			BGM.playBGM(NameBGM.BGM_ADTRUCK);
		} else {
			throw new IllegalStateException("Unknown stage");
		}
	}

	public static GameScene getInstance() {
		if (instance == null) {
			instance = new GameScene(mainApp);
		}

		return instance;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public LevelUpMenu getLevelUpMenu() {
		return levelUpMenu;
	}

	public GameOverScreen getGameOverScreen() {
		return gameOverScreen;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void refreshStage() {
		stage = StageScene.getSelectedStage();
		backgroundImage = new Image(stage.getMapPath());
		borderImageTop = new Image(stage.getBorderBase().getTop());
		borderImageBottom = new Image(stage.getBorderBase().getBottom());
		borderImageLeft = new Image(stage.getBorderBase().getLeft());
		borderImageRight = new Image(stage.getBorderBase().getRight());
	}
}