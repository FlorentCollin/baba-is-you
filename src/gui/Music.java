package gui;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Music extends BabaIsYouApp {

	private static String[] listOfMusic = {"musicMenu.mp3"};
	private Media musicFile;
	private MediaPlayer playerMusic;
	
	public Music(int musicNumber) {
		musicFile = new Media(new File("ressources"+File.separator+"music"+File.separator+listOfMusic[musicNumber]).toURI().toString());
		playerMusic = new MediaPlayer(musicFile);
		playerMusic.setVolume(0.2);
		playerMusic.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void play() {
		if(/*! playerMusic.getStatus().equals(Status.PLAYING) &&*/ (boolean)settings.get("MUSIC")==true) {
		playerMusic.play();
		}
	}
	
	public void stop() {
		if(playerMusic.getStatus().equals(Status.PLAYING)) {
			playerMusic.stop();
		}
	}
	
	public void setVolumeON() {
		playerMusic.setVolume(0.2);
		play();
	}
	
	public void setVolumeOFF() {
		playerMusic.setVolume(0);
		stop();
	}
}
