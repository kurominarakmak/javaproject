package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import player.base.Player;

public class GameStats extends Pane {
	private long startTime;
    private double elapsedTime;
    private double FPS = 0;
    
    private Label timeLabel, fpsLabel, levelLabel, killLabel;
    
    private Player player;

    public GameStats(Player player) {
        this.player = player;
        this.startTime = System.nanoTime();
        
        // layout
        timeLabel = new Label("00 : 00");
        timeLabel.getStyleClass().add("text-sub-24-stoke");
        timeLabel.setTranslateX(364);
        timeLabel.setTranslateY(32);
        
        fpsLabel = new Label("FPS: 0");
        fpsLabel.getStyleClass().add("text-sub-24-stoke");
        fpsLabel.setTranslateX(12);
        fpsLabel.setTranslateY(566);
        
        levelLabel = new Label("Lv. 1");
        levelLabel.getStyleClass().add("text-sub-24-stoke");
        levelLabel.setTranslateX(656);
        levelLabel.setTranslateY(32);
        
//        killLabel = new Label("Kill: 0");
//        killLabel.getStyleClass().add("text-sub-24-stoke");
//        killLabel.setTranslateX(654);
//        killLabel.setTranslateY(64);
        
//        getChildren().addAll(timeLabel, fpsLabel, levelLabel, killLabel);
        getChildren().addAll(timeLabel, fpsLabel, levelLabel);
    }

    public void update(long now) {
        timeLabel.setText(String.format("%02d : %02d", (int) (elapsedTime / 60), (int) (elapsedTime % 60)));
        fpsLabel.setText(String.format("FPS: %.1f", FPS));
        levelLabel.setText("Lv. " + player.getLevel());
//        killLabel.setText("Kill: ");
    }
    
    public void setFps(double fps) {
        this.FPS = fps;
    }
    
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void draw(GraphicsContext gc) {
        drawExpBar(gc);
    }

    private void drawExpBar(GraphicsContext gc) {
        double expPercent = (double) player.getExp() / player.getExpToNextLevel();
        double barWidth = 800;
        double filledWidth = barWidth * expPercent;

        gc.setFill(Color.rgb(24, 54, 132));
        gc.fillRect(0, 0, barWidth, 20);
        
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 20, barWidth, 4);

        gc.setFill(Color.rgb(91, 212, 230));
        gc.fillRect(0, 0, filledWidth, 20);
        
        gc.setFill(Color.rgb(255, 255, 255));
        gc.fillRect(0, 0, filledWidth, 3);
    }
}
