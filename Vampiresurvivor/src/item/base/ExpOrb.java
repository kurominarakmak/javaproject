package item.base;

  
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.GameScene;
import player.base.Player;

public class ExpOrb {
	private static Image img = new Image(ClassLoader.getSystemResourceAsStream("items/expDiamond.png"));
    private double x, y;
    private static final double SIZE = 10;
    private boolean collected = false;
    private static final int EXP_AMOUNT = 20;
    private GameScene gameScene;  

    public ExpOrb(double x, double y, GameScene gameScene) {
        this.x = x;
        this.y = y;
        this.gameScene = gameScene;  // เอา GameScene ไว้ใช้เรียก playerGainExp
        System.out.println("ExpOrb Created at: " + x + ", " + y);
    }

    public void update(Player player) {
        if (!collected && Math.hypot(player.getX() - x, player.getY() - y) < 20) {
            collected = true;
            gameScene.playerGainExp(EXP_AMOUNT);  
            System.out.println("ExpOrb Collected!");
        }
    }

    public void draw(GraphicsContext gc) {
        if (!collected) {
//            gc.setFill(Color.GREEN);
            gc.drawImage(img, x - SIZE / 2, y - SIZE /2,SIZE,SIZE-3);
//            gc.fillOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        }
    }

    public boolean isCollected() {
        return collected;
    }
}
