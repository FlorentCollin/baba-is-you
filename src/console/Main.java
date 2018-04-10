package console;

/* Ce qu'il faut modifier :
 * Test unitaires
 * rapport
 */

/* A AJOUTER
 *  Les Téléporteurs (idée méga intéressante !)
 *  EDITEUR DE NIVEAUX
 */


// NE FONCTIONNE PLUS ATTENTION !!!
import java.util.Scanner;

import game.boardController.Board;
import game.boardController.Rules;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe principale qui va gérer le jeu en mode console 
 * 
 */
public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[][] listOfLevels = LevelManager.getListOfLevels();
		Board board = null;
		String line;
		int i=0;
		while(i<listOfLevels.length)
		{
			while(true)
			{
//				Rules.scanRules(board.getBoard());
				board.searchPlayers();
				DisplayBoard.display(board);
				if(board.isWin())
				{
					i++;
					break;
				}

				int direction;				
				line = keyInput();
				switch(line.split("")[0])
				{
				case "z" : direction = 0; break;
				case "d" : direction = 1; break;
				case "s" : direction = 2; break;
				case "q" : direction = 3; break;
				case "n" : direction = -1; break;
				case "p" : direction = -2; break;
				case "r" : direction = -3; break;
				default : direction = 1; break;
				}
				if(direction == -1 && i<listOfLevels.length-1)
				{
					i++;
					break;
				}
				if(direction == -2 && i>=1)
				{
					i--;
					break;
				}
				if(direction == -3)
				{
					break;
				}
				for(Tuple player : board.getPlayers())
				{
//					System.out.println("Something move ? " + board.move(player.getX(), player.getY(), player.getZ(), direction));
				}
				
			}
		}		
		System.out.println("FINISH !");
		in.close();
	}
    /**
     * Methode qui les lit les input en mode console
     * @return L'input
     */
    public static String keyInput()
    {
        Scanner in = new Scanner(System.in);
        String line = in.next();
        in.close();
        return line;
    }
}
