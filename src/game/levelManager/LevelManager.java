package game.levelManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import game.element.Baba;
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

/**
 * Classe qui va s'occuper de l'importation des niveaux et de l'édition
 * 
 */
public class LevelManager {
	/**
	 * Méthode qui va générer le plateau du jeu grâce à un fichier .txt
	 * @param nameLevel le nom du fichier .txt où se trouve la description du niveau
	 * @return le plateau du jeu généré
	 * @throws IOException 
	 */
	public static Cell[][] readLevel(String nameLevel) // Pour une POO on va sûrement changer le type String par un type Level ou autre à voir...
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
		
		// Ouverture du fichier
        try{
            File file = new File(nameLevel);
            System.out.println(file.getAbsolutePath());
            br = new BufferedReader(new FileReader(file.getAbsolutePath()+ ".txt"));
        } catch (FileNotFoundException fnfex) {
            System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
        }
        
        // Instanciation de tableau en récupérant le nombre de colonnes et de lignes grâce à la première ligne du fichier
        try {
        	line = br.readLine();
        	rows = Integer.parseInt(line.split(" ")[0]);
        	cols = Integer.parseInt(line.split(" ")[1]);
        	array = new Cell[cols][rows];
        	for(int i = 0; i < cols; i++)
        	{
        		for(int j = 0; j< rows; j++)
        		{
        			if(array[i][j] == null)
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
                switch(word)
                {
                case "wall" : itemToAdd = new Wall(rows, cols); break;
                case "rock" : itemToAdd = new Rock(rows, cols); break;
                case "is" : itemToAdd = new TextIs(rows, cols); break;
                case "flag" : itemToAdd = new Flag(rows, cols); break;
                case "text_wall" : itemToAdd = new TextWall(rows, cols); break;
                case "text_rock" : itemToAdd = new TextRock(rows, cols); break;
                case "text_baba" : itemToAdd = new TextBaba(rows, cols); break;
                case "text_goop" : itemToAdd = new TextGoop(rows, cols); break;
                case "text_flag" : itemToAdd = new TextFlag(rows, cols); break;
                case "win" : itemToAdd = new TextWin(rows, cols); break;
                case "push" : itemToAdd = new TextPush(rows, cols); break;
                case "stop" : itemToAdd = new TextStop(rows, cols); break;
                case "you" : itemToAdd = new TextYou(rows, cols); break;
                case "baba" : itemToAdd = new Baba(rows, cols); break;
                case "sink" : itemToAdd = new TextSink(rows, cols); break;
                }
                
             	cellToChange = array[cols][rows];
             	cellToChange.addItem(itemToAdd);
               	array[cols][rows] = cellToChange;
                 
            }
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
        } 
        
		return array;
	}
}
