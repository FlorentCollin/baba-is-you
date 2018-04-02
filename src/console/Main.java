package console;

import java.util.Scanner;

import game.element.Board;
import game.element.Item;
import game.element.Rock;
import game.element.Rules;
import game.element.TextRock;
import game.element.Wall;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe principale qui va gérer le jeu en mode console 
 * 
 */
public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[] listOfLevels = LevelManager.getListOfLevels();
		Board board = null;
		String line;
		int i=0;
		while(i<listOfLevels.length)
		{
			board = LevelManager.readLevel(listOfLevels[i]);

			while(true)
			{
				Rules.scanRules(board);
				board.searchPlayers();
				DisplayBoard.display(board);
				if(board.isWin())
				{
					i++;
					break;
				}

				int direction;				
				line = keyInput();
				switch(line)
				{
				case "z" : direction = 0; break;
				case "d" : direction = 1; break;
				case "s" : direction = 2; break;
				case "q" : direction = 3; break;
				default : direction = 1; break;
				}
				for(Tuple player : board.getPlayers())
				{
					System.out.println("Something move ? " + board.move(player.getX(), player.getY(), player.getZ(), direction));
				}
			}
		}		


	}
    /**
     * Methode qui les lit les input en mode console
     * @return L'input
     */
    public static String keyInput()
    {
        Scanner in = new Scanner(System.in);
        String line = in.next();
        
        return line;
    }
}
