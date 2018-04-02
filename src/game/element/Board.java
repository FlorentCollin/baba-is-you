package game.element;

import java.util.ArrayList;

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
	
	public boolean isWin()
	{
		IRule wordInRule = whoIsWin();
		if(wordInRule == null)
		{
			return false;
		}
		for(Tuple player : players)
		{
			for(Item element : board[player.getY()][player.getX()].getList())
			{
				if(element.isRepresentedBy(wordInRule))
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
	public IRule whoIsWin()
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
	public void changeItemCell(int x1, int y1, int x2, int y2, int z)
	{
		Item itemChange =  board[y1][x1].remove(z);
		board[y2][x2].add(itemChange);
	}
	
	public boolean move(int x1, int y1, int z, int direction)
	{
		int x2 = x1;
		int y2 = y1;
		Cell cellToMove = board[y1][x1];
		if(cellToMove.isEmpty())
		{
			return true;
		}
		if(z == -1)
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
	 * Méthode qui va scanner la map et rechercher les différents joueurs en fonction des règles actives
	 * 
	 */
	public void searchPlayers()
	{
		players = new ArrayList<>();
		IRule playerIs = whoIsPlayer();
		if(playerIs == null)
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
					if(list.get(z) instanceof IRealItem && ((IRealItem) list.get(z)).isRepresentedBy(playerIs))
					{
						players.add(new Tuple(x,y,z));
					}
				}
			}
		}
		
	}
	
	/**
	 * Méthode qui va rechercher dans la liste de règles actives quel Item "is YOU".
	 */
	public IRule whoIsPlayer()
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextYou)
			{
				return element[0];
			}
		}
		return null;
	}
}
