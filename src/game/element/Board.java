package game.element;

/**
 * Classe qui va gérer le plateau de jeu.
 * 
 */
public class Board {
	
	private Cell[][] board;

	
	public Board(int cols, int rows)
	{
		board = new Cell[rows][cols];
		
	}
}
