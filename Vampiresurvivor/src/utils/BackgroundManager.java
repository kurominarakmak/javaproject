package utils;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class BackgroundManager {
    public static Background createBackground(String filename) {
        Image bgImage = new Image(ClassLoader.getSystemResourceAsStream("images/bg/" + filename + ".png"), 800, 600, true, true);
        BackgroundImage bg = new BackgroundImage(
            bgImage, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER,
            new BackgroundSize(1, 1, true, true, true, true)
        );
        return new Background(bg);
    }
}