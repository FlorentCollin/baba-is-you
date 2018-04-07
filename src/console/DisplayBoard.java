package console;

import java.util.ArrayList;
import game.element.Board;
import game.element.Cell;
import game.element.Item;

/**
 * Affiche le niveau en mode console 
 *
 */
public class DisplayBoard {
	public static void display(Board board)
	{
		ArrayList<Item> list;
		Item lastElement;
		Cell[][] array = board.getBoard();
		//It�ration sur tous les �l�ments de la map
		for(Cell[] element1 : array)
		{
			for(Cell element2 : element1)
			{
				list = element2.getList();
				lastElement = list.get(list.size()-1);
				System.out.print(lastElement.getName()); //Affichage du dernier �l�ment en fonction de l'ordre de priorit� d'affichage
			}
			System.out.println(); //Passage � la ligne suivante
		}
	}
}
