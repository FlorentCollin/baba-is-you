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
 * Classe qui va s'occuper de l'importation des niveaux, de l'�dition, des sauvegardes, etc,...
 * 
 */
public class LevelManager {
//	private static String[][] listOfLevels = {{"editor/test_0"}};
	//A MODIFIER POUR RAJOUTER DES NIVEAUX !
	private static String[][] listOfLevels = {{"levels"+File.separator+"lvl0"},{"levels"+File.separator+"lvl1"},{"levels"+File.separator+"lvl2"},{"levels"+File.separator+"lvl3"},{"levels"+File.separator+"lvl4_0", "levels"+File.separator+"lvl4_1"}};
	/*Pourquoi peut-on avoir plusieurs Boards actifs en m�me temps ? Dans le jeu � partir du niveau 5 on d�couvre un nouvel Item : les portails.
	 * Quand on emprunte un portail on se rend au Board suivant donc on doit forc�ment avoir une liste de Board pour g�rer les t�l�portations avec les portails*/
	private static Board[] activesBoards;
	
	public static Board[] getActivesBoards()
	{
		return activesBoards;
	}
	
	public static String[][] getListOfLevels()
	{
		return listOfLevels;
	}

	/**
	 * M�thode qui va g�n�rer le plateau du jeu gr�ce � un fichier .txt
	 * @param nameLevel le nom du fichier .txt o� se trouve la description du niveau
	 * @return le plateau du jeu g�n�r�
	 * @throws IOException 
	 */
	public static void readLevel(String[] namesLevels) 
	{
		
		BufferedReader br = null;
		Cell[][] array = null; //return
		String line;
		String word;
		String[] list;
		Cell cellToChange;
		Item itemToAdd = null; // on l'initialise car on l'utilise dans un try catch donc il est possible qu'il ne soit pas initialis� apr�s
		int levelNumber = 0;
		int depthOfLevel = 0;
		int rows; //Lignes
		int cols; //Colonnes
		int rowsOfBoard = 0; //Nombre de lignes de la map
		int colsOfBoard = 0; //Nombre de colonnes de la map
		activesBoards = new Board[namesLevels.length]; //On initialise le nombre de board qui vont �tre actifs simultan�ment
		//On va charger chaque partie du Niveau (Pour les ajouter au final � activesBoards) 
		for(int index = 0; index<namesLevels.length; index++)
		{
			// Ouverture du fichier
			try{
				File file = new File(namesLevels[index]);
				br = new BufferedReader(new FileReader(file.getAbsolutePath()+ ".txt")); //PATH
			} catch (FileNotFoundException fnfex) {
				System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
			}
			// R�cup�ration du LevelNumber (ie du num�ro du niveau, ex LVL 1, LVL 2,...) et du DepthOfLevel (ie de la prondeur du niveau, ex LVL5_1, LVL5_2, LVL5_3,...)
			// Instanciation de tableau en r�cup�rant le nombre de colonnes et de lignes gr�ce � la premi�re ligne du fichier
			// Et pr� remplissage de la map
			try {
				line = br.readLine();
				levelNumber = Integer.parseInt(line.split(" ")[1]);
				depthOfLevel = Integer.parseInt(line.split(" ")[2]);
				line = br.readLine();
				rowsOfBoard = Integer.parseInt(line.split(" ")[0])+2; // +2 pour rajouter les fronti�res/bordures de la map
				colsOfBoard = Integer.parseInt(line.split(" ")[1])+2; 
				array = new Cell[colsOfBoard][rowsOfBoard]; //Initialisation de la map
				//Pr� remplissage de la map avec des cellules vides(ie qui ne contiennent que l'Item Background) ou des fronti�res/bordures
				//It�ration sur toutes les cellules de la map
				for(int i = 0; i < colsOfBoard; i++)
				{
					for(int j = 0; j< rowsOfBoard; j++)
					{
						// Remplissage des fronti�res, si il n'y a pas de fronti�res alors on initialise une cellule vide
						if(i == 0 || i == colsOfBoard-1 || j == 0 || j == rowsOfBoard-1)
						{
							array[i][j] = new Cell(i,j, new Boundary());
						}
						else if(array[i][j] == null)
							array[i][j] = new Cell(i,j);
					}
				}
				// Lecture du reste du fichier
				while((line = br.readLine()) != null)
				{
					list = line.split(" ");
					cols = Integer.parseInt(list[1]);
					rows = Integer.parseInt(list[2]);
					word = list[0];
					
					// Choix de l'Item � ajouter en fonction du mot
					itemToAdd = createItem(word);
					//Ajout de l'item dans sa cellule
					cellToChange = array[rows][cols];
					cellToChange.add(itemToAdd);
					array[rows][cols] = cellToChange;
					
				}
				br.close();
			} catch (IOException ioex) {
				System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
			} 
			activesBoards[index] = new Board(array, levelNumber, depthOfLevel, rowsOfBoard, colsOfBoard);
		}
		Rules.scanRules(activesBoards);
		//On recherche les diff�rents joueurs sur tous les boards et on en profite pour mettre � jour les r�gles sur l'ensemble des boards actifs
		for(Board board : activesBoards)
		{
			board.scanRules();
			board.searchPlayers();
		}
	}
	
