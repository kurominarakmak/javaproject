package utils.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DiagonalLines {
    private Pane root;
    private Canvas canvas;
    private GraphicsContext gc;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int LINE_WIDTH = 4;
    private static final int LINE_SPACING = 32;
    private static final double SPEED = 0.3;
   

    private double offsetX = 0;

    public DiagonalLines(Pane root) {
        this.root = root;
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        startAnimation();
    }

    private void startAnimation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
                offsetX -= SPEED;

                if (offsetX <= -LINE_SPACING) {
                    offsetX = 0;
                }
            }
        };
        animationTimer.start();
    }

    private void draw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(LINE_WIDTH);

        for (int x = -WIDTH; x < WIDTH; x += LINE_SPACING) {
        	gc.strokeLine(x + offsetX, HEIGHT, x + HEIGHT + offsetX, 0);
        }
    }
}
