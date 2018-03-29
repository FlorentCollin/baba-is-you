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
		for(Cell[] element1 : array)
		{
			for(Cell element2 : element1)
			{
				list = element2.getList();
				lastElement = list.get(list.size()-1);
				System.out.print(lastElement.getName());
			//System.out.print(list.size());
			}
			System.out.println();
		}
	}
}
