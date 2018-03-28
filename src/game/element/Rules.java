package game.element;

import java.util.ArrayList;
/**
 * Classe gérant l'entiereté des règles du jeu
 * Se base sur ce principe : Une méthode qui analalyse tout le plateau et ajoute toutes les règles en cours dans une liste 
 * Ensuite la classe item va s'occuper de définir une méthode ex isStop{return name+"isStop" is in liste)
 */
public class Rules {
	
	private static ArrayList<RuleItem[]> listOfRulesActives;
	
	public static ArrayList<RuleItem[]> getListOfRulesActives() {
		return listOfRulesActives;
	}

	/**
	 * Méthode qui va analyser le plateau de jeu pour sortir les règles actives.
	 * @param board Le plateau à l'état actuel.
	 */
	public static void scanRules(Board board)
	{
		//TODO
	}
}
