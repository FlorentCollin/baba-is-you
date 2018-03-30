package console;

import java.util.ArrayList;

import game.element.Board;
import game.element.IRule;
import game.element.Rules;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe principale qui va gérer le jeu en mode console 
 * 
 */
public class Main {

	public static void main(String[] args) {
		Board board = LevelManager.readLevel("lvl1");
		DisplayBoard.display(board);
		Rules.scanRules(board);
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			for(IRule element1 : element)
			{
				System.out.print(element1);
			}
			System.out.println();
		}
		System.out.println("-------------------------------------------------------------------------------");
		board.searchPlayers();
		System.out.println(board.getPlayers());
	}
}
