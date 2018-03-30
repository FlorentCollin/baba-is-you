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
	/**
	 * Méthode qui va scanner la map et rechercher les différents joueurs en fonction des règles actives
	 * 
	 */
	public void searchPlayers()
	{
		players = new ArrayList<>();
		IRule playerIs = whoIsPlayer();
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
