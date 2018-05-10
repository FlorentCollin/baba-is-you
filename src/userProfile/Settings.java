package userProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Settings {

	private BiMap<String, String> userSettings = HashBiMap.create();

	public BiMap<String, String> getUserSettings() {
		return userSettings;
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
		userSettings.put("SPACE", "next_world_mode");
		userSettings.put("P", "previous_level");
		userSettings.put("N", "next_level");
		userSettings.put("F1", "F1");
		userSettings.put("ESCAPE", "ESCAPE");
		// userSettings.put("MUSIC", "true");
		// userSettings.put("SOUNFX", "true");
	}

}
