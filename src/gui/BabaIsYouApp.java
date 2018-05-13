package gui;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import userProfile.Settings;
import userProfile.SettingsInit;
import userProfile.Success;

/**
 * Classe principale qui va gérer le jeu en mode interface graphique.
 * 
 * @author Florent Collin
 */
public class BabaIsYouApp extends Application {

	// Fenêtre principale
	protected static Stage primaryStage;
	protected static Stage secondaryStage = new Stage();
	protected static Class<?> thisClass;
	protected static Group root = new Group();
	protected static Font fontMadness; // Police d'écriture
	// AUTRE
	protected static Settings settings;
	public static Success success;
	protected static Music musicMenu = new Music(0);
	protected static SoundFX sound;
	protected final static String SELECTED_SOUND = "selected.wav";
	protected static Alert alert = new Alert(AlertType.ERROR);

	/**
	 * Méthode principale de l'application
	 */
	@Override
	public void start(Stage Stage) throws Exception {
		primaryStage = Stage;
		thisClass = getClass(); // On en a besoin d'en d'autres méthodes et on ne peut pas y accéder par un
								// getClass()
		primaryStage.setTitle("BABA IS YOU");
		primaryStage.getIcons().add(new Image("file:ressources" + File.separatorChar + "baba.png"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		secondaryStage.initStyle(StageStyle.UNDECORATED);

		// Changement de la police d'écriture en 8_Bit_Madness qui est une police
		// d'écriture gratuite d'utilisation sauf pour une utilisation commerciale
		fontMadness = Font.loadFont(
				"file:ressources" + File.separatorChar + "fonts" + File.separatorChar + "8-Bit Madness.ttf", 21);
		if (fontMadness == null)
			throw new IOException("Custom font was not find");

		primaryStage.setOnCloseRequest((WindowEvent event) -> {
			close();
		});
		init();
		// Chargement du Menu principal
		Menu.loadMenu();

		// Ouverture de l'application
		primaryStage.show();
	}

	/**
	 * Méthode qui va initialiser ce que l'applicationà besoin pour fonctionner
	 * Cette méthode ne sera utilisé que lors de l'appel de la fonction "start"
	 */
	public void init() {
		settings = SettingsInit.init();
		success = new Success();
		sound = new SoundFX();
	}



	/**
	 * Méthode qui va se charger de fermer l'application tout en enregistrant les
	 * paramètres
	 */
	protected static void close() {
		settings.close();
		success.close();
		
		primaryStage.close();
		secondaryStage.close();
	}

	/**
	 * Méthode qui va ouvrir un explorateur permettant ainsi à l'utilisateur de
	 * choisir un fichier .txt contenant un niveau
	 * 
	 * @return le fichier choisi par l'utilisisateur ou null si c'est un fichier
	 *         nécessaire au bon fonctionnement du jeu
	 */
	protected static File FileChooserLvl() {
		// On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le
		// niveau qu'il veut charger dans l'éditeur de niveau
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose level to load :");
		// Le code ci-dessous provient de :
		// "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
		// Il permet de définir les extensions de fichier autorisées. Ici, on n'autorise
		// que les fichiers textes (".txt")
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".txt", "*.txt");
		fc.getExtensionFilters().add(fileExtensions);
		fc.setInitialDirectory(new File("levels" + File.separator + "editor")); // On définit l'endroit où le
																				// FileChooser va s'ouvrir
		File fileChoose = fc.showOpenDialog(primaryStage); // Ouverture de l'explorateur
		if (fileChoose != null) {
			// Si jamais l'utilisateur choisi un fichier qu'il n'est pas sensé pouvoir
			// charger
			if (fileChoose.getName().equals("cleanEditor.txt") || fileChoose.getName().equals("testEditor.txt")) {
				// Pop up d'une erreur
				alert.setTitle("ERROR");
				alert.setHeaderText("You can't load this file");
				alert.setContentText("This file is reserved for the game engine, please choose an other file");
				alert.show();
				return null;
			}
		}
		return fileChoose;
	}

	public static void showSuccessUnlocked() {
		ImageView gif = new ImageView(new Image("file:ressources"+File.separator+"SuccessUnlockedAnimation.gif"));
		gif.setX(750);
		gif.setY(30);
		SoundFX.play("success.wav");
		root.getChildren().add(gif);
	}
	
	/**
	 * Méthode qui va charger une fenêtre "pop up" d'erreur pour indiquer des
	 * fichiers nécessaire au bon fonctionnement du jeu sont manquants
	 */
	protected void alertFilesMissing() {
		alert.setTitle("ERROR");
		alert.setHeaderText("Some files are missing");
		alert.setContentText("Some file used for the game are missing, please reinstall the game.");
		alert.show();
		primaryStage.close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
