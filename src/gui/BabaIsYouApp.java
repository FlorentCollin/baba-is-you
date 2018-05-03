package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Classe principale qui va gérer le jeu en mode interface graphique.
 * 
 */
public class BabaIsYouApp extends Application {
	
	//Fenêtre principale
	protected static Stage primaryStage;
	protected static Stage secondaryStage = new Stage();
	protected static Class<?> thisClass;
	protected static Font fontMadness; //Police d'écriture
	// AUTRE
	protected static JSONObject settings;

	
	@Override
	public void start(Stage Stage) throws Exception {
			primaryStage = Stage;
			thisClass = getClass(); // On en a besoin d'en d'autres méthodes et on ne peut pas y accéder par un getClass()
			primaryStage.setTitle("BABA IS YOU");
			primaryStage.getIcons().add(new Image("file:ressources"+File.separatorChar+"baba.png"));
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			secondaryStage.initStyle(StageStyle.UNDECORATED);
			
			//Changement de la police d'écriture en 8_Bit_Madness qui est une police d'écriture gratuite d'utilisation sauf pour une utilisation commerciale
			fontMadness = Font.loadFont("file:ressources"+File.separatorChar+"fonts"+File.separatorChar+"8-Bit Madness.ttf", 21);
			if(fontMadness == null)
				throw new IOException("Custom font was not find");

			primaryStage.setOnCloseRequest((WindowEvent event) -> {
				close();
			});
			init();
			//Chargement du Menu principal
			Menu.loadMenu();
			
			//Ouverture de l'application
			primaryStage.show();
	}
	
	public void init() {
		initJson();
//		if((boolean) settings.get("MUSIC"))
//			playerMusic.setVolume(0.05);
//		else 
//			playerMusic.setVolume(0);
//		playerMusic.setCycleCount(MediaPlayer.INDEFINITE); //Pour jouer en boucle la musique
	}
	
	private void initJson() {
		//Ouvertue du fichier JSON contenant les raccourcis clavier de l'utilisateur
		JSONParser parser = new JSONParser();
		try {
			
			Object obj = parser.parse(new FileReader("settings"+File.separatorChar+"UserSettings.json"));
			settings = (JSONObject) obj; //Ouverture du fichier JSON et lecture
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		try {
			FileWriter file = new FileWriter("settings"+File.separator+"UserSettings.json");
			file.write(settings.toJSONString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		secondaryStage.close();
		primaryStage.close();
	}
	
	public static File FileChooserLvl() {
		//On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le niveau qu'il veut charger dans l'éditeur de niveau
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose level to load :");
		// Le code ci-dessous provient de : "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
		// Il permet de définir les extensions de fichier autorisées. Ici, on n'autorise que les fichiers textes (".txt")
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".TXT", "*.txt");
		fc.getExtensionFilters().add(fileExtensions);
		fc.setInitialDirectory(new File("levels"+File.separator+"editor")); //On définit l'endroit où le FileChooser va s'ouvrir
		File fileChoose = fc.showOpenDialog(primaryStage);
		if(fileChoose != null) {
			//Code pour split le path trouvé sur : "https://stackoverflow.com/questions/1099859/how-to-split-a-path-platform-independent"
			String[] nameFileSplit = fileChoose.toString().split(Pattern.quote(File.separator));
			if(nameFileSplit[nameFileSplit.length-1].equals("cleanEditor.txt")) {
				return null;
			}
		}
		return fileChoose;
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}

