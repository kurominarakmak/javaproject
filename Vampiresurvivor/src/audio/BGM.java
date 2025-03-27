//package audio;
//
//import javafx.scene.image.Image;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import java.io.File;
//import java.net.URL;
//
//public class BGM {
//	private static MediaPlayer bgmPlayer;
//	
//	public static void playBGM(NameBGM filename) {
//        stopBGM();
//
//        File file = new File("res/audio/bgm/" + filename + ".mp3");
//        
//        Media media = new Media(file.toURI().toString());
//        bgmPlayer = new MediaPlayer(media);
//        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
//        bgmPlayer.setVolume(0.5);
//        bgmPlayer.play();
//    }
//
//    public static void stopBGM() {
//        if (bgmPlayer != null) {
//            bgmPlayer.stop();
//        }
//    }
//    
//    public static void setVolumeBGM(double volume) {
//    	bgmPlayer.setVolume(volume);
//    }
//    
//}


package audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

import java.io.File;

public class BGM {
	private static final Map<NameBGM, MediaPlayer> bgmAll = new HashMap<>();
	private static MediaPlayer currentPlaying = null;
	
	static {
        preloadSounds();
    }

    private static void preloadSounds() {
        for (NameBGM sound : NameBGM.values()) {
            try {
                String path = BGM.class.getResource("/audio/bgm/" + sound.getFilename() + ".wav").toExternalForm();
                Media media = new Media(path);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                bgmAll.put(sound, mediaPlayer);
            } catch (Exception e) {
                System.err.println("Error loading sound: " + sound.getFilename());
                e.printStackTrace();
            }
        }
    }
    
    public static void playBGM(NameBGM sound) {
    	stopBGM();
    	
        MediaPlayer mediaPlayer = bgmAll.get(sound);
        if (mediaPlayer != null) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
            currentPlaying = mediaPlayer;
        } else {
            System.err.println("Sound not found: " + sound.getFilename());
        }
    }

    public static void stopBGM() {
    	if (currentPlaying != null) {
            currentPlaying.stop();
            currentPlaying = null; 
        }
    }
    
    public static void setVolumeBGM(double volume) {
    	if (currentPlaying != null) {
            currentPlaying.setVolume(volume);
        } else {
            System.err.println("No BGM is currently playing.");
        }
    }

}
