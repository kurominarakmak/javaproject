package audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

import java.io.File;

public class SE {
	private static final Map<NameSE, MediaPlayer> soundEffects = new HashMap<>();
	
	static {
        preloadSounds();
    }

    private static void preloadSounds() {
        for (NameSE sound : NameSE.values()) {
            try {
                String path = SE.class.getResource("/audio/se/" + sound.getFilename() + ".wav").toExternalForm();
                Media media = new Media(path);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                soundEffects.put(sound, mediaPlayer);
            } catch (Exception e) {
                System.err.println("Error loading sound: " + sound.getFilename());
                e.printStackTrace();
            }
        }
    }
    
    public static void playSE(NameSE sound) {
        MediaPlayer mediaPlayer = soundEffects.get(sound);
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.setVolume(0.8);
            mediaPlayer.play();
        } else {
            System.err.println("Sound not found: " + sound.getFilename());
        }
    }

    public static void stopSE(NameSE sound) {
        MediaPlayer mediaPlayer = soundEffects.get(sound);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

}


