package gui;

import java.io.File;
import java.io.IOException;

import game.boardController.Board;
import game.boardController.Cell;
import game.boardController.MoveController;
import game.element.Item;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Classe principale qui va gérer le jeu en mode interface graphique.
 * 
 */
public class BabaIsYouApp extends Application {
	
	private final static int CELL_SIZE = 48;
	private static Board board;
	private static Scene menu, game;
	private static Stage primaryStage;
	private static Class<?> thisClass;
	
	@Override
	public void start(Stage Stage) throws Exception {
			primaryStage = Stage;
			thisClass = getClass();
			loadMenu();
			primaryStage.setTitle("BABA IS YOU");
//			primaryStage.setMaximized(true);
//			primaryStage.setFullScreen(true);
//			scene.setCursor(Cursor.NONE);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
	}
	
	public static void loadMenu() {
		Parent root = null;
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
	}
	
	
	public static void loadLevel(int index)
	{
		String[][] listOfLevels = LevelManager.getListOfLevels();
		if(index == -1)
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(listOfLevels[index]);
		board = LevelManager.getActivesBoards()[0];
		Group root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		Canvas canvas = new Canvas(960, 960);
		root.getChildren().add(canvas);
		
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		
		holder.setStyle("-fx-background-color: #1c1f22");
		
		drawBoard(canvas, board);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int x;
			int y;
			int direction;
			Cell[][] map;
			Image imageItem;
			@Override
			public void handle(KeyEvent event) 
			{
				String code = event.getCode().toString();
				switch(code)
				{
				case "Z": direction = 0 ; break;
				case "D": direction = 1 ; break;
				case "S": direction = 2 ; break;
				case "Q": direction = 3 ; break;
				case "X": LevelManager.saveLvl(); return; //Sauvegarde le niveau en cours
				case "L": //Charge la sauvegarde
					LevelManager.loadSaveLvl();
					board = LevelManager.getActivesBoards()[0]; 
					drawBoard(canvas, board);
					return;
				case "R": //Reinitialise le niveau en cours
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()]);
					board = LevelManager.getActivesBoards()[0];
					drawBoard(canvas, board);
					return;
				case "A":
					if(board.getDepthOfLevel()-1>=0)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()-1];
						board.searchPlayers();
						drawBoard(canvas, board);
					}
					return;
				case "E":
					if(board.getDepthOfLevel()<LevelManager.getActivesBoards().length-1)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()+1];
						board.searchPlayers();
						drawBoard(canvas, board);
					}
					return;
				case "SPACE":
					board = LevelManager.getActivesBoards()[(board.getDepthOfLevel()+1)%LevelManager.getActivesBoards().length];
					board.searchPlayers();
					drawBoard(canvas, board);
					return;
				case "P": //Charge le niveau précédent
					if(board.getLevelNumber()-1>=0) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()-1]);
						board = LevelManager.getActivesBoards()[0];
						drawBoard(canvas, board);
					}
					return;
				case "N": //Charge le niveau suivant
					if(board.getLevelNumber()<listOfLevels.length-1) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
						board = LevelManager.getActivesBoards()[0];
						drawBoard(canvas, board);
					}
					return;
				case "ESCAPE": //Retourne au menu principal
					loadMenu();
					return;
				default : return; // Si une autre touche est pressée une ne fait rien
				}
				
				for(Tuple player: board.getPlayers())
				{
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}
				map = board.getBoard();
				for(Tuple cellChanged : board.getAndResetChangedCells())
				{
					x = cellChanged.getX();
					y = cellChanged.getY();
					gc.clearRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
					for(Item element : map[y][x].getList())
					{
						imageItem = new Image("file:ressources"+File.separatorChar+element.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
						gc.drawImage(imageItem, x*CELL_SIZE, y*CELL_SIZE);
					}
				}
				board.searchPlayers();
				if(board.isWin()) 
				{
					if(board.getLevelNumber()>=listOfLevels.length-1)
					{
						primaryStage.close();
						return;
					}
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
					board = LevelManager.getActivesBoards()[0]; //Charge le prochain niveau
					drawBoard(canvas, board);
				}
			}});
	}
	
	public static void drawBoard(Canvas canvas, Board board)
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for(Cell[] element1 : board.getBoard())
		{
			for(Cell element2 : element1)
			{
				for(Item element3 : element2.getList())
				{
					imageItem = new Image("file:ressources"+File.separatorChar+element3.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
					gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
				}
			}
		}
	}
	
	public void playButtonClicked() {
		loadLevel(0);
	}
	
	public void exitButtonClicked() {
		primaryStage.close();
	}
	
	public void loadSaveButtonClicked() {
		loadLevel(-1);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
}

	
