package gui;

import game.element.Board;
import game.element.Cell;
import game.element.Item;
import game.element.Rules;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
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
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		String[] listOfLevels = LevelManager.getListOfLevels();
		board = LevelManager.readLevel(listOfLevels[0]);
//		board = LevelManager.readLevel("testLvl");
		Rules.scanRules(board.getBoard());
		board.searchPlayers();
				
		Group root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("BABA IS YOU");
//		primaryStage.setMaximized(true);
//		primaryStage.setFullScreen(true);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		scene.setCursor(Cursor.NONE);
		Canvas canvas = new Canvas(960, 960);
		root.getChildren().add(canvas);
		
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		
		holder.setStyle("-fx-background-color: #1c1f22");
//		holder.setStyle("-fx-backgroudnd-color: #161616");
		
		drawBoard(canvas, board);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
				case "X": board.saveLvl(); return; //Sauvegarde le niveau en cours
				case "L": //Charge la sauvegarde
					board = LevelManager.readLevel("saveLvl"); 
					Rules.scanRules(board.getBoard());
					board.searchPlayers();
					drawBoard(canvas, board);
					return;
				case "R": //Reinitialise le niveau en cours
					board = LevelManager.readLevel(listOfLevels[board.getLevelNumber()-1]);
					Rules.scanRules(board.getBoard());
					board.searchPlayers();
					drawBoard(canvas, board);
					return;
				case "DIGIT1": //Charge le niveau précédent
					if(board.getLevelNumber()-2>=0) {
						board = LevelManager.readLevel(listOfLevels[board.getLevelNumber()-2]);
						Rules.scanRules(board.getBoard());
						board.searchPlayers();
						drawBoard(canvas, board);
					}
					return;
				case "DIGIT2": //Charge le niveau suivant
					if(board.getLevelNumber()<=listOfLevels.length-1) {
						board = LevelManager.readLevel(listOfLevels[board.getLevelNumber()]);
						Rules.scanRules(board.getBoard());
						board.searchPlayers();
						drawBoard(canvas, board);
					}
					return;
				default : return;
				}
				for(Tuple player: board.getPlayers())
				{
					board.move(player.getX(), player.getY(), player.getZ(), direction);
				}
				map = board.getBoard();
				for(Tuple cellChanged : board.getAndResetChangedCells())
				{
					x = cellChanged.getX();
					y = cellChanged.getY();
					gc.clearRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
					for(Item element : map[y][x].getList())
					{
						imageItem = new Image("file:ressources/"+element.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
						gc.drawImage(imageItem, x*CELL_SIZE, y*CELL_SIZE);
					}
				}
				board.searchPlayers();
				if(board.isWin()) 
				{
					if(board.getLevelNumber()>3)
					{
						primaryStage.close();
						return;
					}
					board = LevelManager.readLevel(listOfLevels[board.getLevelNumber()]); //Charge le prochain niveau
					Rules.scanRules(board.getBoard());
					board.searchPlayers();
					drawBoard(canvas, board);
				}
			}});
		
		
		primaryStage.show();	
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
					imageItem = new Image("file:ressources/"+element3.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
					gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
}

	
