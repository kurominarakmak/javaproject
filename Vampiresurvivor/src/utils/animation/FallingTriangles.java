package utils.animation;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.Random;

public class FallingTriangles {
    private Pane root;
    private static FallingTriangles instance;
    private Random random = new Random();
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    
    private boolean isRunning = false;
    private ParallelTransition parallelTransition;

    public FallingTriangles(Pane root) {
        this.root = root;
        startFallingTriangles(7); // ✅ เริ่มต้นให้ตก 5 อัน
    }

    public void startFallingTriangles(int count) {
    	if (isRunning) return;
        isRunning = true;
    	
        for (int i = 0; i < count; i++) {
            createFallingTriangle();
        }
    }

    private void createFallingTriangle() {
        Image triangleImage = new Image("file:res/images/obj/triangle_while.png");
        ImageView triangle = new ImageView(triangleImage);
        
        int randomSize = random.nextInt(32, 96);
        triangle.setFitWidth(randomSize);
        triangle.setFitHeight(randomSize);
        
        double startX = 50 + Math.pow(random.nextDouble(), 1.5) * (WIDTH - 2 * 50);
        double startY = -50;
        triangle.setX(startX);
        triangle.setY(startY);
        root.getChildren().add(triangle);
        
        double randomFactor = Math.pow(random.nextDouble(1, 5), 1.5);

        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(triangle);
        translateTransition.setFromY(startY);
        translateTransition.setToY(HEIGHT);
        translateTransition.setDuration(Duration.seconds(randomFactor * 1));
        translateTransition.setInterpolator(Interpolator.LINEAR);

        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setNode(triangle);
        rotateTransition.setByAngle(360); // หมุน 360 องศา
        rotateTransition.setDuration(Duration.seconds(randomFactor * 1)); // หมุนเร็ว-ช้าแบบสุ่ม
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE); // หมุนไปเรื่อย ๆ
        rotateTransition.setInterpolator(Interpolator.LINEAR);

        ParallelTransition parallelTransition = new ParallelTransition(triangle, rotateTransition, translateTransition);


        
        translateTransition.setOnFinished(e -> {
        	root.getChildren().remove(triangle);
        	createFallingTriangle();
        });
        

        parallelTransition.play();
    }
    
    public void start() {
        isRunning = true;
        if (parallelTransition != null) {
        	parallelTransition.play();
        }
    }
    
    public void stop() {
        isRunning = false;
        if (parallelTransition != null) {
            parallelTransition.stop();
        }
    }

}