	/**
	 * M�thode qui va retourner la classe d'un item � partir d'un string
	 * @param str le string sur lequel on veut trouver l'item
	 * @return la classe d'un Item
	 */
	public static Class<?> getClassFromString(String str)
	{
		/* Ici on va utiliser le principe de r�fl�ction
		 * Ce principe va nous permettre de trouver une class � partir d'un String
		 * Et donc d'�viter de devoir placer un switch(str) qui aurait du �num�rer tous les �l�ments possible
		 * On rend ainsi l'ajout de nouveaux items plus dynamique :)
		 * Source : https://stackoverflow.com/questions/22439436/loading-a-class-from-a-different-package
		 */
		try {
			return Class.forName("game.element."+str); //Come les Class Item sont dans un autre package on doit indiquer o� les trouver
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * M�thode qui va cr�er un Item correspondant � un string. Il se peut que le constructeur de l'Item doit avoir un argument
	 * (EX : Wall(TextWall.class), si l'Item a besoin d'un argument on va chercher sa comparaison dans un fichier json
	 * @param str Le string duquel on veut cr�er l'Item
	 * @return L'Item correspondant au string
	 * @author Le code de la lecture du JSON vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 * Et on utilise la lib "json-simple-1.1"
	 */
	public static Item createItem(String str)
	{
		Item itemToReturn = null;
		Class<?> strClass = getClassFromString(str);
		try {
			Constructor<?> strConstructor = strClass.getConstructors()[0];
			if(strConstructor.getParameterTypes().length != 0) //Si le constructeur demande un param�tre "Class" alors on recherche la classe associ� � l'Item
			{
				JSONParser parser = new JSONParser();
				try {
					
					Object obj = parser.parse(new FileReader("ressources"+File.separatorChar+"CorrespondingTextOrItem.json"));
					JSONObject jsonObject = (JSONObject) obj; //Ouverture du fichier JSON et lecture
					Class<?> CorrespondingItemClass = getClassFromString(jsonObject.get(str).toString()); //R�cup�ration de la class correspondante � l'Item (ex : wall --> TextWall.class)
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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			else {
				try {
					itemToReturn = (Item) strConstructor.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		return itemToReturn;
	}
	
	/**
	 * M�thode qui va ouvrir un fichier JSON qui contient les correspondances de chaque Item (Ex : la cl� "Wall" donne comme valeur "TextWall")
	 * Et apr�s avoir ouvert le fichier, la m�thode va juste renvoyer la valeur de la cl� donn�e en param�tre
	 * @param str La cl� dont on veut conna�tre la correspondance dans le JSON
	 * @return la valeur de la cl� donn�e en param�tre
	 * @author Le code de la lecture du json vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 */
	public static String correspondingTextOrItem(String str)
	{
		JSONParser parser = new JSONParser();
			try {
				
				Object obj = parser.parse(new FileReader("ressources"+File.separatorChar+"CorrespondingTextOrItem.json"));
				JSONObject jsonObject = (JSONObject) obj; //Ouverture du fichier JSON et lecture
				return jsonObject.get(str).toString(); //On retourne la valeur de la cl� donn�e en param�tre sous la forme d'un String			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	//TODO Changer la m�thode saveLvl pour avoir une m�thode unique qui prend en param�tre le nombres de colonnes et de lignes 
	//+ le nombre de colonnes ou de ligne � retirer
	
	/**
	 * M�thode qui va sauvegarder le niveau en cours dans un fichier externe et qui permettra de le recharger
	 * @param board la map a sauvegarder
	 */
	public static void saveLvl(String name)
	{
		//On clean le dossier de sauvegarde pour �craser correctement les diff�rentes profondeurs de niveaux
		File saveDir = new File("levels"+File.separatorChar+"saves");
		try {
			FileUtils.forceDelete(saveDir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		saveDir.mkdir();

		//On sauvegarde le niveau dans un nouveau fichier en it�rant sour l'enti�ret� des Boards actifs
		BufferedWriter bw = null;
		for(int index = 0; index<activesBoards.length; index++)
		{
			try {
				Board board = activesBoards[index];
				File save = new File(name+"_"+board.getDepthOfLevel()+".txt"); //Nom du fichier dans lequel on va faire la savegarde
				bw = new BufferedWriter(new FileWriter(save));
				bw.write("LVL " + board.getLevelNumber()+ " " + board.getDepthOfLevel()); //Ajout de la premi�re ligne qui d�signe le num�ro du niveau 
				bw.newLine(); 
				bw.write((board.getRows()-2) + " " + (board.getCols()-2)); //Ajout de la deuxi�me ligne qui d�signe le nombre de lignes et de colonnes de la map
				// On retire -2 pour ne pas prendre en compte les bordures (fronti�res)
				bw.newLine(); // = "\n"
				//It�ration sur toute la map pour r�cup�rer les Items
				for(Cell[] element1 : board.getBoard())
				{
					for(Cell element2 : element1)
					{
						for(Item element3 : element2.getList())
						{
							if(!(element3 instanceof Boundary)) //On pourrait optimiser en supprimant directement les colonnes concern�es
							{
								bw.write(element3.getName() + " " + (element2.getY()) + " " + (element2.getX()));
								bw.newLine();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				}
				catch (Exception e) {}
			}
		}
	}
	
	public static void saveLvlEditor(String name) {
		
		BufferedWriter bw = null;
		for(int index = 0; index<activesBoards.length; index++)
		{
			try {
				Board board = activesBoards[index];
				File save = new File("levels"+File.separatorChar+"editor"+File.separatorChar+name+"_"+board.getDepthOfLevel()+".txt"); //Nom du fichier dans lequel on va sauvegarder le niveau
				bw = new BufferedWriter(new FileWriter(save));
				bw.write(name + " " + board.getLevelNumber()+ " " + board.getDepthOfLevel()); //Ajout de la premi�re ligne qui d�signe le num�ro du niveau 
				bw.newLine(); 
				bw.write(18 + " " + 18); //Ajout de la deuxi�me ligne qui d�signe le nombre de lignes et de colonnes de la map (Comme on est dans l'�diteur de niveau
				// L'utilisateur ne peut pas choisir la taille du niveau et le taile impos�e est de 18x18 cases
				bw.newLine(); // = "\n"
				//It�ration sur toute la map
				for(Cell[] element1 : board.getBoard())
				{
					for(Cell element2 : element1)
					{
						for(Item element3 : element2.getList())
						{
							if(!(element3 instanceof Boundary) && element2.getX()>4 && element2.getY()<20) //On pourrait optimiser en supprimant directement les colonnes concern�es
							{
								bw.write(element3.getName() + " " + (element2.getY()) + " " + (element2.getX()-4));
								bw.newLine();
							}
						}
					}
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				}
				catch (Exception e) {}
			}
		}
	}
	
	/**
	 * M�thode qui va charger le lvl de l'utilisateur pour l'adapter � l'�diteur de niveau
	 * (utilis� notamment par le boutton "LOAD" dans l'�diteur de niveau)
	 * D�roulement de la m�thode :
	 * 1) On copie le fichier "cleanEditor" pour pouvoir rajouter le niveau de l'utilisateur � la place de la partie vide o�
	 * l'utilisateur peut normalement modifier le niveau
	 * 2) On lit le fichier de l'utilisateur et on le re�crit en modifiant le num�ro des colonnes et des lignes pour qu'ils correspondent
	 * au format de l'�diteur de niveau
	 * ATTENTION le niveau � charger doit �tre de la taille 18x18 cases !
	 */
	public static String loadUserLvl(File fileName) {
		// 1) On copier le fichier "cleanEditor"
		File newCleanEditor = new File("levels"+File.separator+"editor"+File.separator+"cleanEditor_0.txt");
		try {
			FileUtils.copyFile(new File("levels"+File.separator+"cleanEditor.txt"), newCleanEditor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 2) On lit le fichier de l'utilisateur
		String[] namesLevels = {fileName.getAbsolutePath().substring(0, fileName.getAbsolutePath().length()-4)}; 
		readLevel(namesLevels);
		
		// 3) On re�crit le niveau sous la forme propre � l'�diteur de niveau
		BufferedWriter bw = null;
		Board board = activesBoards[0];
		try {
			bw = new BufferedWriter(new FileWriter(newCleanEditor, true));
			bw.newLine(); // = "\n"
			//It�ration sur toute la map
			for(Cell[] element1 : board.getBoard())
			{
				for(Cell element2 : element1)
				{
					for(Item element3 : element2.getList())
					{
						if(!(element3 instanceof Boundary))
						{
							bw.write(element3.getName() + " " + (element2.getY()) + " " + (element2.getX()+4));
							bw.newLine();
						}
					}
				}
			}
			bw.close();
			activesBoards = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newCleanEditor.getAbsolutePath().substring(0 ,newCleanEditor.getAbsolutePath().length()-4);
	}
	
	/**
	 * M�thode qui va charger la sauvegarde, si elle n'existe pas alors on charge le premier niveau
	 */
	public static void loadSaveLvl()
	{
		File file = new File("levels"+File.separatorChar+"saves");
		if(file.list().length==0) //S'il n'y a pas de sauvegardes on charge le premier niveau par d�faut
		{
			readLevel(getListOfLevels()[0]);
			return;
		}
		String[] listActivesBoards = file.list();
		for(int index = 0; index<listActivesBoards.length; index++)
		{
			listActivesBoards[index] = "levels"+File.separator+"saves"+File.separator+listActivesBoards[index].substring(0, listActivesBoards[index].length()-4);
		}
		readLevel(listActivesBoards);
		
	}
	

//	public static void loadEditor() {
//		String[] cleanEditor = {"cleanEditor"};
//		readLevel(cleanEditor);
//	}

}
