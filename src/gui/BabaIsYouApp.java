package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Classe principale qui va g�rer le jeu en mode interface graphique.
 * 
 * @author Florent Collin
 */
public class BabaIsYouApp extends Application {

	// Fen�tre principale
	protected static Stage primaryStage;
	protected static Stage secondaryStage = new Stage();
	protected static Class<?> thisClass;
	protected static Font fontMadness; // Police d'�criture
	// AUTRE
	protected static JSONObject settings;
	protected static Music musicMenu = new Music(0);
	protected static SoundFX sound;
	protected final static String SELECTED_SOUND = "selected.wav";
	protected static Alert alert = new Alert(AlertType.ERROR);

	/**
	 * M�thode principale de l'application
	 */
	@Override
	public void start(Stage Stage) throws Exception {
		primaryStage = Stage;
		thisClass = getClass(); // On en a besoin d'en d'autres m�thodes et on ne peut pas y acc�der par un
								// getClass()
		primaryStage.setTitle("BABA IS YOU");
		primaryStage.getIcons().add(new Image("file:ressources" + File.separatorChar + "baba.png"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		secondaryStage.initStyle(StageStyle.UNDECORATED);

		// Changement de la police d'�criture en 8_Bit_Madness qui est une police
		// d'�criture gratuite d'utilisation sauf pour une utilisation commerciale
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
	 * M�thode qui va initialiser ce que l'application� besoin pour fonctionner
	 * Cette m�thode ne sera utilis� que lors de l'appel de la fonction "start"
	 */
	public void init() {
		initJson();
		sound = new SoundFX();
	}

	/**
	 * Chargement des param�tres
	 * 
	 */
	private void initJson() {
		// Ouvertue du fichier JSON contenant les raccourcis clavier de l'utilisateur
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("settings" + File.separatorChar + "UserSettings.json"));
			settings = (JSONObject) obj; // Ouverture du fichier JSON et lecture
		} catch (FileNotFoundException e) {
			alertFilesMissing();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * M�thode qui va se charger de fermer l'application tout en enregistrant les
	 * param�tres
	 */
	protected static void close() {
		try {
			FileWriter file = new FileWriter("settings" + File.separator + "UserSettings.json");
			file.write(settings.toJSONString()); // Ecriture des param�tres dans le fichier JSON correspondant
			file.close();
		} catch (IOException e) {
			primaryStage.close();
		}
		// Fermeture de l'application
		secondaryStage.close();
		primaryStage.close();
	}

	/**
	 * M�thode qui va ouvrir un explorateur permettant ainsi � l'utilisateur de
	 * choisir un fichier .txt contenant un niveau
	 * 
	 * @return le fichier choisi par l'utilisisateur ou null si c'est un fichier
	 *         n�cessaire au bon fonctionnement du jeu
	 */
	protected static File FileChooserLvl() {
		// On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le
		// niveau qu'il veut charger dans l'�diteur de niveau
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose level to load :");
		// Le code ci-dessous provient de :
		// "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
		// Il permet de d�finir les extensions de fichier autoris�es. Ici, on n'autorise
		// que les fichiers textes (".txt")
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".txt", "*.txt");
		fc.getExtensionFilters().add(fileExtensions);
		fc.setInitialDirectory(new File("levels" + File.separator + "editor")); // On d�finit l'endroit o� le
																				// FileChooser va s'ouvrir
		File fileChoose = fc.showOpenDialog(primaryStage); // Ouverture de l'explorateur
		if (fileChoose != null) {
			// Si jamais l'utilisateur choisi un fichier qu'il n'est pas sens� pouvoir
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

	/**
	 * M�thode qui va charger une fen�tre "pop up" d'erreur pour indiquer des
	 * fichiers n�cessaire au bon fonctionnement du jeu sont manquants
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
