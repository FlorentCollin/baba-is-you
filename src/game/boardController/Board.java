package game.boardController;

import java.util.ArrayList;

import game.element.IRealItem;
import game.element.IRule;
import game.element.Item;
import game.element.TextOn;
import game.element.TextWin;
import game.element.TextYou;
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
	private int depthOfLevel; // Num�rode la profondeur de niveau (ex LVL5_1, LVL5_2,...)
	private ArrayList<Tuple> players;
	private ArrayList<Tuple> changedCells = new ArrayList<>();
	

	
	public Board(Cell[][] board, int LevelNumber, int depthOfLevel, int rows, int cols)
	{
		this.board = board;
		this.LevelNumber = LevelNumber;
		this.depthOfLevel = depthOfLevel;
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


	public int getDepthOfLevel() {
		return depthOfLevel;
	}

	public ArrayList<Tuple> getPlayers()
	{
		return players;
	}
	
	public void addChangedCell(Tuple changedCell)
	{
		this.changedCells.add(changedCell);
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
	 * Cette m�thode va �tre tr�s utile pour optimiser la vitesse du programme
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
		ArrayList<IRule[]> listOfActivesRules = Rules.scanRules(LevelManager.getActivesBoards());
		IRule element1;
		IRule element2;
		//It�ration sur la liste des r�gles Actives pour savoir si il y a une r�gle du type "WALL is ROCK", "WALL IS BABA", etc,...
		for(IRule[] element : listOfActivesRules)
		{
			element1 = element[0];
			element2 = element[2];
			if(element1.isWord() && element2.isWord())
			{
				//On change tous les items qui correspondent � element1 en item element2
				changeAllItemsforAnOtherItem(element1, element2);
			}
		}
	}
	
	/**
	 * Change tous les items de la map correspondant � baseItem et les remplace par les items correspondant � endItem
	 * @param baseItem L'item qu'on doit changer
	 * @param endItem Ce en quoi baseItem doit �tre chang�
	 */
	private void changeAllItemsforAnOtherItem(IRule baseItem, IRule endItem) 
	{
		//It�ration sur l'enti�ret� de la map
		for(int i = 0; i < cols; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				ArrayList<Item> list = board[i][j].getList();
				//It�ration sur la liste d'Item
				for(int k = 0; k < list.size(); k++)
				{
					if(list.get(k).isRepresentedBy(baseItem)) //Si l'�lement correspond � baseItem alors on doit le changer en endItem
					{
						Item itemEndItem = (Item) endItem; //Pour pouvoir utiliser .getName() aucune erreur possible car IRule n'est implant� que dans Item
						//Ici on change l'Item mais comme on veut passer d'un Item de r�gle � un "vrai" item on doit chercher sa correspondance
						//Dans le fichier JSON des correspondance
						list.set(k, LevelManager.createItem(LevelManager.correspondingTextOrItem(itemEndItem.getName())));
						changedCells.add(new Tuple(j,i,0)); //On n'oublie pas d'ajouter les cellules chang�s car sinon elles ne seront pas repeinte par l'interface graphique
					}
				}
			}
		}
		
	}

	public boolean isAnActiveTp(Item tp)
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextOn && tp.isRepresentedBy(element[0]))
				return true;
		}
		return false;
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
			scanRules();
	}
	
	/**
	 * M�thode qui va sauvegarder le niveau en cours dans un fichier .txt
	 */
	public void saveLvl()
	{
		LevelManager.saveLvl();
	}
}
