package game.boardController;

import java.util.ArrayList;

import game.element.IRule;
import game.element.Item;

/**
 * Classe gérant l'entiereté des règles du jeu Se base sur ce principe : Une
 * méthode qui analalyse tout le plateau et ajoute toutes les règles en cours
 * dans une liste Ensuite la classe item va s'occuper de définir une méthode ex:
 * isStop qui va regarder dans la liste de règle si l'Item "STOP"
 */
public class Rules {

	private static ArrayList<IRule[]> listOfRulesActives;
	private static IRule[] ruleToAdd;

	public static ArrayList<IRule[]> getListOfRulesActives() {
		return listOfRulesActives;
	}

	/**
	 * Méthode qui va analyser la map pour sortir les règles actives.
	 * 
	 * @param board
	 *            La map.
	 */
	public static ArrayList<IRule[]> scanRules(ArrayList<Board> boards) {
		listOfRulesActives = new ArrayList<>();
		Cell[][] array;
		for (Board oneBoard : boards) {
			array = oneBoard.getBoard();
			int rows = array.length; // On récupère le nombre de lignes et de colonnes de la map
			int cols = array[0].length;
			/*
			 * Itération sur chaque Cellule du tableau
			 */
			for (int i = 0; i < cols; i++) {
				for (int j = 0; j < rows; j++) {
					// Chaque cellule est composé d'une liste d'Item
					ArrayList<Item> list = array[i][j].getList(); // On récupère la liste d'Item
					// Itération sur la liste d'Item
					for (Item element : list) {
						if (element instanceof IRule) // Si l'élement implémente IRule alors c'est que c'est un mot qui
														// peut servir à créer une règle
						{
							IRule iRuleElement = (IRule) element;
							if (iRuleElement.isWord()) // Si l'élement est un nom commun alors c'est peut être le début
														// d'une règle
							{
								ruleToAdd = new IRule[3];
								ruleToAdd[0] = iRuleElement;
								if (i <= cols - 2) {
									scanInOneDirection(array, j, i, "RIGHT"); // Ajout de la règle horizontal si elle
																				// existe
								}
								// On doit de nouveau instancier une nouvelle règle au cas où ScanRight en a
								// déjà trouvé une
								ruleToAdd = new IRule[3];
								ruleToAdd[0] = iRuleElement;
								if (j <= rows - 2) {
									scanInOneDirection(array, j, i, "DOWN"); // Ajout de la règle vertical si elle
																				// existe
								}
							}
						}
					}
				}
			}
			oneBoard.scanRules(); // On applique les nouvelles règles à chaque board
		}
		return listOfRulesActives;
	}

	/**
	 * Méthode qui va analyser les cellules à droite de la cellule RuleItem Si la
	 * première cellule à sa droite est un verbe, la méthode va analyser la cellule
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
		// Itération sur la liste d'Item de la cellule à droite
		for (Item element1 : array[y][x].getList()) {
			if (element1 instanceof IRule) // Si l'élement implémente IRule alors c'est que c'est un mot qui peut servir
											// à créer une règle
			{
				IRule iRuleElement1 = (IRule) element1;
				if (iRuleElement1.isVerb()) // Si l'élement est un verbe on regarde si la cellule à droite est un mot
											// pour finir la règle
				{
					if (direction.equals("RIGHT"))
						x = x + 1;
					else if (direction.equals("DOWN"))
						y = y + 1;
					for (Item element2 : array[y][x].getList()) {
						if (element2 instanceof IRule)// Si l'élement implémente IRule alors c'est que c'est un mot qui
														// peut servir à créer une règle
						{
							IRule iRuleElement2 = (IRule) element2;
							// Si l'élément est un mot ou ue action on a une nouvelle règle
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
