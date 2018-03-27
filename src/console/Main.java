package console;

import game.element.Board;
import game.element.Cell;

/**
 * Classe principale qui va gérer le jeu en mode console 
 * 
 */
public class Main {

	public static void main(String[] args) {
		Board board = new Board(10,10);
		System.out.println(board);
		Cell cell = new Cell();
		System.out.println(cell);
		
	}
}
