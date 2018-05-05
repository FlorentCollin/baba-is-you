package gui;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundFX extends BabaIsYouApp {
	private static File file;
	private static Media soundMedia;
	private static MediaPlayer soundPlayer;
	private static boolean soundOn = true;
	
	public static void setSound(boolean trueOrFalse) {
		soundOn = trueOrFalse;
	}
	
	public static void play(String soundName) {
		if(soundOn) {
			file = new File("ressources"+File.separator+"soundFX"+File.separator+soundName);
			soundMedia = new Media(file.toURI().toString());
			soundPlayer = new MediaPlayer(soundMedia);
			soundPlayer.setVolume(0.2);
			soundPlayer.play();
		}
	}
	
	public static void setVolumeON() {
		soundOn = true;
	}
	
	public static void setVolumeOFF() {
		soundOn = false;
	}
}
