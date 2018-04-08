package game.element;

import java.util.ArrayList;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe qui va g�rer le plateau de jeu.
 * 
 */
public class Board {
	
	private Cell[][] board; //map
	private int rows; // nombre de lignes
	private int cols; // nombre de colonnes
	private int LevelNumber; // Num�ro du niveau (ex LVL 1, LVL 2,...)
	private ArrayList<Tuple> players;
	private ArrayList<Tuple> changedCells = new ArrayList<>();
	

	

	public Board(Cell[][] board, int LevelNumber, int rows, int cols)
	{
		this.board = board;
		this.LevelNumber = LevelNumber;
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
	
	public int getLevelNumber() {
		return LevelNumber;
	}


	public void setLevelNumber(int levelNumber) {
		LevelNumber = levelNumber;
	}


	public ArrayList<Tuple> getPlayers()
	{
		return players;
	}
	
	/**
	 * M�thode qui va renvoyer l'ensemble des cellules chang�es et qui va remettre � 0 cette liste
	 * @return Les cellules chang�es
	 */
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
	 * M�thode qui va supprimer tous les doublons d'Item dans une cellule, par exemple [Wall, Wall, Wall] --> [Wall]
	 * @param cellToChange Cellule sur laquelle la m�thode va agir
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
	 * M�thode qui va scanner les r�gles et ajuster la map en fonction
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
						//TODO ??? Petit probl�me d'impl�mentation
					}
				}
			}
		}
		
	}


	/**
	 * M�thode qui va regarder la map et retourner true si le joueur est sur l'objet qui permet de r�ussir le niveau
	 * @return true si le niveau est gagn�, false sinon
	 */
	public boolean isWin()
	{
		IRule wordInRule = whoIsWin(); //R�cup�ration de l'item gagnant
		//Optimisation de l'algo pour �viter d'it�rer sur l'enti�ret� des joueurs si il n'y a pas d'item gagnant
		if(wordInRule == null)
		{
			return false;
		}
		for(Tuple player : players)
		{
			//It�ration sur chaque cellule contenant un joueur pour voir si le joueur est sur l'item qui permet de r�ussir le niveau
			for(Item element : board[player.getY()][player.getX()].getList())
			{
				if(element.isRepresentedBy(wordInRule)) //Regarde si l'item correspond � l'Item permettant de r�ussir le niveau
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * M�thode qui va rechercher dans la liste de r�gles actives quel Item "is WIN".
	 */
	private IRule whoIsWin()
	{
		//It�ration sur la liste des r�gles actives
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
	 * M�thode qui va scanner la map et rechercher les diff�rents joueurs en fonction des r�gles actives
	 * 
	 */
	public void searchPlayers()
	{
		players = new ArrayList<>();
		ArrayList<IRule> playerIs = whoIsPlayer();
		//Optimisation de l'algo qui �vite de parcourir l'enti�ret� de la map si il n'y a aucun joueur
		if(playerIs.size() == 0)
		{
			return;
		}
		//It�ration sur l'enti�ret� de la map pour rechercher les joueurs
		for(int y = 0; y < cols; y++)
		{
			for(int x = 0; x < rows; x++)
			{
				ArrayList<Item> list = board[y][x].getList();
				//Le z correspond � l'index dans la liste d'Item de la cellule et ici on it�re sur la liste d'Item
				for(int z = 0; z < list.size(); z++)
				{
					//It�ration sur la list des joueurs
					for(IRule OnePlayer : playerIs)
					{
						//Si l'Item est un joueur alors on ajoute un Tuple(x,y,z) qui correspond au coordonn�es du joueur dans la map
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
	 * M�thode qui va rechercher dans la liste de r�gles actives quel Item "is YOU".
	 */
	public ArrayList<IRule> whoIsPlayer()
	{
		//Note : on renvoit une ArrayList<IRule> car il est possible d'avoir plusieurs sortes de joueurs en m�me temps
		//Exemple "BABA is YOU" et "WALL is YOU"
		ArrayList<IRule> playerIs = new ArrayList<>();
		//It�ration sur l'ensemble des r�gles
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
	 * M�thode qui va changer de cellule un item
	 * @param x1 position en x de l'item � changer de cellule
	 * @param y1 position en y de l'item � changer de cellule
	 * @param x2 nouvelle position en x de l'item
	 * @param y2 nouvelle position en y de l'item
	 * @param z position de l'item dans la liste
	 */
	public void changeItemCell(int x1, int y1, int x2, int y2, int z)
	{
		changedCells.add(new Tuple(x1,y1,0)); //On rajoute les cellules modifi�es pour les repeintre en mode GUI
		changedCells.add(new Tuple(x2,y2,0));
		Item itemChange =  board[y1][x1].remove(z);
		board[y2][x2].add(itemChange);
		// Si un item de R�gle est boug� alors on rescan les r�gles
		if(itemChange instanceof IRule)
			Rules.scanRules(board);
	}
	
		
	/**
	 * M�thode r�cursive qui va bouger un item dans une direction
	 * @param x1 position en x de l'item
	 * @param y1 position en y de l'item
	 * @param z position dans la liste de la cellule[y][x] (si z == -1 alors z = le dernier item de la liste de la cellule[y][x])
	 * @param direction direction dans laquelle l'item sera d�placer (0 == UP, 1 == RIGHT, 2 == DOWN, 3 == LEFT)
	 * @return true si l'objet � �t� boug�, false sinon
	 */
	public boolean move(int x1, int y1, int z, int direction)
	{
		//Copie des coordonn�es
		int x2 = x1;
		int y2 = y1;
		Cell cellToMove = board[y1][x1];
		if(cellToMove.isEmpty()) //Si la cellule est vide alors on peut for�ment bouger tous les �l�ments pr�c�dents
		{
			return true;
		}
		if(z == -1 || z > cellToMove.getList().size()-1) //On remet l'index de la liste de la Cellule s'il est trop loin au dernier �lement
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
		Cell nextCell = board[y2][x2];
		if(! nextCell.isEmpty())
		{
			//On v�rifie en premier si le prochain item est sous la r�gle "STOP", si c'est le cas cela indique qu'on ne peut pas bouger cellToMove
			if(nextCell.lastItem().isStop())
			{
				return false;
			}
			else if(nextCell.lastItem().isDeadly())
			{
				//Ajout des cellules qui doivent �tre repeinte par la partie graphique
				changedCells.add(new Tuple(x1,y1,0));
				changedCells.add(new Tuple(x2,y2,0));
				cellToMove.remove(z); //On "tue" la cellule � bouger
				nextCell.removeItem(nextCell.lastItem()); //On retire la cellule qui "tue"
				return true;
			}
			//PARTIE RECURSIVE ! 
			//Si le prochain item est sous la r�gle "PUSH" alors on applique la m�thode "move" au prochain item
			else if(nextCell.lastItem().isPushable())
			{
				if(move(x2, y2, -1, direction)) //Comme "move" est une fonction bool�enne si l'item suivant � �t� bouger alors cellToMove peut aussi bouger 
				{
					changeItemCell(x1, y1, x2, y2, z);
					return true;
				}
			}
			//Si la prochaine case n'est pas vide mais qu'aucune des r�gles qui modifient "move" ne sont actives dessus alors on peut bouger cellToMove
			else
			{
				changeItemCell(x1, y1, x2, y2, z);
				return true;
			}
		}
		//Si la prochaine case est vide alors on peut forc�ment bouger cellToMove
		else
		{
			changeItemCell(x1, y1, x2, y2, z);
			return true;
		}
		//Comme l'enti�ret� des "return" sont dans des if else on retourne false pour �tre s�r d'avoir une valeur de retour
		return false;
	}
	
	/**
	 * M�thode qui va sauvegarder le niveau en cours dans un fichier .txt
	 */
	public void saveLvl()
	{
		LevelManager.saveLvl(this);
	}
}
