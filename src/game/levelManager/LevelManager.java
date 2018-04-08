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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.element.Board;
import game.element.Boundary;
import game.element.Cell;
import game.element.Item;

/**
 * Classe qui va s'occuper de l'importation des niveaux, de l'�dition, des sauvegardes, etc,...
 * 
 */
public class LevelManager {
	
	//A MODIFIER POUR RAJOUTER DES NIVEAUX !
	private static String[] listOfLevels = {"lvl1","lvl2","lvl3","lvl4"};
//	private static String[] listOfLevels = {"testLvl"};
	
	/**
	 * M�thode qui va g�n�rer le plateau du jeu gr�ce � un fichier .txt
	 * @param nameLevel le nom du fichier .txt o� se trouve la description du niveau
	 * @return le plateau du jeu g�n�r�
	 * @throws IOException 
	 */
	public static Board readLevel(String nameLevel) 
	{
		
		BufferedReader br = null;
		Cell[][] array = null; //return
		String line;
		String word;
		String[] list;
		Cell cellToChange;
		Item itemToAdd = null; // on l'initialise car on l'utilise dans un try catch donc il est possible qu'il ne soit pas initialis� apr�s
		int levelNumber = 0;
		int rows; //Lignes
		int cols; //Colonnes
		int rowsOfBoard = 0; //Nombre de lignes de la map
		int colsOfBoard = 0; //Nombre de colonnes de la map
		
		// Ouverture du fichier
        try{
            File file = new File("levels/"+nameLevel);
            System.out.println(file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file.getAbsolutePath()+ ".txt")); //PATH
        } catch (FileNotFoundException fnfex) {
            System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
        }
        // R�cup�ration du LevelNumber (ie du num�ro du niveau, ex LVL 1, LVL 2,...)
        // Instanciation de tableau en r�cup�rant le nombre de colonnes et de lignes gr�ce � la premi�re ligne du fichier
        // Et pr� remplissage de la map
        try {
        	line = br.readLine();
        	levelNumber = Integer.parseInt(line.split(" ")[1]);
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
                rows = Integer.parseInt(list[1]);
                cols = Integer.parseInt(list[2]);
                word = list[0];
                
                // Choix de l'Item � ajouter en fonction du mot
                itemToAdd = createItem(word);
                //Ajout de l'item dans sa cellule
             	cellToChange = array[cols][rows];
             	cellToChange.add(itemToAdd);
               	array[cols][rows] = cellToChange;
                 
            }
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
        } 
        
		return new Board(array, levelNumber, rowsOfBoard, colsOfBoard);
	}
	
	/**
	 * M�thode qui va retourner la classe d'un item � partir d'un string
	 * @param str le string sur lequel on doit trouver l'item
	 * @return la classe d'un Item
	 */
	public static Class<?> getClassFromString(String str)
	{
		/* Ici on va utiliser le principe de r�fl�ction
		 * Ce principe va nous permettre de trouver une class � partir d'un String
		 * Et donc d'�viter de devoir placer un switch(str) qui aurait du �num�rer tous les �l�ments possible
		 * On rend ainsi l'ajout de nouveaux items plus dynamique :)
		 */
		try {
			return Class.forName("game.element."+str);
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
	 * @author Le code de la r�cuparation du json vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
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
					
					Object obj = parser.parse(new FileReader("ressources/CorrespondingTextOrItem.json"));
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
	 * @author Le code de la r�cuparation du json vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 */
	public static String correspondingTextOrItem(String str)
	{
		JSONParser parser = new JSONParser();
			try {
				
				Object obj = parser.parse(new FileReader("ressources/CorrespondingTextOrItem.json"));
				JSONObject jsonObject = (JSONObject) obj; //Ouverture du fichier JSON et lecture
				return jsonObject.get(str).toString();				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
			
		
	}
	
	/**
	 * M�thode qui va sauvegarder le niveau en cours dans un fichier externe et qui permettra de le recharger
	 * @param board la map a sauvegarder
	 */
	public static void saveLvl(Board board)
	{
		BufferedWriter bw = null;
		
		try {
			File save = new File("levels/saveLvl.txt"); //Nom du fichier dans lequel on va faire la savegarde
			bw = new BufferedWriter(new FileWriter(save));
			bw.write("LVL " + board.getLevelNumber()); //Ajout de la premi�re ligne qui d�signe le num�ro du niveau 
			bw.newLine(); 
			bw.write((board.getRows()-2) + " " + (board.getCols()-2)); //Ajout de la deuxi�me ligne qui d�signe le nombre de lignes et de colonnes de la map
			bw.newLine();
			//It�ration sur toute la map pour r�cup�rer les Items
			for(Cell[] element1 : board.getBoard())
			{
				for(Cell element2 : element1)
				{
					for(Item element3 : element2.getList())
					{
						if(! element3.getName().equals("####"))
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
	
	public static String[] getListOfLevels()
	{
		return listOfLevels;
	}
}
