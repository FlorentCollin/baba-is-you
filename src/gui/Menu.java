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
	 * Méthode qui va charger le menu principal
	 */
	public static void loadMenu() {
		Parent root = null;
		musicMenu.play(); // Démarrage de la musique de fond

		// Chargement du fichier @FXML qui représente le menu principal
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			primaryStage.close(); // Si le fichier du menu n'existe pas on fait crash l'application car rien ne
									// fonctionnerai.
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
		/*
		 * Gestionnaire d'évènements du clavier, si la touche ESCAPE est pressée alors
		 * on ferme l'application Sinon si la touche ENTER est pressée alors on charge
		 * le premier niveau
		 */
		menu.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode().toString().equals("ESCAPE")) {
				close(); // Fermeture de l'application
				secondaryStage.close();
			} else if (event.getCode().toString().equals("ENTER")) {
				Level.loadLevel(LevelManager.getListOfLevels()[0], true, true); // Chargement du premier niveau
				musicMenu.fadeVolume();
				; // Arrêt de la musique
			}
		});

	}

	// Clic sur les différents bouttons
	@FXML
	public void playButtonClicked() {
		SoundFX.play(SELECTED_SOUND);
		musicMenu.fadeVolume(); // Arrêt de la musique
		Level.loadLevel(LevelManager.getListOfLevels()[0], true, true);
	}

	@FXML
	public void exitButtonClicked() {
		close();
		secondaryStage.close();
	}

	@FXML
	public void loadSaveButtonClicked() {
		SoundFX.play(SELECTED_SOUND);
		musicMenu.fadeVolume();
		Level.loadLevel("save", true, true); // Chargement du niveau sauvegardé
	}

	@FXML
	public void editorButtonClicked() {
		SoundFX.play(SELECTED_SOUND);
		musicMenu.fadeVolume();
		File file = new File("levels" + File.separator + "editor" + File.separator + "testEditor.txt");
		if (file.exists()) { // Si le fichier existe c'est que l'utilisateur à déjà commencé à composer un
								// niveau
			String name = LevelManager.loadUserLvl(file);
			Editor.loadEditor(name, true);
		} else // On charge l'éditeur de base (sans rien)
			Editor.loadEditor("levels" + File.separator + "cleanEditor", true);
	}

	@FXML
	public void successButtonClicked() {
		// TODO
		SoundFX.play(SELECTED_SOUND);
	}

	@FXML
	public void levelsButtonClicked() {
		SoundFX.play(SELECTED_SOUND);
		LevelsChoice.loadLevelsMenu();
	}

	@FXML
	public void settingsButtonClicked() {
		SoundFX.play(SELECTED_SOUND);
		SettingsMenu.loadSettingsMenu();
	}

	// Gestion des animations dans le menu principal
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
