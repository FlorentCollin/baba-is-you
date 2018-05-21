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

/**
 * Classe qui va gérer l'entièreté des succès
 * @author Florent
 *
 */
public class Success {

	//Chemin d'accès
	private static final String settingsPath = "code" + File.separator + "settings" + File.separator;
	
	private static Map<String, ArrayList<Object>> success = new HashMap<>();
	
	public static Map<String, ArrayList<Object>> getSuccess() {
		return success;
	}

	public static void setSuccess(Map<String, ArrayList<Object>> success) {
		Success.success = success;
	}
	
	/**
	 * Constructeur qui va soit chargé les succès de l'utilisateur s'ils existent
	 * Sinon il va charger les succès non débloqués
	 */
	@SuppressWarnings("unchecked") // Comme c'est un type générique on ne peut pas utiliser Map<String, ArrayList<Object>>.class
	public Success() { 
		//Deserialisation
		Gson gson = new Gson();
		File file = new File(settingsPath +File.separator+"UserSuccess.json");
		if(! file.exists()) {
			file = new File(settingsPath +File.separator+"UserSuccessReset.json");
		}
		try {
			setSuccess(gson.fromJson(new FileReader(file), Map.class));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Some files are missing. Please reinstall the game.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode qui indique si un succès à été débloqué ou non
	 * @param name le nom du succès
	 * @return true si le succès a été débloqué ou s'il n'existe pas
	 * false sinon
	 */
	public boolean isUnlocked(String name) {
		if(success.get(name) == null) {
			return true;
		} 
		else {
			return (boolean) success.get(name).get(0);
		}
	}
	
	/**
	 * Méthode qui débloque un succès
	 * @param name le nom du succès
	 * @return true si le succès a été débloqué, false sinon
	 */
	public boolean unlock(String name) {
		if(success.get(name) == null) {
			return false;
		}
		if((boolean)success.get(name).get(0) == true) {
			return false;
		}
		//Changement false --> true
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
			//Serialisation
			Gson gson = new Gson();
			FileWriter file = new FileWriter(settingsPath  + File.separator + "UserSuccess.json");
			file.write(gson.toJson(success));
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
