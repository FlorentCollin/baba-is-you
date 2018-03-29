package console;

import game.element.Board;
import game.levelManager.LevelManager;

/**
 * Classe principale qui va gérer le jeu en mode console 
 * 
 */
public class Main {

	public static void main(String[] args) {
		Board board = new Board(LevelManager.readLevel("lvl1"));
		DisplayBoard.display(board);
	}
}
