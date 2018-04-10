package game.boardController;

import game.element.Item;
import game.element.TpBlue;
import game.element.TpRed;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
/**
 * Classe qui va controller tous les déplacements dans chaque Board
 *
 */
public class MoveController {

	/**
	 * Méthode récursive qui va bouger un item dans une direction
	 * @param x1 position en x de l'item
	 * @param y1 position en y de l'item
	 * @param z position dans la liste de la cellule[y][x] (si z == -1 alors z = le dernier item de la liste de la cellule[y][x])
	 * @param direction direction dans laquelle l'item sera déplacer (0 == UP, 1 == RIGHT, 2 == DOWN, 3 == LEFT)
	 * @return true si l'objet à été bougé, false sinon
	 */
	public static boolean move(Board board, int x1, int y1, int z, int direction)
	{
		//Copie des coordonnées
		int x2 = x1;
		int y2 = y1;
		Board[] activesBoard = LevelManager.getActivesBoards();
		Cell cellToMove = board.getBoard()[y1][x1];
		if(cellToMove.isEmpty()) //Si la cellule est vide alors on peut forément bouger tous les éléments précédents
		{
			return true;
		}
		if(z == -1 || z > cellToMove.getList().size()-1) //On remet l'index de la liste de la Cellule s'il est trop loin au dernier élement
		{
			z = cellToMove.getList().size()-1;
		}
		switch(direction)
		{
		case 0: y2--; break; //UP
		case 1: x2++; break; //RIGHT
		case 2: y2++; break; //DOWN
		case 3: x2--; break; //LEFT
		}
		Cell nextCell = board.getBoard()[y2][x2];
		if(! nextCell.isEmpty())
		{
			//On vérifie en premier si le prochain item est sous la règle "STOP", si c'est le cas cela indique qu'on ne peut pas bouger cellToMove
			if(nextCell.lastItem().isStop())
			{
				return false;
			}
			else if(nextCell.lastItem().isDeadly())
			{
				//Ajout des cellules qui doivent être repeinte par la partie graphique
				board.addChangedCell(new Tuple(x1,y1,0));
				board.addChangedCell(new Tuple(x2,y2,0));
				cellToMove.remove(z); //On "tue" la cellule à bouger
				nextCell.removeItem(nextCell.lastItem()); //On retire la cellule qui "tue"
				return true;
			}
			//PARTIE RECURSIVE ! 
			//Si le prochain item est sous la règle "PUSH" alors on applique la méthode "move" au prochain item
			else if(nextCell.lastItem().isPushable())
			{
				if(move(board, x2, y2, -1, direction)) //Comme "move" est une fonction booléenne si l'item suivant à été bouger alors cellToMove peut aussi bouger 
				{
					board.changeItemCell(x1, y1, x2, y2, z);
					return true;
				}
			}
			//Si la prochaine case n'est pas vide mais qu'aucune des règles qui modifient "move" ne sont actives dessus alors on peut bouger cellToMove
			else
			{
				Item itemToAdd;
				for(Item element : nextCell.getList())
				{
					if(element instanceof TpBlue && board.getDepthOfLevel()+1<activesBoard.length && board.isAnActiveTp(element))
					{
						board.addChangedCell(new Tuple(x1,y1,0));
						board.addChangedCell(new Tuple(x2,y2,0));
						itemToAdd = cellToMove.remove(z);
						activesBoard[board.getDepthOfLevel()+1].getCell(x2, y2).add(itemToAdd);
						return true;
					}
					if(element instanceof TpRed && board.getDepthOfLevel()-1>=0 && board.isAnActiveTp(element))
					{
						board.addChangedCell(new Tuple(x1,y1,0));
						board.addChangedCell(new Tuple(x2,y2,0));
						itemToAdd = cellToMove.remove(z);
						activesBoard[board.getDepthOfLevel()-1].getCell(x2, y2).add(itemToAdd);
						return true;
					}
				}
				board.changeItemCell(x1, y1, x2, y2, z);
				return true;
			}
		}
		//Si la prochaine case est vide alors on peut forcément bouger cellToMove
		else
		{
			board.changeItemCell(x1, y1, x2, y2, z);
			return true;
		}
		//Comme l'entièreté des "return" sont dans des if else on retourne false pour être sûr d'avoir une valeur de retour
		return false;
	}
}
