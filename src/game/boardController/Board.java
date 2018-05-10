package game.boardController;

import java.io.File;
import java.util.ArrayList;

import game.element.RealItem;
import game.element.IRule;
import game.element.Item;
import game.element.TextOn;
import game.element.TextWin;
import game.element.TextYou;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

/**
 * Classe qui va gérer le plateau de jeu.
 *
 */
public class Board {

	private Cell[][] board; // map
	private int rows; // nombre de lignes
	private int cols; // nombre de colonnes
	private int LevelNumber; // Numéro du niveau (ex LVL 1, LVL 2,...)
	private int depthOfLevel; // Numéro de la profondeur de niveau (ex LVL5_1, LVL5_2,...)
	private ArrayList<Tuple> players;
	private ArrayList<Tuple> changedCells = new ArrayList<>();

	public Board(Cell[][] board, int LevelNumber, int depthOfLevel, int rows, int cols) {
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

	public Cell getCell(int x, int y) {
		return board[y][x];
	}

	public void setCell(int x, int y, Cell c) {
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

	public ArrayList<Tuple> getPlayers() {
		return players;
	}

	public void addChangedCell(Tuple changedCell) {
		this.changedCells.add(changedCell);
	}

	/**
	 * Méthode qui va renvoyer l'ensemble des cellules changées et qui va remettre é
	 * 0 cette liste
	 * 
	 * @return Les cellules changées
	 */
	public ArrayList<Tuple> getAndResetChangedCells() {
		ArrayList<Tuple> temp = changedCells;
		for (Tuple element : changedCells) {
			removeAllOccurenceInACell(board[element.getY()][element.getX()]);
		}
		changedCells = new ArrayList<>();
		return temp;
	}

	/**
	 * Méthode qui va supprimer tous les doublons d'Item dans une cellule, par
	 * exemple [Wall, Wall, Wall] --> [Wall] Cette méthode va étre trés utile pour
	 * optimiser la vitesse du programme
	 * 
	 * @param cellToChange
	 *            Cellule sur laquelle la méthode va agir
	 */
	public void removeAllOccurenceInACell(Cell cellToChange) {
		ArrayList<Item> list = cellToChange.getList();
		for (int index = 0; index < list.size() - 1; index++) {
			if (list.get(index).equals(list.get(index + 1))) {
				list.remove(index + 1);
			}
		}
	}

	/**
	 * Méthode qui va scanner les régles et ajuster la map en fonction
	 *
	 */
	public void scanRules() {
		if (Rules.getListOfRulesActives() == null)
			Rules.scanRules(LevelManager.getActivesBoards());
		/*
		 * Ici on regarde si le niveau qu'on a chargé est différent de "cleanEditor" car
		 * lorsqu'on lançait l'éditeur et qu'une règle du style "ROCK IS WALL" était
		 * actives on avait le rocher (où se trouve les items sélectionnables) qui se
		 * transformait en mur Ce qu'on veut éviter pour pouvoir toujours choisir le
		 * rocher
		 */
		if (!LevelManager.getCurrentLeveLName().substring(LevelManager.getCurrentLeveLName().length() - 11)
				.equals("cleanEditor")) {
			// Itération sur la liste des régles Actives pour savoir si il y a une régle du
			// type "WALL is ROCK", "WALL IS BABA", etc,...
			for (IRule[] element : Rules.getListOfRulesActives()) {
				if (element[0].isWord() && element[2].isWord()) {
					// On change tous les items qui correspondent é element1 en item element2
					changeAllItemsforAnOtherItem(element[0], element[2]);
				}
			}
		}
	}

	/**
	 * Change tous les items de la map correspondant à la baseItem et les remplace
	 * par les items correspondant au endItem
	 * 
	 * @param baseItem
	 *            L'item qu'on doit changer
	 * @param endItem
	 *            Ce en quoi baseItem doit étre changé
	 */
	private void changeAllItemsforAnOtherItem(IRule baseItem, IRule endItem) {
		// Ici on change l'Item mais comme on veut passer d'un Item de régle à un "vrai"
		// item on doit chercher sa correspondance
		// Dans le fichier JSON des correspondances
		String endItemStr = LevelManager.correspondingTextOrItem(((Item) endItem).getName()); // Pour pouvoir utiliser
																								// .getName() aucune
																								// erreur possible car
																								// IRule n'est implanté
																								// que dans Item
		// Itération sur l'entiéreté de la map
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				ArrayList<Item> list = board[i][j].getList();
				// Itération sur la liste d'Item
				for (int k = 0; k < list.size(); k++) {
					if (list.get(k).isRepresentedBy(baseItem)) // Si l'élement correspond é baseItem alors on doit le
																// changer en endItem
					{
						list.set(k, LevelManager.createItem(endItemStr));
						changedCells.add(new Tuple(j, i, 0)); // On n'oublie pas d'ajouter les cellules changés car
																// sinon elles ne seront pas repeinte par l'interface
																// graphique
					}
				}
			}
		}
	}

