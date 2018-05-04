package gui;

import java.io.File;
import java.io.IOException;

import game.levelManager.LevelManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class Menu extends BabaIsYouApp {

	protected static Scene menu;
	protected static Canvas canvas = new Canvas(960, 960);
	protected static Group root;
	protected static Text close;
	protected static Music musicMenu = new Music(0);
	
	// START MENU
	@FXML
	private ImageView cadrePlay;
	@FXML
	private ImageView cadreLoadSave;
	@FXML
	private ImageView cadreEditor;
	@FXML
	private ImageView cadreLevels;
	@FXML
	private ImageView cadreExit;
	@FXML
	private ImageView cadreSettings;
	@FXML
	private ImageView cadreSuccess;
	
	/**
	 * M�thode qui va charger le menu principal 
	 */
	public static void loadMenu() {
		Parent root = null; 
		musicMenu.play(); //D�marrage de la musique de fond

		//Chargement du fichier @FXML qui repr�sente le menu principal
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
		/*
		 * Gestionnaire d'�v�nements du clavier, si la touche ESCAPE est press�e alors on ferme l'application
		 * Sinon si la touche ENTER est press�e alors on charge le premier niveau
		 */
		menu.setOnKeyPressed((KeyEvent event) -> {
				if(event.getCode().toString().equals("ESCAPE")) {
					close(); //Fermeture de l'application
					secondaryStage.close();
				}
				else if(event.getCode().toString().equals("ENTER")) {
					Level.loadLevel(LevelManager.getListOfLevels()[0], true, true); //Chargement du premier niveau
					musicMenu.stop(); // Arr�t de la musique
				}
		});
	}
	
	@FXML
	public void playButtonClicked() {
		Level.loadLevel(LevelManager.getListOfLevels()[0], true, true);
		musicMenu.stop();
	}
	
	@FXML
	public void exitButtonClicked() {
		close();
		secondaryStage.close();
	}
	
	@FXML
	public void loadSaveButtonClicked() {
		musicMenu.stop();
		Level.loadLevel("save", true, true); //Chargement du niveau sauvegard�
	}
	
	@FXML
	public void editorButtonClicked() {
		musicMenu.stop();
		File file = new File("levels"+File.separator+"editor"+File.separator+"testEditor.txt");
		if(file.exists()) { //Si le fichier existe c'est que l'utilisateur � d�j� commenc� � composer un niveau
			String name = LevelManager.loadUserLvl(file);
			Editor.loadEditor(name, true); //On retire le ".txt"
		}
		else //On charge l'�diteur de base
			Editor.loadEditor("levels"+File.separator+"cleanEditor", true);
	}
	
	@FXML
	public void successButtonClicked() {
		//TODO
	}
	
	@FXML
	public void levelsButtonClicked() {
		LevelsChoice.loadLevelsMenu();
	}
	
	@FXML
	public void settingsButtonClicked() {
		Settings.loadSettingsMenu();
	}
	
	//Gestion des animations dans le menu principal
	@FXML
	public void cadrePlayMouseEntered() {
		cadrePlay.setOpacity(1);
	}
	
	@FXML
	public void cadrePlayMouseExited() {
		cadrePlay.setOpacity(0);
	}
	
	@FXML
	public void cadreLoadSaveMouseEntered() {
		cadreLoadSave.setOpacity(1);
	}
	
	@FXML
	public void cadreLoadSaveMouseExited() {
		cadreLoadSave.setOpacity(0);
	}
	
	@FXML
	public void cadreEditorMouseEntered() {
		cadreEditor.setOpacity(1);
	}
	
	@FXML
	public void cadreEditorMouseExited() {
		cadreEditor.setOpacity(0);
	}
	
	@FXML
	public void cadreLevelsMouseEntered() {
		cadreLevels.setOpacity(1);
	}
	
	@FXML
	public void cadreLevelsMouseExited() {
		cadreLevels.setOpacity(0);
	}
	
	@FXML
	public void cadreExitMouseEntered() {
		cadreExit.setOpacity(1);
	}
	
	@FXML
	public void cadreExitMouseExited() {
		cadreExit.setOpacity(0);
	}
	
	@FXML
	public void cadreSettingsMouseEntered() {
		cadreSettings.setOpacity(1);
	}
	
	@FXML
	public void cadreSettingsMouseExited() {
		cadreSettings.setOpacity(0);
	}
	
	@FXML
	public void cadreSuccessMouseEntered() {
		cadreSuccess.setOpacity(1);
	}
	
	@FXML
	public void cadreSuccessMouseExited() {
		cadreSuccess.setOpacity(0);
	}
}