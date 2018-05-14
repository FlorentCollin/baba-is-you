package userProfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Success {
	
	private static Map<String, ArrayList<Object>> success = new HashMap<>();
	
	public static Map<String, ArrayList<Object>> getSuccess() {
		return success;
	}

	public static void setSuccess(Map<String, ArrayList<Object>> success) {
		Success.success = success;
	}

	@SuppressWarnings("unchecked") // Comme c'est un type générique on ne peut pas utiliser Map<String, ArrayList<Object>>.class
	public Success() { 
		Gson gson = new Gson();
		File file = new File("settings"+File.separator+"UserSuccess.json");
		if(! file.exists()) {
			file = new File("settings"+File.separator+"UserSuccessReset.json");
		}
		try {
			setSuccess(gson.fromJson(new FileReader(file), Map.class));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isUnlocked(String name) {
		if(success.get(name) == null) {
			return true;
		} 
		else {
			return (boolean) success.get(name).get(0);
		}
	}
	
	public boolean unlock(String name) {
		if(success.get(name) == null) {
			return false;
		}
		if((boolean)success.get(name).get(0) == true) {
			return false;
		}
		ArrayList<Object> newProprieties = success.get(name);
		newProprieties.set(0, true);
		success.replace(name, newProprieties);
		return true;
	}
	
	/**
	 * Méthode qui va se charger de fermer l'application tout en enregistrant les
	 * succès
	 */
	public void close() {
		try {
			Gson gson = new Gson();
			FileWriter file = new FileWriter("settings" + File.separator + "UserSuccess.json");
			file.write(gson.toJson(success));
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
