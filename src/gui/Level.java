package gui;

import java.io.File;

import game.boardController.Board;
import game.boardController.Cell;
import game.boardController.MoveController;
import game.element.Item;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;

public class Level extends BabaIsYouApp {
	
	protected static Board board;
	protected static Scene game;
	protected static Canvas canvas = new Canvas(960, 960);
	protected static StackPane holder;
	protected static Group root = new Group();
	protected static double CELL_SIZE = 48;
	protected static MediaPlayer soundfx;
	
	/**
	 * M�thode qui va charger un niveau et l'afficher
	 * @param names les noms des fichiers des diff�rentes pronfondeur d'un niveau
	 * @param activateInputs Est ce que les "Inputs" du clavier peuvent �tre utilis�
	 */
	public static void loadLevel(String name, boolean eraseActivesBoards, boolean activateInputs)
	{
		
		
		if(name.equals("save")) //Chargement du niveau sauvegard�
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(name, eraseActivesBoards); //Chargement normal du niveau donn� en param�tre
		board = LevelManager.getActivesBoards().get(0); //R�cup�ration de la premi�re pronfondeur du niveau
		root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		root.getChildren().add(canvas);
		CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows()); //De combien doit �tre la taille d'une cellule
		//Ce StackPane est repr�sente le fond fonc� qu'il y a sur les diff�rents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur � chaque fois qu'on redessine une cellule
		holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		holder.setStyle("-fx-background-color: #1c1f22");
		
		drawBoard(); //Affichage du niveau dans le canvas
		if(board.getLevelNumber() == 0)
			Advice.loadAdviceStage("advice1");
		else if (board.getLevelNumber() == 4)
			Advice.loadAdviceStage("advice2");
			
