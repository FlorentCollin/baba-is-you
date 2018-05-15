package game.levelManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.boardController.Board;
import game.boardController.Cell;
import game.boardController.Rules;
import game.element.Boundary;
import game.element.Item;

/**
 * Classe qui va s'occuper de l'importation des niveaux, de l'�dition, des
 * sauvegardes, etc,...
 * 
 */
public class LevelManager {
	// private static String[][] listOfLevels = {{"editor/test_0"}};
	// A MODIFIER POUR RAJOUTER DES NIVEAUX !
	private static String[] listOfLevels = { "levels" + File.separator + "lvl0", "levels" + File.separator + "lvl1",
			"levels" + File.separator + "lvl2", "levels" + File.separator + "lvl3", "levels" + File.separator + "lvl4",
			"levels" + File.separator + "lvl5", "levels" + File.separator + "lvl6" };
	private static String currentLeveLName;
	/*
	 * Pourquoi peut-on avoir plusieurs Boards actifs en m�me temps ? Dans le jeu �
	 * partir du niveau 5 on d�couvre un nouvel Item : les portails. Quand on
	 * emprunte un portail on se rend au Board suivant donc on doit forc�ment avoir
	 * une liste de Board pour g�rer les t�l�portations avec les portails
	 */
	private static ArrayList<Board> activesBoards;
	private static JSONObject correspondingItem;

	public static String getCurrentLeveLName() {
		return currentLeveLName;
	}

	public static ArrayList<Board> getActivesBoards() {
		return activesBoards;
	}

	public static void removeLastBoard() {
		activesBoards.remove(activesBoards.size() - 1);
	}

	public static String[] getListOfLevels() {
		return listOfLevels;
	}

