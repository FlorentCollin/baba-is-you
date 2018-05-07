package gui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class Settings extends Menu{
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
	   
	    //METHODES
	    
	    @FXML
		public static void loadSettingsMenu() {
			Parent root = null; 
//			if(! playerMusic.getStatus().equals(Status.PLAYING)) {
//				playerMusic.play();
//			}
			
			//Chargement du fichier @FXML qui représente le menu des paramtres
			try {
				root = FXMLLoader.load(thisClass.getResource("settingsMenu.fxml"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			menu = new Scene(root, 960, 960);
			primaryStage.setScene(menu);
			
			menu.setOnKeyPressed((KeyEvent event) -> {
				if(event.getCode().toString().equals("ESCAPE"))
					loadMenu();
			});

		}
	    
	    /**
	     * Méthode qui va changer un raccourci clavier par le choix de l'utilisateur
	     * @param key le raccourci à modifier
	     * @param newValue le nouveau raccourci
	     * @throws IOException Au cas où le fichier json n'est pas trouvé
	     */
	    @SuppressWarnings("unchecked")
	    public static void changeSettings(String key, ImageView keyImage) throws IOException {
	    	keyImage.setOpacity(1);
	    	menu.setOnKeyPressed(new EventHandler<KeyEvent>() {
	    		@Override
	    		public void handle(KeyEvent event) {
	    			Image imageToLoad = new Image("file:ressources"+File.separator+"Key_images"+File.separator+event.getCode()+".png");
	    			if(!imageToLoad.isError()) {
	    				keyImage.setImage(imageToLoad);
	    				settings.remove(settings.get(key).toString());
	    				settings.put(event.getCode().toString(), key);
	    				settings.replace(key, event.getCode().toString());
	    				
	    				menu.setOnKeyPressed((KeyEvent e) -> {
	    					if(e.getCode().toString().equals("ESCAPE"))
	    						loadMenu();
	    				});
	    			}
	    			keyImage.setOpacity(0.5);
	    		}
	    	});
	    }
	    
	@FXML
	public void setSettingsImages() {
		upKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("up")+".png"));
		downKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("down")+".png"));
		rightKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("right")+".png"));
		leftKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("left")+".png"));
		restartKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("restart")+".png"));
		saveKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("save")+".png"));
		loadSaveKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("load_save")+".png"));
		nextWorldKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("next_world")+".png"));
		nextWorldModKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("next_world_mod")+".png"));
		previousWorldKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("previous_world")+".png"));
		if((boolean) settings.get("MUSIC")) { musicOn.setOpacity(1); musicOff.setOpacity(0.5);}
		else { musicOn.setOpacity(0.5); musicOff.setOpacity(1);}
		if((boolean) settings.get("SOUNDFX")) { soundOn.setOpacity(1); soundOff.setOpacity(0.5);}
		else { soundOn.setOpacity(0.5); soundOff.setOpacity(1);}
	}
	@FXML
	public void upPressed() {
		try {
			changeSettings("up", upKey);
		} catch (IOException e) {
			throw new RuntimeException(e); // Ici cela n'a pas d'importance car si "upkey" n'a pas été chargé on aurait déjà eu une erreur plus tôt
		}
	}
	
	@FXML
	public void downPressed() {
		try {
			changeSettings("down", downKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void rightPressed() {
		try {
			changeSettings("right", rightKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void leftPressed() {
		try {
			changeSettings("left", leftKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void restartPressed() {
		try {
			changeSettings("restart", restartKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void nextWorldPressed() {
		try {
			changeSettings("next_world", nextWorldKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void previousWorldPressed() {
		try {
			changeSettings("previous_world", previousWorldKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void nextWorldModPressed() {
		try {
			changeSettings("next_world_mod", nextWorldModKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void savePressed() {
		try {
			changeSettings("save", saveKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@FXML
	public void loadSavePressed() {
		try {
			changeSettings("load_save", loadSaveKey);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void musicOnPressed() {
		musicOn.setOpacity(1);
		musicOff.setOpacity(0.5);
		settings.replace("MUSIC", true);
		musicMenu.setVolumeON();
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void musicOffPressed() {
		musicOff.setOpacity(1);
		musicOn.setOpacity(0.5);
		settings.replace("MUSIC", false);
		musicMenu.setVolumeOFF();
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void soundOnPressed() {
		soundOn.setOpacity(1);
		soundOff.setOpacity(0.5);
		settings.replace("SOUNDFX", true);
		SoundFX.setVolumeON();
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void soundOffPressed() {
		soundOff.setOpacity(1);
		soundOn.setOpacity(0.5);
		settings.replace("SOUNDFX", false);
		SoundFX.setVolumeOFF();
	}
	@FXML
	public void resetTextClicked() {
		try {
			FileUtils.copyFile(new File("settings"+File.separator+"UserSettingsReset.json"), new File("settings"+File.separator+"UserSettings.json"));
		} catch (IOException e) {
			alertFilesMissing();
		}
		init(); //On recharge le fichier contenant les raccourcis car on vient de reset ceux-ci et on recharge par la même occasion la musique
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
