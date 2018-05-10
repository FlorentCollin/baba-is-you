package userProfile;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private Map<String, String> userSettings = new HashMap<>();
	private Map<String, Boolean> soundSettings = new HashMap<>(); 

	public Map<String, String> getUserSettings() {
		return userSettings;
	}
	
	public Map<String, Boolean> getSoundSettings() {
		return soundSettings;
	}

	public Settings() {
		userSettings.put("Z", "up");
		userSettings.put("S", "down");
		userSettings.put("D", "right");
		userSettings.put("Q", "left");
		userSettings.put("R", "restart");
		userSettings.put("X", "save");
		userSettings.put("L", "load_save");
		userSettings.put("E", "next_world");
		userSettings.put("A", "previous_world");
		userSettings.put("SPACE", "next_world_mod");
		userSettings.put("P", "previous_level");
		userSettings.put("N", "next_level");
		userSettings.put("up", "Z");
		userSettings.put("down", "S");
		userSettings.put("right", "D");
		userSettings.put("left", "Q");
		userSettings.put("restart", "R");
		userSettings.put("save", "X");
		userSettings.put("load_save", "L");
		userSettings.put("next_world", "E");
		userSettings.put("previous_world", "A");
		userSettings.put("next_world_mod", "SPACE");
		userSettings.put("previous_level", "P");
		userSettings.put("next_level", "N");
		userSettings.put("F1", "F1");
		userSettings.put("ESCAPE", "ESCAPE");
		
		soundSettings.put("MUSIC", true);
		soundSettings.put("SOUNDFX", true);
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
}

