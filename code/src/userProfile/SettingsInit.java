package userProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Classe qui va s'occuper de charger les paramètres de l'utilisateur
 * @author Florent
 *
 */
public class SettingsInit {
	/**
	 * Chargement des paramètres en fonction des fichiers présent dans le répertoire "settings".
	 * 
	 */
	public static Settings init(boolean newSettings) {
		// Ouvertue du fichier JSON contenant les raccourcis clavier de l'utilisateur
		File settingsJson = new File("code" + File.separator + "settings" + File.separator + "UserSettings.json");
		// Si ce fichier n'existe pas on charge les paramètres par défaut
		if (!settingsJson.exists() || newSettings) {
			settingsJson = new File("code" + File.separator + "settings" + File.separator + "UserSettingsReset.json");
		}
		Gson gson = new Gson(); //Deserialisation
		try {
			return gson.fromJson(new FileReader(settingsJson), Settings.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Some files are missing. Please reinstall the game");
			e.printStackTrace();
		}
		return null;
	}
}
