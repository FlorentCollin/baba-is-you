package game.element;

/**
 * Classe qui va gérer le plateau de jeu.
 * 
 */
public class Board {
	
	private Cell[][] board;

	
	public Cell[][] getBoard() {
		return board;
	}

	public Board(Cell[][] board)
	{
		this.board = board;
	}
}
