package game.element;
// Note : lors de l'implémentation de la fonction qui change les règles on peut s'arrêter après le dernier objet de type.
// Genre si on veut modifier tous les rochers si le prochaine objet n'est pas un objet ou arrête l'ittération ? 


import java.util.ArrayList;

/**
 * Classe qui contient une liste d'Item et qui permettra d'organiser l'affichage graphique.
 * 
 */
public class Cell {

	private ArrayList<Item> list;

	public Cell()
	{
		list = new ArrayList<>();
		// Background back = new Background();
		// list.add(back);
	}
	
	public void move(Item element)
	{
		//TODO
	}
	public void add(Item element)
	{
		//TODO
	}
}
