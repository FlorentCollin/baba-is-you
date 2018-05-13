package gui;

import java.io.File;

import game.boardController.Board;
import game.boardController.Cell;
import game.boardController.MoveController;
import game.element.Item;
import game.element.TextFlag;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;

public class Level extends BabaIsYouApp {

	protected static Board board;
	protected static Scene game;
	protected static Canvas canvas = new Canvas(960, 960);
	protected static StackPane holder;
	protected static double CELL_SIZE = 48;
	protected static MediaPlayer soundfx;

	/**
	 * M�thode qui va charger un niveau et l'afficher
	 * 
	 * @param name
	 *            le nom du fichier � charger
	 * @param eraseActivesBoards
	 *            Est ce qu'on doit supprimer les boards actives ou juste rajouter
	 *            le niveau � la suite de ceux-ci
	 * @param activateInputs
	 *            Est ce que les "Inputs" du clavier peuvent �tre utilis�
	 */
	public static void loadLevel(String name, boolean eraseActivesBoards, boolean activateInputs) {

		if (name.equals("save")) // Chargement de la sauvegarde
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(name, eraseActivesBoards); // Chargement normal du niveau donn� en param�tre
		board = LevelManager.getActivesBoards().get(0); // R�cup�ration de la premi�re pronfondeur du niveau
		// Initialisation
		root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		root.getChildren().add(canvas);
		CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows()); // De combien doit �tre la taille
																						// d'une cellule
		/*
		 * Ce StackPane est repr�sente le fond fonc� qu'il y a sur les diff�rents
		 * niveaux Cela permet de ne pas devoir afficher un fond de cette couleur �
		 * chaque fois qu'on redessine une cellule
		 */
		holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		holder.setStyle("-fx-background-color: #1c1f22");

		drawBoard(); // Affichage du niveau dans le canvas
		// Chargement de la fen�tre d'astuce
		if (board.getLevelNumber() == 0)
			Advice.loadAdviceStage("advice1");
		else if (board.getLevelNumber() == 4)
			Advice.loadAdviceStage("advice2");

