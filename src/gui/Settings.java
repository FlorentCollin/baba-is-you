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
				e.printStackTrace();
			}
//			setSettingsImages();
			
			menu = new Scene(root, 960, 960);
			primaryStage.setScene(menu);
		
//			upKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("UP").toString()+".png"));
			
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
		upKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("UP")+".png"));
		downKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("DOWN")+".png"));
		rightKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("RIGHT")+".png"));
		leftKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("LEFT")+".png"));
		restartKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("RESTART")+".png"));
		saveKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("SAVE")+".png"));
		loadSaveKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("LOAD_SAVE")+".png"));
		nextWorldKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("NEXT_WORLD")+".png"));
		nextWorldModKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("NEXT_WORLD_MOD")+".png"));
		previousWorldKey.setImage(new Image("file:ressources"+File.separator+"Key_images"+File.separator+settings.get("PREVIOUS_WORLD")+".png"));
		if((boolean) settings.get("MUSIC")) { musicOn.setOpacity(1); musicOff.setOpacity(0.5);}
		else { musicOn.setOpacity(0.5); musicOff.setOpacity(1);}
		if((boolean) settings.get("SOUNDFX")) { soundOn.setOpacity(1); soundOff.setOpacity(0.5);}
		else { soundOn.setOpacity(0.5); soundOff.setOpacity(1);}
	}
	@FXML
	public void upPressed() {
		try {
			changeSettings("UP", upKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void downPressed() {
		try {
			changeSettings("DOWN", downKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void rightPressed() {
		try {
			changeSettings("RIGHT", rightKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void leftPressed() {
		try {
			changeSettings("LEFT", leftKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void restartPressed() {
		try {
			changeSettings("RESTART", restartKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void nextWorldPressed() {
		try {
			changeSettings("NEXT_WORLD", nextWorldKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void previousWorldPressed() {
		try {
			changeSettings("PREVIOUS_WORLD", previousWorldKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void nextWorldModPressed() {
		try {
			changeSettings("NEXT_WORLD_MOD", nextWorldModKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void savePressed() {
		try {
			changeSettings("SAVE", saveKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	public void loadSavePressed() {
		try {
			changeSettings("LOAD_SAVE", loadSaveKey);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void musicOnPressed() {
		musicOn.setOpacity(1);
		musicOff.setOpacity(0.5);
		settings.replace("MUSIC", true);
//			playerMusic.setVolume(0.05);
//			playerMusic.play();
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void musicOffPressed() {
		musicOff.setOpacity(1);
		musicOn.setOpacity(0.5);
		settings.replace("MUSIC", false);
//			playerMusic.setVolume(0);
//			playerMusic.stop();
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void soundOnPressed() {
		soundOn.setOpacity(1);
		soundOff.setOpacity(0.5);
		settings.replace("SOUNDFX", true);
	}
	@SuppressWarnings("unchecked")
	@FXML
	public void soundOffPressed() {
		soundOff.setOpacity(1);
		soundOn.setOpacity(0.5);
		settings.replace("SOUNDFX", false);
	}
	@FXML
	public void resetTextClicked() {
		try {
			FileUtils.copyFile(new File("settings"+File.separator+"UserSettingsReset.json"), new File("settings"+File.separator+"UserSettings.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		init(); //On recharge le fichier contenant les raccourcis car on vient de reset ceux-ci et on recharge par la même occasion la musique
	}
	public void resetTextMouseEntered() {
		resetText.setOpacity(1);
	}
	public void resetTextMouseExited() {
		resetText.setOpacity(0.5);
	}
}
