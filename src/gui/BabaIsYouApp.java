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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
	
	// START MENU
	@FXML
	private ImageView cadrePlay;
	@FXML
	private ImageView cadreLoadSave;
	@FXML
	private ImageView cadreEditor;
	@FXML
	private ImageView cadreExit;
	
	// EDITOR
	private static Font fontMadness;
	private static TextField saveText;
	private static Button saveButton;
	private static Button testButton;
	private static Button resetButton;
	private static Button loadButton;
	private static Text textZone;
	private static Text textSelectedItem;
	
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
			
			//Changement de la police d'écriture en 8_Bit_Madness qui est une police d'écriture gratuite d'utilisation sauf pour une utilisation commerciale
			fontMadness = Font.loadFont("file:ressources"+File.separatorChar+"fonts"+File.separatorChar+"8-Bit Madness.ttf", 21);
			if(fontMadness == null)
				throw new IOException("Custom font was not find");
			
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

	private static void loadEditor(String levelName) {
		//TextField pour entrer le nom du niveau à sauvegarder
		String[] levelsNames = {levelName};
		loadLevel(levelsNames, false);
		
		saveText = new TextField();
		saveText.setFont(fontMadness);
		saveText.setPromptText("name");
		saveText.setLayoutX(53);
		saveText.setLayoutY(45);
		
		//Boutton qui permet de charger un niveau
		loadButton = new Button("LOAD");
		loadButton.setFont(fontMadness);
		loadButton.setOnMouseClicked(loadButtonClicked());
		loadButton.setLayoutX(250);
		loadButton.setLayoutY(45);
		
		//Boutton qui va permettre à l'utilisateur de sauvegarder le niveau qu'il a crée
		saveButton = new Button("SAVE");
		saveButton.setFont(fontMadness);
		saveButton.setOnMouseClicked(saveButtonClicked());
		saveButton.setLayoutX(330);
		saveButton.setLayoutY(45);
		
		//Boutton qui va reset l'éditeur de niveau
		resetButton = new Button("RESET");
		resetButton.setFont(fontMadness);
		resetButton.setOnMouseClicked(resetButtonClicked());
		resetButton.setLayoutX(410);
		resetButton.setLayoutY(45);
		
		//Boutton qui va permettre à l'utilisateur de tester le niveau qu'il vient de créer
		testButton = new Button("TEST");
		testButton.setFont(fontMadness);
		testButton.setOnMouseClicked(testButtonClicked());
		testButton.setLayoutX(500);
		testButton.setLayoutY(45);
		
		//Text qui va indiquer si le niveau à bien été sauvegarder
		textZone = new Text("");
		textZone.setFont(fontMadness);
		textZone.setFill(Color.WHITE);
		textZone.setScaleX(1.5);
		textZone.setScaleY(1.5);
		textZone.setLayoutX(80);
		textZone.setLayoutY(110);
		
		//Text "Selected Item"
		textSelectedItem = new Text("SELECTED  ITEM  : ");
		textSelectedItem.setFont(fontMadness);
		textSelectedItem.setFill(Color.WHITE);
		textSelectedItem.setScaleX(1.5);
		textSelectedItem.setScaleY(1.5);
		textSelectedItem.setLayoutX(685);
		textSelectedItem.setLayoutY(65);
		
		
		//On ajout le tout à la scène
		root.getChildren().addAll(loadButton, saveText, saveButton, testButton, resetButton, textZone, textSelectedItem);
		
		game.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double mouseX = event.getX();
				double mouseY = event.getY();
//				System.out.println(board.getBoard()[(int)(mouseY/CELL_SIZE)][(int)(mouseX/CELL_SIZE)].getList());
				if(mouseX >= 20*CELL_SIZE && mouseX<= 23*CELL_SIZE && mouseY >= 4*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					selectedItem = board.getBoard()[(int)(mouseY/CELL_SIZE)][(int)(mouseX/CELL_SIZE)].getLastItem();
					if(selectedItem != null) {
						board.setCell(22, 1, new Cell(1, 22, selectedItem));
						drawCell(board.getCell(22, 1));
					}
					return;
				}
				else if(mouseX >= 1*CELL_SIZE && mouseX < 19*CELL_SIZE && mouseY >= 5*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					if(selectedItem == null) {
						return;
					}
					int x = (int)(mouseX/CELL_SIZE);
					int y = (int)(mouseY/CELL_SIZE);
					if(selectedItem instanceof Eraser) {
						board.setCell(x, y, new Cell(y, x));
					}
					else if(board.getCell(x, y).getList().size() > 0) {
						board.getCell(x, y).add(selectedItem);
					}
					else {
						board.setCell(x, y, new Cell(y, x, selectedItem));
					}
					textZone.setText(""); //On met la textZone à "" pour ne pas afficher que le niveau a été sauvegarder alors qu'il ne l'est pas
//					drawBoard();
					drawCell(board.getCell(x, y));
				}
			}
			
		});
		
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				String code = event.getCode().toString();
				if(code.equals("ESCAPE")) {
					loadMenu();
				}
			}
			
		});
	}	
	
	private static void loadLevel(String[] names, boolean activateInputs)
	{
		if(names[0].equals("save"))
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(names);
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
		if(activateInputs)
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
				case "X": LevelManager.saveLvl("levels"+File.separator+"saves"+File.separator+"save"); 
					return; //Sauvegarde le niveau en cours
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
		
	// START MENU 
	}
	@FXML
	public void playButtonClicked() {
		loadLevel(LevelManager.getListOfLevels()[0], true);
	}
	
	@FXML
	public void exitButtonClicked() {
		primaryStage.close();
	}
	
	@FXML
	public void loadSaveButtonClicked() {
		String[] save = {"save"};
		loadLevel(save, true);
	}
	
	@FXML
	public void editorButtonClicked() {
		File file = new File("levels"+File.separator+"editor"+File.separator+"testEditor_0.txt");
		if(file.exists()) {
			String name = LevelManager.loadUserLvl(file);
			loadEditor(name); //On retire le ".txt"
		}
		else
			loadEditor("levels"+File.separator+"cleanEditor");
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
	
	// EDITOR
	private static EventHandler<MouseEvent> saveButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				if (saveText.getText().equals("testEditor")) {
					textZone.setText("ENTER AN OTHER NAME.");
				}
				else if(saveText.getText().length()>0) {
					LevelManager.saveLvlEditor(saveText.getText());
					textZone.setText("LEVEL SAVED !");
				}
				else {
					textZone.setText("ENTER A NAME !");
				}
			}
			
		};
	}
	
	private static EventHandler<MouseEvent> resetButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				//On supprime le niveau temporaire pour que si l'utilisateur quitte et reviens dans l'éditeur de niveau l'éditeur soit vide
				File[] files = {new File("levels"+File.separator+"editor"+File.separator+"cleanEditor_0.txt"), 
						new File("levels"+File.separator+"editor"+File.separator+"testEditor_0.txt")};
				for(File file : files) {
					if(file.exists())
						file.delete();
				}
				loadEditor("levels"+File.separator+"cleanEditor");
			}
			
		};
	}
	
	private static EventHandler<MouseEvent> loadButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				//On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le niveau qu'il veut charger dans l'éditeur de niveau
				FileChooser fc = new FileChooser();
				fc.setTitle("Choose level to load :");
				// Le code ci-dessous provient de : "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
				// Il permet de définir les extensions de fichier autorisées. Ici, on n'autorise que les fichiers textes (".txt")
				FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".TXT", "*.txt");
				fc.getExtensionFilters().add(fileExtensions);
				fc.setInitialDirectory(new File("levels"+File.separator+"editor")); //On définit l'endroit où le FileChooser va s'ouvrir
				File fileChoose = fc.showOpenDialog(primaryStage);
				if(!(fileChoose == null))
					loadEditor(LevelManager.loadUserLvl(fileChoose));
				
			
			}
		};
	}	
	
	private static EventHandler<MouseEvent> testButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				LevelManager.saveLvlEditor("testEditor");
				String[] testEditor = {"levels"+File.separator+"editor"+File.separatorChar+"testEditor_0"};
				loadLevel(testEditor, true);
				
			}
			
		};
	}
	
}

	
