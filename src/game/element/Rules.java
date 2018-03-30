package game.element;

import java.util.ArrayList;
/**
 * Classe g�rant l'entieret� des r�gles du jeu
 * Se base sur ce principe : Une m�thode qui analalyse tout le plateau et ajoute toutes les r�gles en cours dans une liste 
 * Ensuite la classe item va s'occuper de d�finir une m�thode ex isStop{return name+"isStop" is in liste)
 */
public class Rules {
	
	private static ArrayList<IRule[]> listOfRulesActives = new ArrayList<>();
	private static IRule[] ruleToAdd;
	
	public static ArrayList<IRule[]> getListOfRulesActives() {
		return listOfRulesActives;
	}

	/**
	 * M�thode qui va analyser la map pour sortir les r�gles actives.
	 * @param board La map.
	 */
	public static void scanRules(Board board)
	{
		int rows = board.getRows(); // On r�cup�re le nombre de lignes et de colonnes de la map
		int cols = board.getCols(); 
		Cell[][] array = board.getBoard();
		/*
		 * It�ration sur chaque Cellule du tableau
		 */
		for(int i = 0; i < cols; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				// Chaque cellule est compos� d'une liste d'Item
				ArrayList<Item> list = array[i][j].getList(); // On r�cup�re la liste d'Item
				// It�ration sur la liste d'Item
				for(Item element : list) 
				{
					if(element instanceof IRule) // Si l'�lement impl�mente IRule alors c'est que c'est un mot qui peut servir � cr�er une r�gle
					{
						IRule iRuleElement = (IRule) element;
						if(iRuleElement.isWord()) // Si l'�lement est un nom commun alors c'est peut �tre le d�but d'une r�gle
						{
							ruleToAdd = new IRule[3];
							ruleToAdd[0] = iRuleElement;
							if(i <= cols-2)
							{
								scanRight(array, j, i); // Ajout de la r�gle horizontal si elle existe
							}
							if(j <= rows-2)
							{
								scanDown(array, j, i);	// Ajout de la r�gle vertical si elle existe
							}
						}
					}
				}
			}
		}
	}
	/**
	 * M�thode qui va analyser les cellules � droite de la cellule RuleItem
	 * Si la premi�re cellule � sa droite est un verbe, la m�thode va analyser la cellule qui suit pour savoir
	 * si cette cellule est un mot.
	 * @param array tableau de cellule
	 * @param x	position x du RuleItem
	 * @param y position y du RuleItem
	 */
	public static void scanRight(Cell[][] array, int x, int y)
	{
		// It�ration sur la liste d'Item de la cellule � droite 
		for(Item element1 : array[y][x+1].getList())
		{
			if(element1 instanceof IRule) // Si l'�lement impl�mente IRule alors c'est que c'est un mot qui peut servir � cr�er une r�gle
			{
				IRule iRuleElement1 = (IRule) element1;
				if(iRuleElement1.isVerb()) // Si l'�lement est un verbe on regarde si la cellule � droite est un mot pour finir la r�gle
				{
					for(Item element2 : array[y][x+2].getList())
					{
						if(element2 instanceof IRule)
						{
							IRule iRuleElement2 = (IRule) element2;
							if(iRuleElement2.isWord())
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
	/**
	 * M�thode qui va analyser les cellules en dessous de la cellule RuleItem
	 * Si la premi�re cellule en dessous est un verbe, la m�thode va analyser la cellule qui suit pour savoir
	 * si cette cellule est un mot.
	 * @param array tableau de cellule
	 * @param x	position x du RuleItem
	 * @param y position y du RuleItem
	 */
	public static void scanDown(Cell[][] array, int x, int y)
	{
		// It�ration sur la liste d'Item de la cellule en dessous 
		for(Item element1 : array[y+1][x].getList())
		{
			if(element1 instanceof IRule) // Si l'�lement impl�mente IRule alors c'est que c'est un mot qui peut servir � cr�er une r�gle
			{
				IRule iRuleElement1 = (IRule) element1;
				if(iRuleElement1.isVerb()) // Si l'�lement est un verbe on regarde si la cellule en dessous est un mot pour finir la r�gle
				{
					for(Item element2 : array[y+2][x].getList())
					{
						if(element2 instanceof IRule)
						{
							IRule iRuleElement2 = (IRule) element2;
							if(iRuleElement2.isWord())
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
}