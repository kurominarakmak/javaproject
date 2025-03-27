package scene;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class StartGameScreen {
    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void startGame() {
        active = false;
    }

    public void render(GraphicsContext gc, int width, int height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height); 

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(40));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Press ENTER to Start", width / 2.0, height / 2.0);
    }
}
