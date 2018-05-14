package gui;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Classe gérant les sons (différents des musiques)
 * @author Florent
 *
 */
public class SoundFX extends BabaIsYouApp {
	private static File file;
	private static Media soundMedia;
	private static MediaPlayer soundPlayer;
	private static boolean soundOn = true;

	/**
	 * Met à jour la variable contenant le choix de l'utilisateur
	 * @param trueOrFalse true si l'utilisateur veut les sons, false sinon
	 */
	public static void setSound(boolean trueOrFalse) {
		soundOn = trueOrFalse;
	}

	/**
	 * Crée un MediaPlayer avec le son en paramètre et joue le son
	 * @param soundName le son à jouer
	 * @return
	 */
	public static MediaPlayer play(String soundName) {
		if (soundOn) {
			file = new File("ressources" + File.separator + "soundFX" + File.separator + soundName);
			soundMedia = new Media(file.toURI().toString());
			soundPlayer = new MediaPlayer(soundMedia);
			soundPlayer.setVolume(0.2);
			soundPlayer.play();
			return soundPlayer;
		}
		return null;
	}

	public static boolean isSoundOn() {
		return soundOn;
	}

	public static void setVolumeON() {
		soundOn = true;
	}

	public static void setVolumeOFF() {
		soundOn = false;
	}
}
