package userProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

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
	 * Méthode qui va changer un raccourci clavier par le choix de l'utilisateur
	 * 
	 * @param key
	 *            le raccourci à modifier
	 * @param value
	 *            le nouveau raccourci
	 *            
	 */
	public boolean changeSettings(String key, String value) {
		if (userSettings.get(value) != null) {
			return false; //Signifie que le raccourci est déjà utilisé

		} else {
			userSettings.remove(userSettings.get(key).toString());
			userSettings.put(value, key);
			userSettings.replace(key, value);
			return true;
		}
	}
	
	public void replaceSound(String key, Boolean trueOrFalse) {
		soundSettings.replace(key, trueOrFalse);
	}
	
	/**
	 * Méthode qui va se charger de fermer l'application tout en enregistrant les
	 * paramètres
	 */
	public void close() {
		try {
			Gson gson = new Gson();
			FileWriter file = new FileWriter("settings" + File.separator + "UserSettings.json");
			file.write(gson.toJson(this));
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

