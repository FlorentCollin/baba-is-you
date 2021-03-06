package userProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Classe qui va g�rer les param�tres de l'utilisateur
 * @author Florent
 *
 */
public class Settings {

	private Map<String, String> userSettings = new HashMap<>();
	private Map<String, Boolean> soundSettings = new HashMap<>(); 

	public Map<String, String> getUserSettings() {
		return userSettings;
	}
	
	public Map<String, Boolean> getSoundSettings() {
		return soundSettings;
	}
	
	/**
	 * M�thode qui va changer un raccourci clavier par le choix de l'utilisateur
	 * 
	 * @param key
	 *            le raccourci � modifier
	 * @param value
	 *            le nouveau raccourci
	 *            
	 */
	public boolean changeSettings(String key, String value) {
		if (userSettings.get(value) != null) {
			return false; //Signifie que le raccourci est d�j� utilis�

		} else {
			userSettings.remove(userSettings.get(key).toString());
			userSettings.put(value, key);
			userSettings.replace(key, value);
			return true;
		}
	}
	
	/**
	 * M�thode qui va changer le choix de l'utilisateur au niveaux des sons
	 * @param key 
	 * 			  la valeur � modifier
	 * @param trueOrFalse
	 * 					le choix de l'utilisateur
	 */
	public void replaceSound(String key, Boolean trueOrFalse) {
		soundSettings.replace(key, trueOrFalse);
	}
	
	/**
	 * M�thode qui va se charger de fermer l'application tout en enregistrant les
	 * param�tres dans un fichier JSON
	 */
	public void close() {
		try {
			Gson gson = new Gson();
			FileWriter file = new FileWriter("code" + File.separator + "settings" + File.separator + "UserSettings.json");
			file.write(gson.toJson(this));
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

