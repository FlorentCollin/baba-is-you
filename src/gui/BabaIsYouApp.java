package gui;

import java.io.File;
import java.io.IOException;

import game.boardController.Board;
import game.boardController.Cell;
import game.boardController.MoveController;
import game.element.Eraser;
import game.element.Item;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Classe principale qui va gérer le jeu en mode interface graphique.
 * 
 */
public class BabaIsYouApp extends Application {
	
	private static double CELL_SIZE = 48;
	private static Board board;
	private static Scene menu, game;
	private static Canvas canvas;
	private static Group root;
	private static Stage primaryStage;
	private static Class<?> thisClass;
	private static Item selectedItem;
	
	@FXML
	private ImageView cadrePlay;
	@FXML
	private ImageView cadreLoadSave;
	@FXML
	private ImageView cadreEditor;
	@FXML
	private ImageView cadreExit;
	
	@Override
	public void start(Stage Stage) throws Exception {
			primaryStage = Stage;
			thisClass = getClass();
			loadMenu();
			primaryStage.setTitle("BABA IS YOU");
			primaryStage.getIcons().add(new Image("file:ressources"+File.separatorChar+"baba.png"));
//			primaryStage.setMaximized(true);
//			primaryStage.setFullScreen(true);
//			scene.setCursor(Cursor.NONE);
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
	}
	
	private static void loadMenu() {
		Parent root = null;
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
	}
	
	private static void loadEditor() {
		loadLevel(-2);
		TextField saveText = new TextField();
		saveText.setPromptText("name");
		saveText.setLayoutX(53);
		saveText.setLayoutY(45);
		
		Button saveButton = new Button("SAVE");
		saveButton.setLayoutX(250);
		saveButton.setLayoutY(45);
		root.getChildren().addAll(saveText, saveButton);
		
		game.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double mouseX = event.getX();
				double mouseY = event.getY();
				if(mouseX >= 21*CELL_SIZE && mouseX<= 23*CELL_SIZE && mouseY >= 3*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					selectedItem = board.getBoard()[(int)(mouseY/CELL_SIZE)][(int)(mouseX/CELL_SIZE)].getLastItem();
					if(selectedItem != null) {
						board.setCell(1, 22, new Cell(1, 22, selectedItem));
						drawCell(board.getCell(1, 22));
					}
				}
				if(mouseX >= 1*CELL_SIZE && mouseX <= 20*CELL_SIZE && mouseY >= 4*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					if(selectedItem == null) {
						return;
					}
					else if(selectedItem instanceof Eraser) {
						board.setCell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE), new Cell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE)));
						drawCell(board.getCell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE)));
					}
					else {
						board.setCell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE), new Cell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE), selectedItem));
						drawCell(board.getCell((int)(mouseY/CELL_SIZE), (int)(mouseX/CELL_SIZE)));
					}
				}
			}
			
		});
	
	}
	
	
	private static void loadLevel(int index)
	{
		String[][] listOfLevels = LevelManager.getListOfLevels();
		if(index == -1)
			LevelManager.loadSaveLvl();
		else if(index == -2)
			LevelManager.loadEditor();
		else
			LevelManager.readLevel(listOfLevels[index]);
		board = LevelManager.getActivesBoards()[0];
		root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		canvas = new Canvas(960, 960);
		root.getChildren().add(canvas);
		CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
		
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		holder.setStyle("-fx-background-color: #1c1f22");
		
		drawBoard();
		if(index != -2)
			activateKeyInputs();
	}
	
	public static void drawBoard()
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
	
	private static void drawCell(Cell oneCell) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		int x = oneCell.getX();
		int y = oneCell.getY();
		gc.clearRect(y*CELL_SIZE, x*CELL_SIZE, CELL_SIZE, CELL_SIZE);
		for(Item element : oneCell.getList())
		{
			imageItem = new Image("file:ressources"+File.separatorChar+element.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
			gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
		}
	}
	
	public static void activateKeyInputs() {
		String[][] listOfLevels = LevelManager.getListOfLevels();
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int direction;
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
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "R": //Reinitialise le niveau en cours
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()]);
					board = LevelManager.getActivesBoards()[0];
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "A":
					if(board.getDepthOfLevel()-1>=0)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()-1];
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "E":
					if(board.getDepthOfLevel()<LevelManager.getActivesBoards().length-1)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()+1];
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "SPACE":
					board = LevelManager.getActivesBoards()[(board.getDepthOfLevel()+1)%LevelManager.getActivesBoards().length];
					board.searchPlayers();
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "P": //Charge le niveau précédent
					if(board.getLevelNumber()-1>=0) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()-1]);
						board = LevelManager.getActivesBoards()[0];
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "N": //Charge le niveau suivant
					if(board.getLevelNumber()<listOfLevels.length-1) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
						board = LevelManager.getActivesBoards()[0];
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "ESCAPE": //Retourne au menu principal
					loadMenu();
					return;
				default : return; // Si une autre touche est pressée une ne fait rien
				}
				//Save previous move
				
				for(Tuple player: board.getPlayers())
				{
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}
				for(Tuple cellChanged : board.getAndResetChangedCells())
				{
					drawCell(board.getBoard()[cellChanged.getY()][cellChanged.getX()]);
				}
				board.searchPlayers();
				if(board.isWin()) 
				{
					if(board.getLevelNumber()>=listOfLevels.length-1)
					{
						loadMenu();
						return;
					}
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
					board = LevelManager.getActivesBoards()[0]; //Charge le prochain niveau
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
				}
			}});
	}
	@FXML
	public void playButtonClicked() {
		loadLevel(0);
	}
	
	@FXML
	public void exitButtonClicked() {
		primaryStage.close();
	}
	
	@FXML
	public void loadSaveButtonClicked() {
		loadLevel(-1);
	}
	
	@FXML
	public void editorButtonClicked() {
		loadEditor();
	}
	
	@FXML
	public void cadrePlayMouseEntered() {
		cadrePlay.setOpacity(1);
	}
	
	@FXML
	public void cadrePlayMouseExited() {
		cadrePlay.setOpacity(0);
	}
	
	@FXML
	public void cadreLoadSaveMouseEntered() {
		cadreLoadSave.setOpacity(1);
	}
	
	@FXML
	public void cadreLoadSaveMouseExited() {
		cadreLoadSave.setOpacity(0);
	}
	
	@FXML
	public void cadreEditorMouseEntered() {
		cadreEditor.setOpacity(1);
	}
	
	@FXML
	public void cadreEditorMouseExited() {
		cadreEditor.setOpacity(0);
	}
	
	@FXML
	public void cadreExitMouseEntered() {
		cadreExit.setOpacity(1);
	}
	
	@FXML
	public void cadreExitMouseExited() {
		cadreExit.setOpacity(0);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
}

	
