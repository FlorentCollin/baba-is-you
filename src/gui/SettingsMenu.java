package gui;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import userProfile.SettingsInit;

public class SettingsMenu extends Menu {
	// SETTINGS MENU
	@FXML
	private ImageView upKey;
	@FXML
	private ImageView restartKey;
	@FXML
	private ImageView nextWorldKey;
	@FXML
	private ImageView saveKey;
	@FXML
	private ImageView downKey;
	@FXML
	private ImageView rightKey;
	@FXML
	private ImageView musicOff;
	@FXML
	private ImageView previousWorldKey;
	@FXML
	private ImageView nextWorldModKey;
	@FXML
	private ImageView loadSaveKey;
	@FXML
	private ImageView musicOn;
	@FXML
	private ImageView soundOn;
	@FXML
	private ImageView leftKey;
	@FXML
	private ImageView soundOff;
	@FXML
	private Text resetText;
	@FXML
	private Text closeButton;

	// METHODES

	@FXML
	public static void loadSettingsMenu() {
		Parent root = null;
		// if(! playerMusic.getStatus().equals(Status.PLAYING)) {
		// playerMusic.play();
		// }

		// Chargement du fichier @FXML qui représente le menu des paramtres
		try {
			root = FXMLLoader.load(thisClass.getResource("settingsMenu.fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);

		menu.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode().toString().equals("ESCAPE"))
				loadMenu();
		});
	}

	/**
	 * Méthode qui va s'occuper de la gestion de l'interface lorsque l'utilisateur veut changer un raccourci
	 * 
	 * @param key
	 *            le raccourci à modifier
	 * @param newValue
	 *            le nouveau raccourci
	 * @throws IOException
	 *             Au cas où le fichier json n'est pas trouvé
	 */
	//TODO COMMENTER
	public static void changeSettings(String key, ImageView keyImage)  {
		keyImage.setOpacity(1);
		menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Image imageToLoad = new Image(
						"file:ressources" + File.separator + "Key_images" + File.separator + event.getCode() + ".png");
				if (!imageToLoad.isError()) {
					if (settings.changeSettings(key, event.getCode().toString()))
						keyImage.setImage(imageToLoad);
					else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("WARNING");
						alert.setHeaderText("This shortcut is already used, please choose an other one.");
						alert.show();
					}

				}
				menu.setOnKeyPressed((KeyEvent e) -> {
					if (e.getCode().toString().equals("ESCAPE"))
						loadMenu();
				});
				keyImage.setOpacity(0.5);
			}
		});
	}

	@FXML
	public void setSettingsImages() { // TODO A modifier pour rendre cette méthode modulaire
		upKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("up") + ".png"));
		downKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("down") + ".png"));
		rightKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("right") + ".png"));
		leftKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("left") + ".png"));
		restartKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("restart") + ".png"));
		saveKey.setImage(new Image(
				"file:ressources" + File.separator + "Key_images" + File.separator + settings.getUserSettings().get("save") + ".png"));
		loadSaveKey.setImage(new Image("file:ressources" + File.separator + "Key_images" + File.separator
				+ settings.getUserSettings().get("load_save") + ".png"));
		nextWorldKey.setImage(new Image("file:ressources" + File.separator + "Key_images" + File.separator
				+ settings.getUserSettings().get("next_world") + ".png"));
		nextWorldModKey.setImage(new Image("file:ressources" + File.separator + "Key_images" + File.separator
				+ settings.getUserSettings().get("next_world_mod") + ".png"));
		previousWorldKey.setImage(new Image("file:ressources" + File.separator + "Key_images" + File.separator
				+ settings.getUserSettings().get("previous_world") + ".png"));
		if ((boolean) settings.getSoundSettings().get("MUSIC")) {
			musicOn.setOpacity(1);
			musicOff.setOpacity(0.5);
		} else {
			musicOn.setOpacity(0.5);
			musicOff.setOpacity(1);
		}
		if ((boolean) settings.getSoundSettings().get("SOUNDFX")) {
			soundOn.setOpacity(1);
			soundOff.setOpacity(0.5);
		} else {
			soundOn.setOpacity(0.5);
			soundOff.setOpacity(1);
		}
	}

	@FXML
	public void upPressed() {
		changeSettings("up", upKey);
	}

	@FXML
	public void downPressed() {
		changeSettings("down", downKey);
	}

	@FXML
	public void rightPressed() {
		changeSettings("right", rightKey);
	}

	@FXML
	public void leftPressed() {
		changeSettings("left", leftKey);
	}

	@FXML
	public void restartPressed() {
		changeSettings("restart", restartKey);
	}

	@FXML
	public void nextWorldPressed() {
		changeSettings("next_world", nextWorldKey);

	}

	@FXML
	public void previousWorldPressed() {
		changeSettings("previous_world", previousWorldKey);
	}

	@FXML
	public void nextWorldModPressed() {
		changeSettings("next_world_mod", nextWorldModKey);
	}

	@FXML
	public void savePressed() {
		changeSettings("save", saveKey);
	}

	@FXML
	public void loadSavePressed() {
		changeSettings("load_save", loadSaveKey);
	}

	@FXML
	public void musicOnPressed() {
		musicOn.setOpacity(1);
		musicOff.setOpacity(0.5);
		settings.replaceSound("MUSIC", true);
		musicMenu.setVolumeON();
	}

	@FXML
	public void musicOffPressed() {
		musicOff.setOpacity(1);
		musicOn.setOpacity(0.5);
		settings.replaceSound("MUSIC", false);
		musicMenu.setVolumeOFF();
	}

	@FXML
	public void soundOnPressed() {
		soundOn.setOpacity(1);
		soundOff.setOpacity(0.5);
		settings.replaceSound("SOUNDFX", true);
		SoundFX.setVolumeON();
	}

	@FXML
	public void soundOffPressed() {
		soundOff.setOpacity(1);
		soundOn.setOpacity(0.5);
		settings.replaceSound("SOUNDFX", false);
		SoundFX.setVolumeOFF();
	}

	@FXML
	public void resetTextClicked() {
		settings = SettingsInit.init(true); // On écrase les settings 
	}

	public void resetTextMouseEntered() {
		resetText.setOpacity(1);
	}

	public void resetTextMouseExited() {
		resetText.setOpacity(0.5);
	}

	@FXML
	public void closeButtonMouseClicked() {
		Menu.loadMenu();
	}

	@FXML
	public void closeButtonMouseEntered() {
		closeButton.setOpacity(1);
	}

	@FXML
	public void closeButtonMouseExited() {
		closeButton.setOpacity(0.5);
	}
}
