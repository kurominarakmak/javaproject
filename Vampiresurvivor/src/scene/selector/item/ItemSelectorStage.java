package scene.selector.item;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import player.base.Player;

public class ItemSelectorStage extends Stage {
    private static ItemSelectorStage instance;
    private Runnable onCloseCallback; // ใช้ callback เมื่อปิดหน้าต่าง

    private ItemSelectorStage(Player player) {
        super();
        this.setResizable(true);
        this.setTitle("Items Selector Vampire Rhythm");
        this.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("items/IconSet.png")));
        Scene scene = new Scene(ItemSelectorPane.getInstance(player));
        this.setScene(scene);

        // ถ้าปิดหน้าต่างแล้ว ให้เรียก callback
        this.setOnHidden(event -> {
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });
    }

    public static ItemSelectorStage getInstance(Player player) {
        if (instance == null) {
            instance = new ItemSelectorStage(player);
        }
        ItemSelectorPane.getInstance(player).refreshItem();
        return instance;
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }
}