		if(activateInputs) //On autorise ou non les "Inputs" clavier
			activateKeyInputs();
	}
	
	/**
	 * M�thode qui va dessiner le niveau en cours dans le canvas
	 */
	protected static void drawBoard()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		//Vidage de la cellule dans le canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		//It�ration sur l'enti�ret� du Board pour afficher tous les Items
		for(Cell[] element1 : board.getBoard())
		{
			for(Cell element2 : element1)
			{
				//It�ration sur tous les Items de la cellule
				for(Item element3 : element2.getList())
				{
					//R�cup�ration de l'image � afficher en fonction de son nom
					imageItem = new Image("file:ressources"+File.separatorChar+element3.getName()+".png", CELL_SIZE, CELL_SIZE, true, true);
					//On dessine l'Item en fonction de la taille d'une cellule
					gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
					for(Item element4 : element3.getEffects()) {
						imageItem = new Image("file:ressources"+File.separatorChar+element4.getName()+".png", CELL_SIZE, CELL_SIZE, true, true);
						gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
					}
				}
			}
		}
	}
	
	/**
	 * M�thode qui va redessiner une cellule
	 * @param oneCell la cellule � redessiner
	 */
	protected static void drawCell(Cell oneCell) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		//R�cup�ration des coordonn�es de la cellule
		int x = oneCell.getX();
		int y = oneCell.getY();	
		//Vidage de la cellule dans le canvas
		gc.clearRect(y*CELL_SIZE, x*CELL_SIZE, CELL_SIZE, CELL_SIZE);
		//It�ration sur tous les Items de la cellule
		for(Item element1 : oneCell.getList())
		{
			//R�cup�ration de l'image � afficher en fonction de son nom
			imageItem = new Image("file:ressources"+File.separatorChar+element1.getName()+".png", CELL_SIZE, CELL_SIZE, true, true);
			//On dessine l'Item en fonction de la taille d'une cellule
			gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
			for(Item element2 : element1.getEffects()) {
				imageItem = new Image("file:ressources"+File.separatorChar+element2.getName()+".png", CELL_SIZE, CELL_SIZE, true, true);
				gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
			}
		}
	}
	
	/**
	 * M�thode qui va g�rer les inputs clavier de l'utilisateur
	 */
	public static void activateKeyInputs() {
		String[] listOfLevels = LevelManager.getListOfLevels();
		
		//Gestionnaire d'�v�nements clavier
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int direction; //Variable qui va �tre utilis� dans la fonction move()
			@Override
			public void handle(KeyEvent event) 
			{
				String code = (String) settings.get(event.getCode().toString()); //R�cup�ration de la touche press�e
				if(code == null)
					code = "Not a shortcut"; //Pour pouvoir tout de m�me rentrer dans le switch
				switch(code)
				{
				case "up": direction = 0 ; break;
				case "right": direction = 1 ; break;
				case "down": direction = 2 ; break;
				case "left": direction = 3 ; break;
				case "save": LevelManager.saveLvl("levels"+File.separator+"saves"+File.separator+"save"); 
					return; //Sauvegarde le niveau en cours
				case "load_save": //Charge la sauvegarde
					LevelManager.loadSaveLvl();
					board = LevelManager.getActivesBoards().get(0); 
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "restart": //Reinitialise le niveau en cours
					if(board.getLevelNumber() == -1) {
						if((new File("levels"+File.separator+"editor"+File.separator+"testEditor.txt").exists()));
							LevelManager.readLevel("levels"+File.separator+"editor"+File.separator+"testEditor", true);
					}
					else {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()], true);
					}
					board = LevelManager.getActivesBoards().get(0);
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "previous_world": //Charge la prondeur pr�c�dente
					if(board.getDepthOfLevel()-1>=0)
					{
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel()-1);
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_world": //Charge la pronfondeur suivante
					if(board.getDepthOfLevel()<LevelManager.getActivesBoards().size()-1)
					{
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel()+1);
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_world_mod": //Charge la pronfondeur suivante et si elle n'existe pas alors on charge la premi�re profondeur
					board = LevelManager.getActivesBoards().get((board.getDepthOfLevel()+1)%LevelManager.getActivesBoards().size());
					board.searchPlayers();
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "previous_level": //Charge le niveau pr�c�dent
					if(board.getLevelNumber()-1>=0) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()-1], true);
						board = LevelManager.getActivesBoards().get(0);
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_level": //Charge le niveau suivant
					if(board.getLevelNumber()<listOfLevels.length-1 && board.getLevelNumber() != -1) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1], true);
						board = LevelManager.getActivesBoards().get(0);
						if(board.getLevelNumber() == 4)
							Advice.loadAdviceStage("advice2");
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "F1": //Affiche les astuces
					if(board.getLevelNumber() == 0)
						Advice.loadAdviceStage("advice1");
					else if(board.getLevelNumber() == 4)
						Advice.loadAdviceStage("advice2");
					return;
				case "ESCAPE": //Retourne au menu principal
					Menu.loadMenu();
					return;
				default : return; // Si une autre touche est press�e une ne fait rien
				}
				//It�ration sur la liste des joueurs pour les d�placer
				for(Tuple player: board.getPlayers())
				{
					//D�placement d'un joueur en fonction de la direction
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}
				//Son lors du d�placement
				
				//On redessine les cellules qui ont �t� modifi�es
				for(Tuple cellChanged : board.getAndResetChangedCells())
				{
					drawCell(board.getBoard()[cellChanged.getY()][cellChanged.getX()]);
				}
				board.searchPlayers(); //On recherche les nouveaux joueurs
				if(board.isWin())
				{
					//Si le prochain niveau n'existe pas alors on charge le menu
					if(board.getLevelNumber()>=listOfLevels.length-1 || board.getLevelNumber() == -1)
					{
						Menu.loadMenu();
						return;
					}
					ImageView gif = new ImageView(new Image("file:ressources/GoldenCupAnimation.gif"));
					gif.setX(750);
					gif.setY(30);
					SoundFX.play("success.wav");
					root.getChildren().add(gif);
					//Chargement du prochain niveau
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1], true);
					board = LevelManager.getActivesBoards().get(0); //Charge le prochain niveau
					if(board.getLevelNumber() == 4)
						Advice.loadAdviceStage("advice2");
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
				}
			}});
	}
	
}
