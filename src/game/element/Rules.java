package game.element;

import java.util.ArrayList;
/**
 * Classe g�rant l'entieret� des r�gles du jeu
 * Se base sur ce principe : Une m�thode qui analalyse tout le plateau et ajoute toutes les r�gles en cours dans une liste 
 * Ensuite la classe item va s'occuper de d�finir une m�thode ex isStop{return name+"isStop" is in liste)
 */
public class Rules {
	
	private static ArrayList<RuleItem[]> listOfRulesActives;
	
	public static ArrayList<RuleItem[]> getListOfRulesActives() {
		return listOfRulesActives;
	}

	/**
	 * M�thode qui va analyser le plateau de jeu pour sortir les r�gles actives.
	 * @param board Le plateau � l'�tat actuel.
	 */
	public static void scanRules(Board board)
	{
		//TODO
	}
}
