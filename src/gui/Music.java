package gui;

import java.io.File;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Music extends BabaIsYouApp {

	private static String[] listOfMusic = {"musicMenu.wav"};
	private Media musicFile;
	private MediaPlayer playerMusic;
	private final double NORMAL_VOLUME = 0.2;
	
	//AUTRE
	private Timeline timeline;
	
	public Music(int musicNumber) {
		musicFile = new Media(new File("ressources"+File.separator+"music"+File.separator+listOfMusic[musicNumber]).toURI().toString());
		playerMusic = new MediaPlayer(musicFile);
		playerMusic.setVolume(0.2);
		playerMusic.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void play() {
		if(timeline != null) { //Corrige un bug qui lorsque le timer était en cours d'exécution et qu'on revenait au menu, la musique ne se lançait pas
			timeline.stop();
			playerMusic.setVolume(NORMAL_VOLUME);
		}
		if(!playerMusic.getStatus().equals(Status.PLAYING) && (boolean)settings.get("MUSIC")==true) {
			playerMusic.setVolume(NORMAL_VOLUME);
			playerMusic.play();
		}
	}
	
	public void stop() {
		if(playerMusic.getStatus().equals(Status.PLAYING)) {
			playerMusic.stop();
		}
	}
	
	public void setVolumeON() {
		playerMusic.setVolume(NORMAL_VOLUME);
		play();
	}
	
	public void setVolumeOFF() {
		playerMusic.setVolume(0);
		stop();
	}
	
	/**
	 * Le but de cette méthode est de crée un fade dans la musique pour que les transitions soient
	 * plus agréables
	 */
	public void fadeVolume() {
		if(timeline != null)
			timeline.stop(); //Permet de corriger un bug assez spécial qui faisait appel à la fonction fadeVolume() alors qu'il était toujours
							// en cours d'exécution
		//On crée un timer qui va permettre de réduire le volume de la musique toutes les 15ms
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(15),
		        ae -> {
		        	playerMusic.setVolume(playerMusic.getVolume()-0.001); // Diminution du volume
		    		if(playerMusic.getVolume()<0) { // Si le volume est inférieur à 0  on arrête le timer et on réinitialise le volume
		    			timeline.stop(); // arrêt de la timeline
		    			stop();
		    		}
		        }));
		timeline.setCycleCount(Animation.INDEFINITE); // On remet le timer à zéro (on crée en quelque sorte une boucle)
		timeline.play();
	}
}