	/**
	 * M�thode qui va g�n�rer le plateau du jeu gr�ce � un fichier .txt
	 * 
	 * @param nameLevel
	 *            le nom du fichier .txt o� se trouve la description du niveau
	 * @return le plateau du jeu g�n�r�
	 * @throws IOException
	 */
	public static void readLevel(String nameLevel, boolean eraseActives) {
		currentLeveLName = nameLevel; // On garde en m�moire le chemin d'acc�s du niveau pour pouvoir le recharger
										// plus facilement
		BufferedReader br = null;
		Cell[][] array = null; // return
		String line = null;
		String word;
		String[] list;
		Cell cellToChange;
		Item itemToAdd = null; // on l'initialise car on l'utilise dans un try catch donc il est possible qu'il
								// ne soit pas initialis� apr�s
		int levelNumber = 0;
		int depthOfLevel = 0;
		int rows; // Lignes
		int cols; // Colonnes
		int rowsOfBoard = 0; // Nombre de lignes de la map
		int colsOfBoard = 0; // Nombre de colonnes de la map
		// Ce boolean est utile dans l'�diteur de niveau pour pouvoir ajouter facilement
		// des profondeurs de niveaux suppl�mentaires
		// Si eraseActives == true alors on reinitialise la liste des plateaux actifs
		if (eraseActives || activesBoards == null)
			activesBoards = new ArrayList<>();
		// On va charger chaque partie du Niveau (Pour les ajouter au final �
		// activesBoards)
		// Ouverture du fichier
		try {
			File file = new File(nameLevel);
			br = new BufferedReader(new FileReader(file.getAbsolutePath() + ".txt")); // PATH
		} catch (FileNotFoundException fnfex) {
			System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
		}
		try {
			line = br.readLine(); // Lecture de la premi�re ligne du fichier pour rentrer dans la boucle
			while (line != null) {
				/*
				 * R�cup�ration du LevelNumber (ie du num�ro du niveau, ex LVL 1, LVL 2,...) et
				 * du DepthOfLevel (ie de la prondeur du niveau, ex LVL5_1, LVL5_2, LVL5_3,...)
				 * Instanciation de tableau en r�cup�rant le nombre de colonnes et de lignes
				 * gr�ce � la premi�re ligne du fichier et pr� remplissage de la map
				 */
				try {
					levelNumber = Integer.parseInt(line.split(" ")[1]);
					depthOfLevel = activesBoards.size();
					line = br.readLine();
					rowsOfBoard = Integer.parseInt(line.split(" ")[0]) + 2; // +2 pour rajouter les fronti�res/bordures
																			// de la map
					colsOfBoard = Integer.parseInt(line.split(" ")[1]) + 2;
					array = new Cell[colsOfBoard][rowsOfBoard]; // Initialisation de la map
					// Pr� remplissage de la map avec des cellules vides(ie qui ne contiennent que
					// l'Item Background) ou des fronti�res/bordures
					// It�ration sur toutes les cellules de la map
					for (int i = 0; i < colsOfBoard; i++) {
						for (int j = 0; j < rowsOfBoard; j++) {
							// Remplissage des fronti�res, si il n'y a pas de fronti�res alors on initialise
							// une cellule vide
							if (i == 0 || i == colsOfBoard - 1 || j == 0 || j == rowsOfBoard - 1) {
								array[i][j] = new Cell(new Boundary());
							} else if (array[i][j] == null) {
								array[i][j] = new Cell();
							}
						}
					}
					// Lecture du reste du fichier
					line = br.readLine();
					while (line != null && !(line.split(" ")[0].equals("LVL"))) {
						list = line.split(" ");
						rows = Integer.parseInt(list[1]);
						cols = Integer.parseInt(list[2]);
						word = list[0];

						// Choix de l'Item � ajouter en fonction du mot
						itemToAdd = createItem(word);
						// Ajout de l'item dans sa cellule
						cellToChange = array[cols][rows];
						cellToChange.add(itemToAdd);
						array[cols][rows] = cellToChange;
						line = br.readLine();
					}
				} catch (IOException ioex) {
					System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
				} 
				activesBoards.add(new Board(array, levelNumber, depthOfLevel, rowsOfBoard, colsOfBoard));
			}
		} catch (NumberFormatException e) {
			System.out.println(
					"Your file does not meet standards. Please read the guide to create your own level in text file.");
			System.out.println(
					"When a file does not meet standards, the program will load automatically the first level.");
			readLevel(listOfLevels[0], true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ArrayIndexOutOfBoundsException e1) {
			System.out.println("One item is out of bands : " + line );
			System.out.println("The program will load automatically the first level");
			readLevel(listOfLevels[0], true);
		}
		Rules.scanRules(activesBoards);
		// On recherche les diff�rents joueurs sur tous les boards et on en profite pour
		// mettre � jour les r�gles sur l'ensemble des boards actifs
		for (Board board : activesBoards) {
			board.scanRules();
			board.searchPlayers();
		}
		try {
			br.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * M�thode qui va retourner la classe d'un item � partir d'un string
	 * 
	 * @param str
	 *            le string sur lequel on veut trouver l'item
	 * @return la classe d'un Item
	 */
	public static Class<?> getClassFromString(String str) {
		/*
		 * Ici on va utiliser le principe de r�fl�ction Ce principe va nous permettre de
		 * trouver une class � partir d'un String Et donc d'�viter de devoir placer un
		 * switch(str) qui aurait du �num�rer tous les �l�ments possible On rend ainsi
		 * l'ajout de nouveaux items plus dynamique :) Source :
		 * https://stackoverflow.com/questions/22439436/loading-a-class-from-a-different
		 * -package
		 */
		try {
			return Class.forName("game.element." + str); // Comme les Class Item sont dans un autre package on doit
															// indiquer o� les trouver
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR : A name of an Item is wrong in the text file : " + str);
		}
		return null;
	}

	/**
	 * M�thode qui va cr�er un Item correspondant � un string. Il se peut que le
	 * constructeur de l'Item doit avoir un argument (EX : Wall(TextWall.class), si
	 * l'Item a besoin d'un argument on va chercher sa comparaison dans un fichier
	 * json
	 * 
	 * @param str
	 *            Le string duquel on veut cr�er l'Item
	 * @return L'Item correspondant au string
	 * @author Le code de la lecture du JSON vient de Mkyong ref :
	 *         "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 *         Et on utilise la lib "json-simple-1.1"
	 */
	public static Item createItem(String str) {
		Item itemToReturn = null;
		Class<?> strClass = getClassFromString(str);
		try {
			Constructor<?> strConstructor = strClass.getConstructors()[0];
			if (strConstructor.getParameterTypes().length != 0) { // Si le constructeur demande un param�tre "Class"
																	// alors on recherche la classe associ� � l'Item
				JSONParser parser = new JSONParser();
				try {
					Object obj = parser
							.parse(new FileReader("settings" + File.separatorChar + "CorrespondingTextOrItem.json"));
					JSONObject jsonObject = (JSONObject) obj; // Ouverture du fichier JSON et lecture
					// R�cup�ration de la class correspondante � l'Item (ex : wall --> TextWall.class)
					Class<?> CorrespondingItemClass = getClassFromString(jsonObject.get(str).toString());
					try {
						itemToReturn = (Item) strConstructor.newInstance(CorrespondingItemClass);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {
					System.out.println("Some files are missing, please reinstall the game");
					e.printStackTrace();
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
			} else {
				try {
					itemToReturn = (Item) strConstructor.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace(); // TODO A modifier
				} catch (IllegalAccessException e) {
					e.printStackTrace(); // TODO
				} catch (IllegalArgumentException e) {
					e.printStackTrace(); // TODO
				} catch (InvocationTargetException e) {
					e.printStackTrace(); // TODO
				}
			}
		} catch (SecurityException e1) {
			e1.printStackTrace(); // TODO
		}
		return itemToReturn;
	}

	/**
	 * M�thode qui va ouvrir un fichier JSON qui contient les correspondances de
	 * chaque Item (Ex : la cl� "Wall" donne comme valeur "TextWall") Et apr�s avoir
	 * ouvert le fichier, la m�thode va juste renvoyer la valeur de la cl� donn�e en
	 * param�tre
	 * 
	 * @param str
	 *            La cl� dont on veut conna�tre la correspondance dans le JSON
	 * @return la valeur de la cl� donn�e en param�tre
	 * @author Le code de la lecture du json vient de Mkyong ref :
	 *         "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 */
	public static String correspondingTextOrItem(String str) {
		if (correspondingItem == null) { // Si c'est la premi�re fois qu'on cr�e un Item on doit lire le fichier JSON
											// des correspondances
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser
						.parse(new FileReader("settings" + File.separatorChar + "CorrespondingTextOrItem.json"));
				correspondingItem = (JSONObject) obj; // Ouverture du fichier JSON et lecture
			} catch (FileNotFoundException e) {
				System.out.println("Some files are missing, please reinstall the game");
				e.printStackTrace();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return correspondingItem.get(str).toString(); // On retourne la valeur de la cl� donn�e en param�tre sous la
														// forme d'un String
	}

	// TODO Changer la m�thode saveLvl pour avoir une m�thode unique qui prend en
	// param�tre le nombres de colonnes et de lignes
	// + le nombre de colonnes ou de ligne � retirer

	/**
	 * M�thode qui va sauvegarder le niveau en cours dans un fichier externe et qui
	 * permettra de le recharger
	 * 
	 * @param board
	 *            la map a sauvegarder
	 */
	public static void saveLvl(String name) {
		// On clean le fichier de sauvegarde
		try {
			File saveFile = new File("levels" + File.separator + "saves" + File.separator + "save.txt");
			if (saveFile.exists())
				FileUtils.forceDelete(saveFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// On sauvegarde le niveau dans un nouveau fichier en it�rant sour l'enti�ret�
		// des Boards actifs
		BufferedWriter bw = null;
		File save = new File(name + ".txt"); // Nom du fichier dans lequel on va faire la savegarde
		try {
			bw = new BufferedWriter(new FileWriter(save));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		for (int index = 0; index < activesBoards.size(); index++) {
			try {
				Board board = activesBoards.get(index);
				// Ajout de la premi�re ligne qui d�signe le num�ro du niveau
				bw.write("LVL " + board.getLevelNumber()); 
				bw.newLine();
				// Ajout de la deuxi�me ligne qui d�signe le nombre de lignes et de colonnes de la map
				bw.write((board.getRows() - 2) + " " + (board.getCols() - 2)); 
				// On retire -2 pour ne pas prendre en compte les bordures (fronti�res)
				bw.newLine(); // = "\n"
				// It�ration sur toute la map pour r�cup�rer les Items
				for (int i=0; i<board.getBoard().length; i++) {
					for (int j=0; j<board.getBoard()[0].length; j++) {
						for (Item element3 : board.getBoard()[i][j].getList()) {
							if (!(element3 instanceof Boundary)) // On pourrait optimiser en supprimant directement les
																	// colonnes concern�es
							{
								bw.write(element3.getName() + " " + j + " " + i);
								bw.newLine();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Fermeture du BufferedWriter
		try {
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveLvlEditor(String name) {
		BufferedWriter bw = null;
		// On clean le fichier temporaire
		try {
//			File saveFile = new File("levels" + File.separator + "editor" + File.separator + "testEditor.txt");
			// Nom du fichier dans lequel on va sauvegarder le niveau
			File save = new File("levels" + File.separatorChar + "editor" + File.separatorChar + name + ".txt"); 
			bw = new BufferedWriter(new FileWriter(save));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int index = 0; index < activesBoards.size(); index++) {
			try {
				Board board = activesBoards.get(index);
				bw.write("LVL" + " " + "-1"); // Ajout de la premi�re ligne qui d�signe
																				// le num�ro du niveau
				bw.newLine();
				// Ajout de la deuxi�me ligne qui d�signe le nombre de lignes et de colonnes de
				// la map (Comme on est dans l'�diteur de niveau
				bw.write(18 + " " + 18); 
				// L'utilisateur ne peut pas choisir la taille du niveau et le taile impos�e est
				// de 18x18 cases
				bw.newLine(); // = "\n"
				// It�ration sur toute la map
				for (int i=0; i<board.getBoard().length; i++) {
					for (int j=0; j<board.getBoard()[0].length; j++) {
						for (Item element3 : board.getBoard()[i][j].getList()) {
							// On pourrait optimiser en supprimant directement les colonnes concern�es
							if (!(element3 instanceof Boundary) && i > 4 && j < 20) 
							{
								bw.write(element3.getName() + " " + j + " " + (i - 4));
								bw.newLine();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Fermeture du BufferedWriter
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * M�thode qui va charger le lvl de l'utilisateur pour l'adapter � l'�diteur de
	 * niveau (utilis� notamment par le boutton "LOAD" dans l'�diteur de niveau)
	 * D�roulement de la m�thode : 1) On copie le contenu du fichier "cleanEditor"
	 * pour pouvoir rajouter le niveau de l'utilisateur � la place de la partie vide
	 * o� l'utilisateur peut normalement modifier le niveau 2) On lit le fichier de
	 * l'utilisateur et on le re�crit en modifiant le num�ro des colonnes et des
	 * lignes pour qu'ils correspondent au format de l'�diteur de niveau ATTENTION
	 * le niveau � charger doit �tre de la taille 18x18 cases !
	 * 
	 * @param fileName
	 *            le nom du fichier � charger dans l'�diteur
	 * @return le nom du fichier qui contient l'�diteur avec le niveau de
	 *         l'utilisateur dans la partie modifiable
	 */
	public static String loadUserLvl(File fileName) {
		// TODO Am�liorer cette m�thode car elle est fort compliqu�, il doit s�rement y
		// avoir un moyen beaucoup plus simple :) mais au moins elle fonctionne
		// 1) On copier le fichier "cleanEditor"
		File newCleanEditor = new File("levels" + File.separator + "editor" + File.separator + "cleanEditor.txt");

		// 2) On lit le fichier de l'utilisateur
		String nameLevel = fileName.getAbsolutePath().substring(0, fileName.getAbsolutePath().length() - 4);
		readLevel(nameLevel, true);

		// 3) On re�crit le niveau sous la forme propre � l'�diteur de niveau
		BufferedReader br = null;
		BufferedWriter bw = null;
		// Ouverture du nouveau fichier
		try {
			bw = new BufferedWriter(new FileWriter(newCleanEditor));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		for (Board board : activesBoards) { // It�ration sur l'ensemble des boards actifs (des mondes parrall�les
											// actifs)
			if (board.getRows() != 20 || board.getCols() != 20) {
				System.out.println(
						"Your file does not have the correct dimensions for the editor (the right dimensions are 18x18). "
								+ "The program will automatically load the first level.");
			}
			// Copie de l'�diteur vierge
			try {
				br = new BufferedReader(new FileReader("levels" + File.separator + "cleanEditor.txt"));
				String line = br.readLine();
				bw.write("LVL -1");
				bw.newLine();
				while ((line = br.readLine()) != null) {
					bw.write(line);
					bw.newLine();
				}
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// Ajout des items sur l'�diteur vierge
			try {
				// It�ration sur toute la map
				for (int i = 0; i<board.getBoard().length; i++) {
					for (int j = 0; j<board.getBoard()[0].length; j++) {
						for (Item element3 : board.getBoard()[i][j].getList()) {
							if (!(element3 instanceof Boundary)) {
								// Ajout dans le fichier des items. +4 pour faire correspondre par rapport � la map
								bw.write(element3.getName() + " " + j + " " + (i + 4));
								bw.newLine();
							}
						}
					}
				}
				activesBoards = null; // RESET
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Fermeture du BufferedWriter
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// On retourne le string modifier pour supprimer le .txt
		return newCleanEditor.getAbsolutePath().substring(0, newCleanEditor.getAbsolutePath().length() - 4);
	}

	/**
	 * M�thode qui va charger la sauvegarde, si elle n'existe pas alors on charge le
	 * premier niveau
	 */
	public static void loadSaveLvl() {
		File file = new File("levels" + File.separatorChar + "saves" + File.separator + "save.txt");
		if (!file.exists()) { // S'il n'y a pas de sauvegardes on charge le premier niveau par d�faut
			readLevel(getListOfLevels()[0], true);
			return;
		} else {
			readLevel("levels" + File.separator + "saves" + File.separator + "save", true);
		}
	}
}
