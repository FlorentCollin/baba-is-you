package game.levelManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import game.element.Cell;
import game.element.Flag;
import game.element.Item;
import game.element.Rock;
import game.element.TextBaba;
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
	public Cell[][] readLevel(String nameLevel) // Pour une POO on va sûrement changer le type String par un type Level ou autre à voir...
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
            br = new BufferedReader(new FileReader(file.getAbsolutePath()+ ".txt"));
        } catch (FileNotFoundException fnfex) {
            System.out.println(fnfex.getMessage() + " The file was not found"); // A MODIFIER
        }
        
        // Instanciation de tableau en récupérant le nombre de colonnes et de lignes grâce à la première ligne du fichier
        try {
        	line = br.readLine();
        	rows = Integer.parseInt(line.split(" ")[0]);
        	cols = Integer.parseInt(line.split(" ")[1]);
        	array = new Cell[rows][cols];
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
                case "is" : itemToAdd = new TextIs(); break;
                case "text_wall" : itemToAdd = new TextWall(); break;
                case "text_rock" : itemToAdd = new TextRock(); break;
                case "push" : itemToAdd = new TextPush(); break;
                case "stop" : itemToAdd = new TextStop(); break;
                case "text_baba" : itemToAdd = new TextBaba(); break;
                case "you" : itemToAdd = new TextYou(); break;
                case "win" : itemToAdd = new TextWin(); break;
                case "wall" : itemToAdd = new Wall(); break;
                case "rock" : itemToAdd = new Rock(); break;
                case "flag" : itemToAdd = new Flag(); break;
                case "text_goop" : itemToAdd = new TextGoop(); break;
                case "sink" : itemToAdd = new TextSink(); break;
                }
                if(array[rows][cols] == null) {
                	
                	array[rows][cols] = new Cell(itemToAdd);
                }
                else {
                	cellToChange = array[rows][cols];
                	cellToChange.add(itemToAdd);
                	array[rows][cols] = cellToChange;
                }
                
            }
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage() + " Error reading file"); // A MODIFIER
        } 
        
		return array;
	}

	
}
