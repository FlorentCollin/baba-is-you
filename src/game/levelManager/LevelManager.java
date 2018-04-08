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
 * Classe qui va s'occuper de l'importation des niveaux, de l'édition, des sauvegardes, etc,...
 * 
 */
public class LevelManager {
	
	//A MODIFIER POUR RAJOUTER DES NIVEAUX !
	private static String[] listOfLevels = {"lvl1","lvl2","lvl3","lvl4"};
//	private static String[] listOfLevels = {"testLvl"};
	
	/**
	 * Méthode qui va générer le plateau du jeu grâce à un fichier .txt
	 * @param nameLevel le nom du fichier .txt où se trouve la description du niveau
	 * @return le plateau du jeu généré
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
		Item itemToAdd = null; // on l'initialise car on l'utilise dans un try catch donc il est possible qu'il ne soit pas initialisé après
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
        // Récupération du LevelNumber (ie du numéro du niveau, ex LVL 1, LVL 2,...)
        // Instanciation de tableau en récupérant le nombre de colonnes et de lignes grâce à la première ligne du fichier
        // Et pré remplissage de la map
        try {
        	line = br.readLine();
        	levelNumber = Integer.parseInt(line.split(" ")[1]);
        	line = br.readLine();
        	rowsOfBoard = Integer.parseInt(line.split(" ")[0])+2; // +2 pour rajouter les frontières/bordures de la map
        	colsOfBoard = Integer.parseInt(line.split(" ")[1])+2; 
        	array = new Cell[colsOfBoard][rowsOfBoard]; //Initialisation de la map
        	//Pré remplissage de la map avec des cellules vides(ie qui ne contiennent que l'Item Background) ou des frontières/bordures
        	//Itération sur toutes les cellules de la map
        	for(int i = 0; i < colsOfBoard; i++)
        	{
        		for(int j = 0; j< rowsOfBoard; j++)
        		{
        			// Remplissage des frontières, si il n'y a pas de frontières alors on initialise une cellule vide
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
                
                // Choix de l'Item à ajouter en fonction du mot
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
	 * Méthode qui va retourner la classe d'un item à partir d'un string
	 * @param str le string sur lequel on doit trouver l'item
	 * @return la classe d'un Item
	 */
	public static Class<?> getClassFromString(String str)
	{
		/* Ici on va utiliser le principe de réfléction
		 * Ce principe va nous permettre de trouver une class à partir d'un String
		 * Et donc d'éviter de devoir placer un switch(str) qui aurait du énumérer tous les éléments possible
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
	 * Méthode qui va créer un Item correspondant à un string. Il se peut que le constructeur de l'Item doit avoir un argument
	 * (EX : Wall(TextWall.class), si l'Item a besoin d'un argument on va chercher sa comparaison dans un fichier json
	 * @param str Le string duquel on veut créer l'Item
	 * @return L'Item correspondant au string
	 * @author Le code de la récuparation du json vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
	 */
	public static Item createItem(String str)
	{
		Item itemToReturn = null;
		Class<?> strClass = getClassFromString(str);
		try {
			Constructor<?> strConstructor = strClass.getConstructors()[0];
			if(strConstructor.getParameterTypes().length != 0) //Si le constructeur demande un paramètre "Class" alors on recherche la classe associé à l'Item
			{
				JSONParser parser = new JSONParser();
				try {
					
					Object obj = parser.parse(new FileReader("ressources/CorrespondingTextOrItem.json"));
					JSONObject jsonObject = (JSONObject) obj; //Ouverture du fichier JSON et lecture
					Class<?> CorrespondingItemClass = getClassFromString(jsonObject.get(str).toString()); //Récupération de la class correspondante à l'Item (ex : wall --> TextWall.class)
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
	 * Méthode qui va ouvrir un fichier JSON qui contient les correspondances de chaque Item (Ex : la clé "Wall" donne comme valeur "TextWall")
	 * Et après avoir ouvert le fichier, la méthode va juste renvoyer la valeur de la clé donnée en paramètre
	 * @param str La clé dont on veut connaître la correspondance dans le JSON
	 * @return la valeur de la clé donnée en paramètre
	 * @author Le code de la récuparation du json vient de Mkyong ref : "https://www.mkyong.com/java/json-simple-example-read-and-write-json/"
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
	 * Méthode qui va sauvegarder le niveau en cours dans un fichier externe et qui permettra de le recharger
	 * @param board la map a sauvegarder
	 */
	public static void saveLvl(Board board)
	{
		BufferedWriter bw = null;
		
		try {
			File save = new File("levels/saveLvl.txt"); //Nom du fichier dans lequel on va faire la savegarde
			bw = new BufferedWriter(new FileWriter(save));
			bw.write("LVL " + board.getLevelNumber()); //Ajout de la première ligne qui désigne le numéro du niveau 
			bw.newLine(); 
			bw.write((board.getRows()-2) + " " + (board.getCols()-2)); //Ajout de la deuxième ligne qui désigne le nombre de lignes et de colonnes de la map
			bw.newLine();
			//Itération sur toute la map pour récupérer les Items
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