	public boolean isAnActiveTp(Item tp) {
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextOn && tp.isRepresentedBy(element[0]))
				return true;
		}
		return false;
	}

	/**
	 * Méthode qui va regarder la map et retourner true si le joueur est sur l'objet
	 * qui permet de réussir le niveau
	 * 
	 * @return true si le niveau est gagné, false sinon
	 */
	public boolean isWin() {
		IRule wordInRule = whoIsWin(); // Récupération de l'item gagnant
		// Optimisation de l'algo pour éviter d'itérer sur l'entiéreté des joueurs si il
		// n'y a pas d'item gagnant
		if (wordInRule == null) {
			return false;
		}
		for (Tuple player : players) {
			// Itération sur chaque cellule contenant un joueur pour voir si le joueur est
			// sur l'item qui permet de réussir le niveau
			for (Item element : board[player.getY()][player.getX()].getList()) {
				if (element.isRepresentedBy(wordInRule)) // Regarde si l'item correspond é l'Item permettant de réussir
															// le niveau
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Méthode qui va rechercher dans la liste de régles actives quel Item "is WIN".
	 */
	private IRule whoIsWin() {
		// Itération sur la liste des régles actives
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextWin) {
				return element[0];
			}
		}
		return null;
	}

	/**
	 * Méthode qui va scanner la map et rechercher les différents joueurs en
	 * fonction des régles actives
	 *
	 */
	public void searchPlayers() {
		players = new ArrayList<>();
		ArrayList<IRule> playerIs = whoIsPlayer();
		// Optimisation de l'algo qui évite de parcourir l'entiéreté de la map si il n'y
		// a aucun joueur
		if (playerIs.size() == 0) {
			return;
		}
		// Itération sur l'entiéreté de la map pour rechercher les joueurs
		for (int y = 0; y < cols; y++) {
			for (int x = 0; x < rows; x++) {
				ArrayList<Item> list = board[y][x].getList();
				// Le z correspond é l'index dans la liste d'Item de la cellule et ici on itére
				// sur la liste d'Item
				for (int z = 0; z < list.size(); z++) {
					// Itération sur la list des joueurs
					for (IRule OnePlayer : playerIs) {
						// Si l'Item est un joueur alors on ajoute un Tuple(x,y,z) qui correspond au
						// coordonnées du joueur dans la map
						if (list.get(z) instanceof RealItem && ((RealItem) list.get(z)).isRepresentedBy(OnePlayer)) {
							players.add(new Tuple(x, y, z));
						}
					}
				}
			}
		}

	}

	/**
	 * Méthode qui va rechercher dans la liste de régles actives quel Item "is YOU".
	 */
	public ArrayList<IRule> whoIsPlayer() {
		// Note : on renvoit une ArrayList<IRule> car il est possible d'avoir plusieurs
		// sortes de joueurs en méme temps
		// Exemple "BABA is YOU" et "WALL is YOU"
		ArrayList<IRule> playerIs = new ArrayList<>();
		// Itération sur l'ensemble des régles
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextYou) {
				playerIs.add(element[0]);
			}
		}
		return playerIs;
	}

	/**
	 * Méthode qui va changer de cellule un item
	 * 
	 * @param x1
	 *            position en x de l'item é changer de cellule
	 * @param y1
	 *            position en y de l'item é changer de cellule
	 * @param x2
	 *            nouvelle position en x de l'item
	 * @param y2
	 *            nouvelle position en y de l'item
	 * @param z
	 *            position de l'item dans la liste
	 */
	public void changeItemCell(int x1, int y1, int x2, int y2, int z) {
		changedCells.add(new Tuple(x1, y1, 0)); // On rajoute les cellules modifiées pour les repeintre en mode GUI
		changedCells.add(new Tuple(x2, y2, 0));
		Item itemChange = board[y1][x1].remove(z);
		board[y2][x2].add(itemChange);
		// Si un item de Régle est bougé alors on rescan les régles et on applique les
		// modifications sur le board
		if (itemChange instanceof IRule) {
			Rules.scanRules(LevelManager.getActivesBoards());
		}
	}

	/**
	 * Méthode qui va sauvegarder le niveau en cours dans un fichier .txt
	 */
	public void saveLvl() {
		LevelManager.saveLvl("levels" + File.separator + "saves" + File.separator + "save");
	}
}
