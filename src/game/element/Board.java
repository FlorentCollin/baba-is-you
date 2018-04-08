package game.element;

import java.util.ArrayList;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe qui va gérer le plateau de jeu.
 * 
 */
public class Board {
	
	private Cell[][] board; //map
	private ArrayList<Tuple> players;
	private int rows; // nombre de lignes
	private int cols; // nombre de colonnes
	private ArrayList<Tuple> changedCells = new ArrayList<>();
	

	

	public Board(Cell[][] board, int rows, int cols)
	{
		this.board = board;
		this.rows = rows;
		this.cols = cols;
	}
	
	
	public Cell[][] getBoard() {
		return board;
	}
	
	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}


	public Cell getCell(int x, int y)
	{
		return board[y][x];
	}
	
	public void setCell(int x, int y, Cell c)
	{
		board[y][x] = c; 
	}
	
	public ArrayList<Tuple> getPlayers()
	{
		return players;
	}
	
	public ArrayList<Tuple> getAndResetChangedCells() {
		ArrayList<Tuple> temp = changedCells;
		for(Tuple element : changedCells)
		{
			removeAllOccurenceInACell(board[element.getY()][element.getX()]);
		}
		changedCells = new ArrayList<>();
		return temp;
	}
	
	/**
	 * Méthode qui va supprimer tous les doublons d'Item dans une cellule, par exemple [Wall, Wall, Wall] --> [Wall]
	 * @param cellToChange Cellule sur laquelle la méthode va agir
	 */
	public void removeAllOccurenceInACell(Cell cellToChange)
	{
		ArrayList<Item> list = cellToChange.getList();
		for(int index = 0; index < list.size()-1; index++)
		{
			if(list.get(index).equals(list.get(index+1))) {
				list.remove(index+1);
			}
		}
			
	}

	/**
	 * Méthode qui va scanner les règles et ajuster le plateau en fonction
	 * 
	 */
	public void scanRules()
	{
		ArrayList<IRule[]> listOfActivesRules = Rules.scanRules(board);
		IRule element1;
		IRule element2;
		for(IRule[] element : listOfActivesRules)
		{
			element1 = element[0];
			element2 = element[2];
			if(element1 instanceof IRealItem && element2 instanceof IRealItem)
			{
				changeAllItemsforAnOtherItem(element1, element2);
			}
		}
	}
	
	private void changeAllItemsforAnOtherItem(IRule baseItem, IRule endItem) 
	{
		for(int i = 0; i < cols; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				ArrayList<Item> list = board[i][j].getList();
				for(int k = 1; k < list.size(); k++)
				{
					if(list.get(k).isRepresentedBy(baseItem))
					{
						//TODO ?????????? Petit problème d'implémentation
					}
				}
			}
		}
		
	}


	/**
	 * Méthode qui va regarder la map et retourner true si le joueur est sur l'objet qui permet de réussir le niveau
	 * @return true si le niveau est gagné, false sinon
	 */
	public boolean isWin()
	{
		IRule wordInRule = whoIsWin(); //Récupération de l'item gagnant
		if(wordInRule == null)
		{
			return false;
		}
		for(Tuple player : players)
		{
			//Itération sur chaque cellule contenant un joueur pour voir si le joueur est sur l'item qui permet de réussir le niveau
			for(Item element : board[player.getY()][player.getX()].getList())
			{
				if(element.isRepresentedBy(wordInRule)) //Regarde si l'item correspond à l'Item permettant de réussir le niveau
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Méthode qui va rechercher dans la liste de règles actives quel Item "is WIN".
	 */
	private IRule whoIsWin()
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextWin)
			{
				return element[0];
			}
		}
		return null;
	}
	
	/**
	 * Méthode qui va scanner la map et rechercher les différents joueurs en fonction des règles actives
	 * 
	 */
	public void searchPlayers()
	{
		players = new ArrayList<>();
		ArrayList<IRule> playerIs = whoIsPlayer();
		if(playerIs.size() == 0)
		{
			return;
		}
		for(int y = 0; y < cols; y++)
		{
			for(int x = 0; x < rows; x++)
			{
				ArrayList<Item> list = board[y][x].getList();
				for(int z = 0; z < list.size(); z++)
				{
					for(IRule OnePlayer : playerIs)
					{
						if(list.get(z) instanceof IRealItem && ((IRealItem) list.get(z)).isRepresentedBy(OnePlayer))
						{
							players.add(new Tuple(x,y,z));
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * Méthode qui va rechercher dans la liste de règles actives quel Item "is YOU".
	 */
	public ArrayList<IRule> whoIsPlayer()
	{
		ArrayList<IRule> playerIs = new ArrayList<>();
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextYou)
			{
				playerIs.add(element[0]);
			}
		}
		return playerIs;
	}
	
	/**
	 * Méthode qui va changer de cellule un item
	 * @param x1 position en x de l'item à changer de cellule
	 * @param y1 position en y de l'item à changer de cellule
	 * @param x2 nouvelle position en x de l'item
	 * @param y2 nouvelle position en y de l'item
	 * @param z position de l'item dans la liste
	 */
	public void changeItemCell(int x1, int y1, int x2, int y2, int z)
	{
		changedCells.add(new Tuple(x1,y1,0)); //On rajoute les cellules modifiées pour les repeintre en mode GUI
		changedCells.add(new Tuple(x2,y2,0));
		Item itemChange =  board[y1][x1].remove(z);
		board[y2][x2].add(itemChange);
		// Si un item de Règle est bougé alors on rescan les règles
		if(itemChange instanceof IRule)
			Rules.scanRules(board);
	}
	
		
	/**
	 * Méthode récursive qui va bouger un item dans une direction
	 * @param x1 position en x de l'item
	 * @param y1 position en y de l'item
	 * @param z position dans la liste de la cellule[y][x] (si z == -1 alors z = le dernier item de la liste de la cellule[y][x])
	 * @param direction direction dans laquelle l'item sera déplacer (0 == UP, 1 == RIGHT, 2 == DOWN, 3 == LEFT)
	 * @return true si l'objet à été bougé, false sinon
	 */
	public boolean move(int x1, int y1, int z, int direction)
	{
		int x2 = x1;
		int y2 = y1;
		Cell cellToMove = board[y1][x1];
		if(cellToMove.isEmpty())
		{
			return true;
		}
		if(z == -1 || z > cellToMove.getList().size()-1)
		{
			z = cellToMove.getList().size()-1;
		}
		switch(direction)
		{
		case 0: y2--; break;
		case 1: x2++; break;
		case 2: y2++; break;
		case 3: x2--; break;
		}
		Cell nextCell = board[y2][x2];
		if(! nextCell.isEmpty())
		{
			if(nextCell.lastItem().isStop())
			{
				return false;
			}
			else if(nextCell.lastItem().isDeadly())
			{
				//Ajout des cellules qui doivent être repeinte par la partie graphique
				changedCells.add(new Tuple(x1,y1,0));
				changedCells.add(new Tuple(x2,y2,0));
				cellToMove.remove(z);
				nextCell.removeItem(nextCell.lastItem());
				return true;
			}
			else if(nextCell.lastItem().isPushable())
			{
				if(move(x2, y2, -1, direction))
				{
					changeItemCell(x1, y1, x2, y2, z);
					return true;
				}
			}
			else
			{
				changeItemCell(x1, y1, x2, y2, z);
				return true;
			}
		}
		else
		{
			changeItemCell(x1, y1, x2, y2, z);
			return true;
		}
		return false;
	}
	
	/**
	 * Méthode qui va sauvegarder le niveau en cours dans un fichier .txt
	 */
	public void saveLvl()
	{
		LevelManager.saveLvl(this);
	}
}