		if (activateInputs) // On autorise ou non les "Inputs" clavier
			activateKeyInputs();
	}

	/**
	 * M�thode qui va dessiner le niveau en cours dans le canvas
	 */
	protected static void drawBoard() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		// Reinitialisation (ie : on supprime tout ce qui avait �t� dessin�)
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// It�ration sur l'enti�ret� du Board pour afficher tous les Items
		Cell[][] array = board.getBoard();
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[0].length; j++) {
				// It�ration sur tous les Items de la cellule
				for (Item element3 : array[j][i].getList()) {
					// R�cup�ration de l'image � afficher en fonction de son nom
					imageItem = new Image("file:ressources" + File.separatorChar + element3.getName() + ".png",
							CELL_SIZE, CELL_SIZE, true, true);
					// On dessine l'Item en fonction de la taille d'une cellule
					gc.drawImage(imageItem, i * CELL_SIZE, j * CELL_SIZE);
					// for(Item element4 : element3.getEffects()) {
					// imageItem = new
					// Image("file:ressources"+File.separatorChar+element4.getName()+".png",
					// CELL_SIZE, CELL_SIZE, true, true);
					// gc.drawImage(imageItem, element2.getY()*CELL_SIZE,
					// element2.getX()*CELL_SIZE);
					// }
				}
			}
		}
	}

	/**
	 * M�thode qui va redessiner une cellule
	 * 
	 * @param oneCell
	 *            la cellule � redessiner
	 */
	protected static void drawCell(int x, int y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		// Reinitialisation de la cellule dans le canvas
		gc.clearRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
		// It�ration sur tous les Items de la cellule
		for (Item element1 : board.getCell(x, y).getList()) {
			// R�cup�ration de l'image � afficher en fonction de son nom
			imageItem = new Image("file:ressources" + File.separatorChar + element1.getName() + ".png", CELL_SIZE,
					CELL_SIZE, true, true);
			// On dessine l'Item en fonction de la taille d'une cellule
			gc.drawImage(imageItem, x * CELL_SIZE, y * CELL_SIZE);
			// for(Item element2 : element1.getEffects()) {
			// imageItem = new
			// Image("file:ressources"+File.separatorChar+element2.getName()+".png",
			// CELL_SIZE, CELL_SIZE, true, true);
			// gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
			// }
		}
	}

	/**
	 * M�thode qui va g�rer les inputs clavier de l'utilisateur
	 */
	public static void activateKeyInputs() {
		String[] listOfLevels = LevelManager.getListOfLevels();

		// Gestionnaire d'�v�nements clavier
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int direction; // Variable qui va �tre utilis� dans la fonction move()

			@Override
			public void handle(KeyEvent event) {
				String code = (String) settings.getUserSettings().get((event.getCode().toString())); // R�cup�ration de la touche press�e
				if (code == null)
					code = "Not a shortcut"; // Pour pouvoir tout de m�me rentrer dans le switch
				switch (code) {
				case "up":
					direction = 0;
					break;
				case "right":
					direction = 1;
					break;
				case "down":
					direction = 2;
					break;
				case "left":
					direction = 3;
					break;
				case "save":
					LevelManager.saveLvl("levels" + File.separator + "saves" + File.separator + "save");
					return; // Sauvegarde le niveau en cours
				case "load_save": // Charge la sauvegarde
					LevelManager.loadSaveLvl();
					board = LevelManager.getActivesBoards().get(0);
					CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "restart": // Reinitialise le niveau en cours
					if(board.getLevelNumber() != -1)
						LevelManager.readLevel(LevelManager.getListOfLevels()[board.getLevelNumber()], true);
					else
						LevelManager.readLevel(LevelManager.getCurrentLeveLName(), true);
					board = LevelManager.getActivesBoards().get(0);
					CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "previous_world": // Charge la prondeur pr�c�dente
					if (board.getDepthOfLevel() - 1 >= 0) {
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel() - 1);
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_world": // Charge la pronfondeur suivante
					if (board.getDepthOfLevel() < LevelManager.getActivesBoards().size() - 1) {
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel() + 1);
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_world_mod": // Charge la pronfondeur suivante et si elle n'existe pas alors on charge la
										// premi�re profondeur
					board = LevelManager.getActivesBoards()
							.get((board.getDepthOfLevel() + 1) % LevelManager.getActivesBoards().size());
					board.searchPlayers();
					CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "previous_level": // Charge le niveau pr�c�dent
					if (board.getLevelNumber() - 1 >= 0) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber() - 1], true);
						board = LevelManager.getActivesBoards().get(0);
						CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "next_level": // Charge le niveau suivant
					if (board.getLevelNumber() < listOfLevels.length - 1 && board.getLevelNumber() != -1) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber() + 1], true);
						board = LevelManager.getActivesBoards().get(0);
						if (board.getLevelNumber() == 4) // Affichage des astuces
							Advice.loadAdviceStage("advice2");
						CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
						drawBoard(); // Reaffichage du jeu
					}
					return;
				case "F1": // Affiche les astuces
					if (board.getLevelNumber() == 0)
						Advice.loadAdviceStage("advice1");
					else if (board.getLevelNumber() == 4)
						Advice.loadAdviceStage("advice2");
					return;
				case "ESCAPE": // Retourne au menu principal
					Menu.loadMenu();
					return;
				default:
					return; // Si une autre touche est press�e on ne fait rien
				}
				// It�ration sur la liste des joueurs pour les d�placer
				for (Tuple player : board.getPlayers()) {
					// D�placement d'un joueur des joueurs en fonction de la direction
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}

				// On redessine les cellules qui ont �t� modifi�es
				for (Tuple cellChanged : board.getAndResetChangedCells()) {
					drawCell(cellChanged.getX(), cellChanged.getY());
				}
				board.searchPlayers(); // On recherche les nouveaux joueurs
				if (board.isWin()) {
					// SUCCESS
					if(!(board.whoIsWin() instanceof TextFlag)) {
						if(success.unlock("DifferentWayToWin"))
							showSuccessUnlocked();
					}
					if(board.getLevelNumber() == 0) {
						if(success.unlock("FinishFirstLevel")) {
							showSuccessUnlocked();
						}
					}
					if(board.getLevelNumber() == listOfLevels.length-1) {
						if(success.unlock("BeatTheGame")) {
							showSuccessUnlocked();
						}
					}
					// Si le prochain niveau n'existe pas alors on charge le menu
					if (board.getLevelNumber() >= listOfLevels.length - 1 || board.getLevelNumber() == -1) {
						Menu.loadMenu();
						return;
					}
						
					// Chargement du prochain niveau
					LevelManager.readLevel(listOfLevels[board.getLevelNumber() + 1], true);
					board = LevelManager.getActivesBoards().get(0); // Charge le prochain niveau
					if (board.getLevelNumber() == 4)
						Advice.loadAdviceStage("advice2");
					CELL_SIZE = canvas.getHeight() / Math.max(board.getCols(), board.getRows());
					drawBoard();
				}
			}
		});
	}

}
