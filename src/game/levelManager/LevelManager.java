package game.levelManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import game.element.Baba;
import game.element.Board;
import game.element.Boundary;
import game.element.Cell;
import game.element.Flag;
import game.element.Item;
import game.element.Rock;
import game.element.TextBaba;
import game.element.TextFlag;
import game.element.TextGoop;
import game.element.TextIs;
import game.element.TextPush;
import game.element.TextRock;
import game.element.TextSink;
import game.element.TextStop;
import game.element.TextWall;
import game.element.TextWin;
import game.element.TextYou;
import game.element.Wall;
import game.element.Water;

/**
 * Classe qui va s'occuper de l'importation des niveaux et de l'�dition
 * 
 */
public class LevelManager {
	
	private static String[] listOfLevels = {"lvl1","lvl2"} ;
	
	/**
	 * M�thode qui va g�n�rer le plateau du jeu gr�ce � un fichier .txt
	 * @param nameLevel le nom du fichier .txt o� se trouve la description du niveau
	 * @return le plateau du jeu g�n�r�
	 * @throws IOException 
	 */
	public static Board readLevel(String nameLevel) // Pour une POO on va s�rement changer le type String par un type Level ou autre � voir...
	{
		
		BufferedReader br = null;
		Cell[][] array = null; //return
		String line;
		String word;
		String[] list;
		Cell cellToChange;
		Item itemToAdd = null;
		int rows; //Lignes
		int cols; //Colonnes
		int rowsOfBoard = 0; //Nombre de lignes de la map
		int colsOfBoard = 0; //Nombre de colonnes de la map
		
		// Ouverture du fichier
        try{
            File file = new File(nameLevel);
            System.out.println(file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file.getAbsolutePath()+ ".txt"));
        } catch (FileNotFoundException fnfex) {
            System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
        }
        
        // Instanciation de tableau en r�cup�rant le nombre de colonnes et de lignes gr�ce � la premi�re ligne du fichier
        // Et pr� remplissage de la map
        try {
        	line = br.readLine();
        	rowsOfBoard = Integer.parseInt(line.split(" ")[0])+2; // +2 pour rajouter les fronti�res
        	colsOfBoard = Integer.parseInt(line.split(" ")[1])+2; 
        	array = new Cell[colsOfBoard][rowsOfBoard];
        	//Pr� remplissage de la map avec des cellules vides(ie qui ne contiennent que l'Item Background)
        	for(int i = 0; i < colsOfBoard; i++)
        	{
        		for(int j = 0; j< rowsOfBoard; j++)
        		{
        			// Remplissage des fronti�res
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
                rows = Integer.parseInt(list[1])+1;
                cols = Integer.parseInt(list[2])+1;
                word = list[0];
                

                
                // Choix de l'Item � ajouter en fonction du mot
                //  Note : Il est possible d'am�liorer le switch pour le remplacer par un constructeur dynamique (en utilisant le principe de refl�ction)
                switch(word)
                {
                case "wall" : itemToAdd = new Wall(TextWall.class); break;
                case "rock" : itemToAdd = new Rock(TextRock.class); break;
                case "is" : itemToAdd = new TextIs(); break;
                case "flag" : itemToAdd = new Flag(TextFlag.class); break;
                case "water" : itemToAdd = new Water(TextGoop.class); break;
                case "text_wall" : itemToAdd = new TextWall(); break;
                case "text_rock" : itemToAdd = new TextRock(); break;
                case "text_baba" : itemToAdd = new TextBaba(); break;
                case "text_goop" : itemToAdd = new TextGoop(); break;
                case "text_flag" : itemToAdd = new TextFlag(); break;
                case "win" : itemToAdd = new TextWin(); break;
                case "push" : itemToAdd = new TextPush(); break;
                case "stop" : itemToAdd = new TextStop(); break;
                case "you" : itemToAdd = new TextYou(); break;
                case "baba" : itemToAdd = new Baba(TextBaba.class); break;
                case "sink" : itemToAdd = new TextSink(); break;
                }
                
             	cellToChange = array[cols][rows];
             	cellToChange.add(itemToAdd);
               	array[cols][rows] = cellToChange;
                 
            }
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
        } 
        
		return new Board(array, rowsOfBoard, colsOfBoard);
	}
	
	public static String[] getListOfLevels()
	{
		return listOfLevels;
	}
}
