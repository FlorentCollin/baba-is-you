package game.boardController;

import java.io.File;
import java.util.ArrayList;

import game.element.IRule;
import game.element.Item;
import game.element.RealItem;
import game.element.TextOn;
import game.element.TextWin;
import game.element.TextYou;
import game.element.TpGreen;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import gui.BabaIsYouApp;

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
	private Tuple[] correspondingTp;

	public Board(Cell[][] board, int LevelNumber, int depthOfLevel, int rows, int cols) {
		this.board = board;
		this.LevelNumber = LevelNumber;
		this.depthOfLevel = depthOfLevel;
		this.rows = rows;
		this.cols = cols;
	}
	// Getteurs et setteurs
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

	public Tuple[] getCorrespondingTp() {
		return correspondingTp;
	}

	/**
	 * Ajoute un tuple contenant les positions d'une cellule qui a été modifié pour pouvoir la redessiner
	 * via l'interface graphique
	 * @param changedCell La tuple contenant la position de la cellule qui a été changée
	 */
	public void addChangedCell(Tuple changedCell) {
		this.changedCells.add(changedCell);
	}

	/**
	 * Méthode qui va renvoyer l'ensemble des cellules changées et qui va remettre à
	 * 0 cette liste
	 * 
	 * @return Les cellules changées
	 */
	public ArrayList<Tuple> getAndResetChangedCells() {
		// On crée une variable temporaire contenant la liste des cellules changées
		ArrayList<Tuple> temp = changedCells;
		/*
		 * Ici on va nettoyer les cellules en retirer les items dupliqués (ex : Si la cellule contient
		 * 2 Walls alors on retire un des deux Walls pour qu'il n'en reste plus qu'un seul.
		 * Cela permet d'optimiser les prochains déplacements et ainsi on optimise la vitesse d'exéction du programme
		 */
		for (Tuple element : changedCells) {
			removeAllOccurenceInACell(board[element.getY()][element.getX()]);
		}
		changedCells = new ArrayList<>();
		return temp; 
	}

	/**
	 * Méthode qui va supprimer tous les doublons d'Item dans une cellule, par
	 * exemple [Wall, Wall, Wall] --> [Wall] Cette méthode va étre très utile pour
	 * optimiser la vitesse du programme
	 * 
	 * @param cellToChange
	 *            Cellule sur laquelle la méthode va agir
	 */
	public void removeAllOccurenceInACell(Cell cellToChange) {
		ArrayList<Item> list = cellToChange.getList();
		// Itération sur la liste
		/*
		 * TODO améliorer cette méthode pour qu'elle supprime vraiment l'entirèreté des doublons 
		 * Pour le moment si on a [WALL, BABA, WALL] la méthode ne supprime pas les deux walls
		 */
		for (int index = 0; index < list.size() - 1; index++) {
			if (list.get(index).equals(list.get(index + 1))) {
				list.remove(index + 1);
			}
		}
	}

	/**
	 * Méthode qui va scanner les règles et ajuster la map en fonction
	 *
	 */
	public void scanRules() {
		if (Rules.getListOfRulesActives() == null)
			Rules.scanRules(LevelManager.getActivesBoards());
		/*
		 * Ici on regarde si le niveau qu'on a chargé est différent de "cleanEditor" car
		 * lorsqu'on lançait l'éditeur et qu'une règle du style "ROCK IS WALL" était
		 * active on avait le rocher (où se trouve les items sélectionnables) qui se
		 * transformait en mur Ce qu'on veut éviter pour pouvoir toujours choisir le
		 * rocher
		 */
		if (!LevelManager.getCurrentLeveLName().substring(LevelManager.getCurrentLeveLName().length() - 11)
				.equals("cleanEditor")) { // -11 = "cleanEditor".length()
			// Itération sur la liste des règles Actives pour savoir si il y a une règle du
			// type "WALL is ROCK", "WALL IS BABA", etc,...
			for (IRule[] element : Rules.getListOfRulesActives()) {
				if (element[0].isWord() && element[2].isWord()) {
					// On change tous les items qui correspondent à element1 en item element2
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
	 *            Ce en quoi baseItem doit être changé
	 */
	private void changeAllItemsforAnOtherItem(IRule baseItem, IRule endItem) {
		// Ici on change l'Item mais comme on veut passer d'un Item de règle à un "vrai"
		// item on doit chercher sa correspondance
		// Dans le fichier JSON des correspondances

		// Pour pouvoir utiliser .getName() on doit cast (Item), aucune erreur possible car IRule n'est
		// implanté que dans Item
		String endItemStr = LevelManager.correspondingTextOrItem(((Item) endItem).getName());
		// Itération sur l'entièreté de la map
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				ArrayList<Item> list = board[i][j].getList();
				// Itération sur la liste d'Item
				for (int k = 0; k < list.size(); k++) {
					if (list.get(k).isRepresentedBy(baseItem)) // Si l'élement correspond au baseItem alors on doit le
						// changer en endItem
					{
						list.set(k, LevelManager.createItem(endItemStr));
						changedCells.add(new Tuple(j, i, 0)); // On n'oublie pas d'ajouter les cellules changées car
						// sinon elles ne seront pas repeinte par l'interface
						// graphique
					}
				}
			}
			//Gestion d'un succès
			if (BabaIsYouApp.success.unlock("Wizard")) {
				BabaIsYouApp.showSuccessUnlocked();
			}
		}
	}

	/**
	 * Regarde si l'item passé en paramètre est bien un TP actif
	 * @param tp 
	 * @return true si l'item est bien un Tp actif, false sinon
	 */
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
		ArrayList<IRule> winIs = whoIsWin(); // Récupération des Items gagnants
		// Optimisation de l'algo pour éviter d'itérer sur l'entièreté des joueurs si il
		// n'y a pas d'item gagnant
		if (winIs == null) {
			return false;
		}
		for (Tuple player : players) {
			// Itération sur chaque cellule contenant un joueur pour voir si le joueur est
			// sur l'item qui permet de réussir le niveau
			for (Item element : board[player.getY()][player.getX()].getList()) {
				for(IRule word : winIs) {
					if (element.isRepresentedBy(word)) { // Regarde si l'item correspond à un des Item permettant de réussir
						// le niveau
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Méthode qui va rechercher dans la liste de règles actives quel Item "is WIN"
	 * @return l'IRule qui signifie la victoire
	 */
	public ArrayList<IRule> whoIsWin() {
		ArrayList<IRule> winIs = new ArrayList<>();
		// Itération sur la liste des règles actives
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextWin) {
				winIs.add(element[0]);
			}
		}
		if(winIs.size() == 0)
			return null;
		return winIs;
	}

	/**
	 * Méthode qui va scanner la map et rechercher les différents joueurs en
	 * fonction des règles actives
	 *
	 */
	public void searchPlayers() {
		// Une optimisation possible est de changer les positions des joueurs dès qu'ils ont bougé
		players = new ArrayList<>(); // Clean des anciens joueurs
		ArrayList<IRule> playerIs = whoIsPlayer();
		// Optimisation de l'algo qui évite de parcourir l'entièreté de la map si il n'y
		// a aucune règle "is YOU"
		if (playerIs.size() == 0) {
			return;
		}
		// Itération sur l'entièreté de la map pour rechercher les joueurs
		for (int y = 0; y < cols; y++) {
			for (int x = 0; x < rows; x++) {
				ArrayList<Item> list = board[y][x].getList();
				// Le z correspond à l'index dans la liste d'Item de la cellule et ici on itère
				// sur la liste d'Item
				for (int z = 0; z < list.size(); z++) {
					// Itération sur la liste des joueurs
					for (IRule OnePlayer : playerIs) {
						// Si l'Item est un joueur alors on ajoute un Tuple(x,y,z) qui correspond au
						// coordonnées du joueur sur le plateau
						if (list.get(z) instanceof RealItem && ((RealItem) list.get(z)).isRepresentedBy(OnePlayer)) {
							players.add(new Tuple(x, y, z));
						}
					}
				}
			}
		}

	}

	/**
	 * Méthode qui va rechercher dans la liste de règles actives quel Item "is YOU".
	 */
	public ArrayList<IRule> whoIsPlayer() {
		// Note : on renvoit une ArrayList<IRule> car il est possible d'avoir plusieurs
		// sortes de joueurs en même temps
		// Exemple "BABA is YOU" et "WALL is YOU"
		ArrayList<IRule> playerIs = new ArrayList<>();
		// Itération sur l'ensemble des règles
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
	 *            position en x de l'item à changer de cellule
	 * @param y1
	 *            position en y de l'item à changer de cellule
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
		// Si un item de Règle est bougé alors on rescan les règles et on applique les
		// modifications sur le board
		if (itemChange instanceof IRule) {
			Rules.scanRules(LevelManager.getActivesBoards());
		}
		else if (itemChange instanceof TpGreen) {
			scanTpGreen();
		}
	}

	/**
	 * Méthode qui va rechercher les tp verts qui permettent de déplacer un item jusqu'à l'autre tp vert
	 */
	public void scanTpGreen() {
		if (!LevelManager.getCurrentLeveLName().substring(LevelManager.getCurrentLeveLName().length() - 11)
				.equals("cleanEditor")) { // -11 = "cleanEditor".length()
			correspondingTp = new Tuple[2];
			int index = 0;
			for (int i = 0; i < cols; i++) {
				for (int j = 0; j < rows; j++) {
					for (Item element : board[i][j].getList()) {
						if (element instanceof TpGreen) {
							if(index>1) {
								return;
							}
							correspondingTp[index] = new Tuple(j, i, 0);
							index++; 
						}
					}
				}
			}
		}
	}
	/**
	 * Méthode qui va sauvegarder le niveau en cours dans un fichier .txt
	 */
	public void saveLvl() {
		LevelManager.saveLvl("code" + File.separator + "levels" + File.separator + "saves" + File.separator + "save");
	}
}
