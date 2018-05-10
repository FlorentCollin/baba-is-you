package game.boardController;

import java.util.ArrayList;

import game.element.IRule;
import game.element.Item;

/**
 * Classe g�rant l'entieret� des r�gles du jeu Se base sur ce principe : Une
 * m�thode qui analalyse tout le plateau et ajoute toutes les r�gles en cours
 * dans une liste Ensuite la classe item va s'occuper de d�finir une m�thode ex:
 * isStop qui va regarder dans la liste de r�gle si l'Item "STOP"
 */
public class Rules {

	private static ArrayList<IRule[]> listOfRulesActives;
	private static IRule[] ruleToAdd;

	public static ArrayList<IRule[]> getListOfRulesActives() {
		return listOfRulesActives;
	}

	/**
	 * M�thode qui va analyser la map pour sortir les r�gles actives.
	 * 
	 * @param board
	 *            La map.
	 */
	public static ArrayList<IRule[]> scanRules(ArrayList<Board> boards) {
		listOfRulesActives = new ArrayList<>();
		Cell[][] array;
		for (Board oneBoard : boards) {
			array = oneBoard.getBoard();
			int rows = array.length; // On r�cup�re le nombre de lignes et de colonnes de la map
			int cols = array[0].length;
			/*
			 * It�ration sur chaque Cellule du tableau
			 */
			for (int i = 0; i < cols; i++) {
				for (int j = 0; j < rows; j++) {
					// Chaque cellule est compos� d'une liste d'Item
					ArrayList<Item> list = array[i][j].getList(); // On r�cup�re la liste d'Item
					// It�ration sur la liste d'Item
					for (Item element : list) {
						if (element instanceof IRule) // Si l'�lement impl�mente IRule alors c'est que c'est un mot qui
														// peut servir � cr�er une r�gle
						{
							IRule iRuleElement = (IRule) element;
							if (iRuleElement.isWord()) // Si l'�lement est un nom commun alors c'est peut �tre le d�but
														// d'une r�gle
							{
								ruleToAdd = new IRule[3];
								ruleToAdd[0] = iRuleElement;
								if (i <= cols - 2) {
									scanInOneDirection(array, j, i, "RIGHT"); // Ajout de la r�gle horizontal si elle
																				// existe
								}
								// On doit de nouveau instancier une nouvelle r�gle au cas o� ScanRight en a
								// d�j� trouv� une
								ruleToAdd = new IRule[3];
								ruleToAdd[0] = iRuleElement;
								if (j <= rows - 2) {
									scanInOneDirection(array, j, i, "DOWN"); // Ajout de la r�gle vertical si elle
																				// existe
								}
							}
						}
					}
				}
			}
			oneBoard.scanRules(); // On applique les nouvelles r�gles � chaque board
		}
		return listOfRulesActives;
	}

	/**
	 * M�thode qui va analyser les cellules � droite de la cellule RuleItem Si la
	 * premi�re cellule � sa droite est un verbe, la m�thode va analyser la cellule
	 * qui suit pour savoir si cette cellule est un mot.
	 * 
	 * @param array
	 *            tableau de cellule
	 * @param x
	 *            position x du RuleItem
	 * @param y
	 *            position y du RuleItem
	 */
	private static void scanInOneDirection(Cell[][] array, int x, int y, String direction) {
		if (direction.equals("RIGHT"))
			x = x + 1;
		else if (direction.equals("DOWN"))
			y = y + 1;
		// It�ration sur la liste d'Item de la cellule � droite
		for (Item element1 : array[y][x].getList()) {
			if (element1 instanceof IRule) // Si l'�lement impl�mente IRule alors c'est que c'est un mot qui peut servir
											// � cr�er une r�gle
			{
				IRule iRuleElement1 = (IRule) element1;
				if (iRuleElement1.isVerb()) // Si l'�lement est un verbe on regarde si la cellule � droite est un mot
											// pour finir la r�gle
				{
					if (direction.equals("RIGHT"))
						x = x + 1;
					else if (direction.equals("DOWN"))
						y = y + 1;
					for (Item element2 : array[y][x].getList()) {
						if (element2 instanceof IRule)// Si l'�lement impl�mente IRule alors c'est que c'est un mot qui
														// peut servir � cr�er une r�gle
						{
							IRule iRuleElement2 = (IRule) element2;
							// Si l'�l�ment est un mot ou ue action on a une nouvelle r�gle
							if (iRuleElement2.isWord() || iRuleElement2.isAction() || iRuleElement2.isEffect())
							{
								ruleToAdd[1] = iRuleElement1;
								ruleToAdd[2] = iRuleElement2;
								listOfRulesActives.add(ruleToAdd);
							}
						}
					}
				}
			}
		}
	}

	public static void cleanRules() {
		listOfRulesActives = new ArrayList<>();
	}
}
