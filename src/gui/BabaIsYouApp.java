package gui;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

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
import javafx.stage.StageStyle;

/**
 * Classe principale qui va g�rer le jeu en mode interface graphique.
 * 
 */
public class BabaIsYouApp extends Application {
	
	//Fen�tre principale
	private static Stage primaryStage;
	private static Board board;
	private static Scene menu, game;
	private static Canvas canvas = new Canvas(960, 960);
	private static Group root;
	private static double CELL_SIZE = 48;
	private static Class<?> thisClass;
	private static Item selectedItem;
	
	//Fen�tre secondaire (d'astuces)
	private static Stage secondaryStage = new Stage();
	private static Scene advice;
	private static Canvas canvasAdvice;
	private static Group rootAdvice;
	private static Text close;
	
	// START MENU
	@FXML
	private ImageView cadrePlay;
	@FXML
	private ImageView cadreLoadSave;
	@FXML
	private ImageView cadreEditor;
	@FXML
	private ImageView cadreLevels;
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
			thisClass = getClass(); // On en a besoin d'en d'autres m�thodes et on ne peut pas y acc�der par un getClass()
			//Chargement du Menu principal
			loadMenu();
			primaryStage.setTitle("BABA IS YOU");
			primaryStage.getIcons().add(new Image("file:ressources"+File.separatorChar+"baba.png"));
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			secondaryStage.initStyle(StageStyle.UNDECORATED);
			
			//Changement de la police d'�criture en 8_Bit_Madness qui est une police d'�criture gratuite d'utilisation sauf pour une utilisation commerciale
			fontMadness = Font.loadFont("file:ressources"+File.separatorChar+"fonts"+File.separatorChar+"8-Bit Madness.ttf", 21);
			if(fontMadness == null)
				throw new IOException("Custom font was not find");

			//Ouverture de l'application
			primaryStage.show();
	}
	
	/**
	 * M�thode qui va charger le menu principal 
	 */
	private static void loadMenu() {
		Parent root = null; 
//		Media media = new Media(new File("ressources/musicMenu.mp3").toURI().toString());
//		MediaPlayer playerMusic = new MediaPlayer(media);
//		playerMusic.play();
//		playerMusic.setCycleCount(MediaPlayer.INDEFINITE); //Si tu veux la jouer en boucle
		
		
		//Chargement du fichier @FXML qui repr�sente le menu principal
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
		
		/*
		 * Gestionnaire d'�v�nements du clavier, si la touche ESCAPE est press�e alors on ferme l'application
		 * Sinon si la touche ENTER est press�e alors on charge le premier niveau
		 */
		menu.setOnKeyPressed((KeyEvent event) -> {
				if(event.getCode().toString().equals("ESCAPE")) {
					primaryStage.close(); //Fermeture de l'application
					secondaryStage.close();
				}
				else if(event.getCode().toString().equals("ENTER")) {
					loadLevel(LevelManager.getListOfLevels()[0], true); //Chargement du premier niveau
				}
		});
	}
	
	private static void loadLevelsMenu() {
		//Initialisation
		root = new Group();
		canvas = new Canvas(960,960);
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		//Ce StackPane est repr�sente le fond fonc� qu'il y a sur les diff�rents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur � chaque fois qu'on redessine une cellule
		root.getChildren().add(canvas);
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		holder.setStyle("-fx-background-color: #1c1f22");
		root.getChildren().add(holder);
		GraphicsContext gc = canvas.getGraphicsContext2D();
//		gc.drawImage(new Image("file:ressources"+File.separator+"BackgroundMenu.png", 960, 960, true, true), 0, 0);
		gc.drawImage(new Image("file:ressources"+File.separator+"levelsMenu.png"), 0, 0);
		int x = -65;
		for(int index = 0; index<=LevelManager.getListOfLevels().length; index++) {
			ImageView levelImage = new ImageView(new Image("file:ressources"+File.separator+"level"+index+".png"));
			levelImage.setPickOnBounds(true);
			levelImage.setOpacity(0.5);
			levelImage.setLayoutX(x);
			levelImage.setLayoutY(200);
			levelImage.setScaleX(0.75);
			levelImage.setScaleY(0.75);
			x+=100;
			final int i = index;
			levelImage.setOnMouseClicked((MouseEvent e) -> {
				loadLevel(LevelManager.getListOfLevels()[i-1], true);
			});
			levelImage.setOnMouseEntered((MouseEvent e) -> {
				levelImage.setOpacity(1);
			});
			levelImage.setOnMouseExited((MouseEvent e) -> {
				levelImage.setOpacity(0.5);
			});
			
			
			root.getChildren().add(levelImage);
		}
		ImageView loadImage= new ImageView(new Image("file:ressources"+File.separator+"load.png"));
		loadImage.setPickOnBounds(true);
		loadImage.setOpacity(0.5);
		loadImage.setScaleX(0.75);
		loadImage.setScaleY(0.75);
		loadImage.setLayoutX(750);
		loadImage.setLayoutY(690);
		loadImage.setOnMouseClicked((MouseEvent e) -> {
			File fileChoose = FileChooserLvl();
			loadLevel(new String[] {fileChoose.getPath().split(Pattern.quote("."))[0]}, true);
		});
		loadImage.setOnMouseEntered((MouseEvent e) -> {
			loadImage.setOpacity(1);
		});
		loadImage.setOnMouseExited((MouseEvent e) -> {
			loadImage.setOpacity(0.5);
		});
		close = new Text("X");
		close.setFont(fontMadness);
		close.setFill(Color.WHITE);
		close.setOpacity(0.5 );
		close.setScaleX(3);
		close.setScaleY(3);
		close.setX(910);
		close.setY(45);
		close.setOnMouseClicked((MouseEvent event) -> {
			loadMenu();
		});
		close.setOnMouseEntered((MouseEvent event) -> {
			close.setOpacity(1);
		});
		close.setOnMouseExited((MouseEvent event) -> {
			close.setOpacity(0.5);
		});
		
		
		root.getChildren().addAll(loadImage, close);
		
		game.setOnKeyPressed((KeyEvent event) -> {
			if(event.getCode().toString() == "ESCAPE")
				loadMenu();
		});
	}

	/**
	 * M�thode qui va charger l'Editeur de niveau
	 * @param levelName Nom du niveau � charger
	 */
	private static void loadEditor(String levelName) {
		/* On charge le niveau comme un niveau normal car l'�diteur est juste une sorte de gros niveau en 22*22
		 * (voir le fichier "levels/cleanEditor.txt pour mieux comprendre */
		loadLevel(new String[] {levelName}, false);
		
		//On initialise l'enti�ret� des bouttons et zone de textes qui permettent de charger, tester, sauvegarder un niveau
		
		//TextField pour entrer le nom du niveau � sauvegarder
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
		
		//Boutton qui va permettre � l'utilisateur de sauvegarder le niveau qu'il a cr�e
		saveButton = new Button("SAVE");
		saveButton.setFont(fontMadness);
		saveButton.setOnMouseClicked(saveButtonClicked());
		saveButton.setLayoutX(330);
		saveButton.setLayoutY(45);
		
		//Boutton qui va reset l'�diteur de niveau
		resetButton = new Button("RESET");
		resetButton.setFont(fontMadness);
		resetButton.setOnMouseClicked(resetButtonClicked());
		resetButton.setLayoutX(410);
		resetButton.setLayoutY(45);
		
//		Button bouton= new Button("demo");
//		bouton.setOnAction((????? event) -> {
//			
//		});
//		
		//Boutton qui va permettre � l'utilisateur de tester le niveau qu'il vient de cr�er
		testButton = new Button("TEST");
		testButton.setFont(fontMadness);
		testButton.setOnMouseClicked(testButtonClicked());
		testButton.setLayoutX(500);
		testButton.setLayoutY(45);
		
		//Text qui va indiquer si le niveau � bien �t� sauvegarder
		textZone = new Text("");
		textZone.setFont(fontMadness);
		textZone.setFill(Color.WHITE);
		textZone.setScaleX(1.5);
		textZone.setScaleY(1.5);
		textZone.setLayoutX(80);
		textZone.setLayoutY(110);
		
		//Text "Selected Item : " (en haut � droite) 
		textSelectedItem = new Text("SELECTED  ITEM  : ");
		textSelectedItem.setFont(fontMadness);
		textSelectedItem.setFill(Color.WHITE);
		textSelectedItem.setScaleX(1.5);
		textSelectedItem.setScaleY(1.5);
		textSelectedItem.setLayoutX(685);
		textSelectedItem.setLayoutY(65);
		
		//Image qui permet de rajouter une pronfondeur de niveau suppl�mentaire
		String[] imagesToLoad = {"file:ressources"+File.separator+"+.png", "file:ressources"+File.separator+"-.png",
				"file:ressources"+File.separator+"fl�cheGauche.png","file:ressources"+File.separator+"fl�cheDroite.png"};
		int x1 = 100;
		for(int index = 0; index<4; index++) {
			ImageView image = new ImageView(new Image(imagesToLoad[index]));
			image.setX(x1);
			image.setY(90);
			image.setScaleX(0.3);
			image.setScaleY(0.3);
			image.setOpacity(0.5);
			image.setPickOnBounds(true);
			final int i = index;
			image.setOnMousePressed((MouseEvent event) -> {
				switch(i) {
				case 0:
					System.out.println("+");
					break;
				case 1:
					System.out.println("-");
					break;
				case 2:
					System.out.println("<--");
					break;
				case 3:
					System.out.println("-->");
					break;
				default: break;
				}
				
			});
			image.setOnMouseEntered((MouseEvent event) -> {
				image.setOpacity(1);
			});
			image.setOnMouseExited((MouseEvent event) -> {
				image.setOpacity(0.5);
			});
			
			x1+=35;
			root.getChildren().add(image);
		}
		
		
		//On ajout le tout � la sc�ne
		root.getChildren().addAll(loadButton, saveText, saveButton, testButton, resetButton, textZone, textSelectedItem);
		
		/*
		 * Gestionnaire d'�v�nements pour la souris qui va permettre de s�lectionner un Item et de pouvoir le placer dans le niveau
		 */
		game.setOnMouseClicked((MouseEvent event) -> {
				//On r�cup�re la position en X et en Y de la souris
				double mouseX = event.getX();
				double mouseY = event.getY();
				/*
				 * Si la souris se situe dans la zone des items s�lectionnables (voir en jeu pour mieux comprendre)
				 * alors on cherche l'item dans le board. S'il existe on le place dans la variable Item selectedItem
				 */
				if(mouseX >= 20*CELL_SIZE && mouseX<= 23*CELL_SIZE && mouseY >= 4*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					selectedItem = board.getBoard()[(int)(mouseY/CELL_SIZE)][(int)(mouseX/CELL_SIZE)].getLastItem();
					if(selectedItem != null) {
						board.setCell(22, 1, new Cell(1, 22, selectedItem));
						drawCell(board.getCell(22, 1));
					}
					return;
				}
				/*
				 * Si la souris se trouve dans la zone o� l'utilisateur peut composer son propre niveau alors l'utilisateur peut int�ragir avec lui
				 */
				else if(mouseX >= 1*CELL_SIZE && mouseX < 19*CELL_SIZE && mouseY >= 5*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					if(selectedItem == null) { // On v�rifie tout de m�me s'il a bien s�lectionner un Item � placer
						return;
					}
					//Coordonn�es de la cellule o� placer l'Item
					int x = (int)(mouseX/CELL_SIZE);
					int y = (int)(mouseY/CELL_SIZE);
					
					//Si l'utilisateur � s�lectionner l'outil gomme alors on supprime ce qui se trouve dans la cellule
					if(selectedItem instanceof Eraser) {
						board.setCell(x, y, new Cell(y, x));
					}
					// S'il y a d�j� un Item dans la cellule on y ajoute l'Item s�lectionn�
					else if(board.getCell(x, y).getList().size() > 0) {
						board.getCell(x, y).add(selectedItem);
					}
					else {
						board.setCell(x, y, new Cell(y, x, selectedItem)); //On cr�e une nouvelle cellule qui ne contiendra que l'Item s�lectionn�
					}
					textZone.setText(""); //On met la textZone � "" pour ne pas afficher que le niveau a �t� sauvegarder alors qu'il ne l'est pas
					drawCell(board.getCell(x, y)); //Mise � jour de la cellule qui a �t� modifi�e
				}
		});

		//Gestionnaire d'�v�nements du clavier : Comme on se situe dans l'�diteur de niveau si l'utilisateur appuie sur "ESCAPE" on affiche le menu principal
		game.setOnKeyPressed((KeyEvent event) -> {
				String code = event.getCode().toString();
				if(code.equals("ESCAPE")) {
					//Sauvegarde automatique du niveau que l'utilisateur est en train de composer pour ne pas qu'il le perde s'il appuie par erreur sur "ESCAPE"
					LevelManager.saveLvlEditor("testEditor"); //On sauvegarde le fichier dans le bon format
					loadMenu();
				}
		});
	}	
	
	/**
	 * M�thode qui va charger un niveau et l'afficher
	 * @param names les noms des fichiers des diff�rentes pronfondeur d'un niveau
	 * @param activateInputs Est ce que les "Inputs" du clavier peuvent �tre utilis�
	 */
	private static void loadLevel(String[] names, boolean activateInputs)
	{
		if(names[0].equals("save")) //Chargement du niveau sauvegard�
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(names); //Chargement normal du niveau donn� en param�tre
		board = LevelManager.getActivesBoards()[0]; //R�cup�ration de la premi�re pronfondeur du niveau
		root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		root.getChildren().add(canvas);
		CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows()); //De combien doit �tre la taille d'une cellule
		
		//Ce StackPane est repr�sente le fond fonc� qu'il y a sur les diff�rents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur � chaque fois qu'on redessine une cellule
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		root.getChildren().add(holder);
		holder.setStyle("-fx-background-color: #1c1f22");
		
		drawBoard(); //Affichage du niveau dans le canvas
		if(board.getLevelNumber() == 0)
			loadAdviceStage("advice1");
		else if (board.getLevelNumber() == 4)
			loadAdviceStage("advice2");
			
		if(activateInputs) //On autorise ou non les "Inputs" clavier
			activateKeyInputs();
	}
	
	public static void loadAdviceStage(String name) {
		rootAdvice = new Group();
		advice = new Scene(rootAdvice, 600, 600);
		secondaryStage.setScene(advice);
		canvasAdvice = new Canvas(600, 600);
		rootAdvice.getChildren().add(canvasAdvice);
		
		GraphicsContext gc = canvasAdvice.getGraphicsContext2D();
		gc.drawImage(new Image("file:ressources"+File.separator+name+".png"), 0, 0);
		
		close = new Text("X");
		close.setFont(fontMadness);
		close.setFill(Color.WHITE);
		close.setOpacity(0.5);
		close.setScaleX(2);
		close.setScaleY(2);
		close.setLayoutX(560);
		close.setLayoutY(40);
		close.setOnMouseClicked((MouseEvent e) -> {
			secondaryStage.close();
		});
		close.setOnMouseEntered((MouseEvent e) -> {
			close.setOpacity(1);
		});
		close.setOnMouseExited((MouseEvent e) -> {
			close.setOpacity(0.5);
		});
		advice.setOnKeyPressed((KeyEvent e) -> {
			if(e.getCode().toString().equals("ESCAPE"))
				secondaryStage.close();
				
			
		});
		rootAdvice.getChildren().addAll(close);
		
		secondaryStage.setX(primaryStage.getX()+180);
		secondaryStage.setY(primaryStage.getY()+180);
		secondaryStage.show();
	}
	
	/**
	 * M�thode qui va dessiner le niveau en cours dans le canvas
	 */
	public static void drawBoard()
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
					imageItem = new Image("file:ressources"+File.separatorChar+element3.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
					//On dessine l'Item en fonction de la taille d'une cellule
					gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
				}
			}
		}
	}
	
	/**
	 * M�thode qui va redessiner une cellule
	 * @param oneCell la cellule � redessiner
	 */
	private static void drawCell(Cell oneCell) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		//R�cup�ration des coordonn�es de la cellule
		int x = oneCell.getX();
		int y = oneCell.getY();
		//Vidage de la cellule dans le canvas
		gc.clearRect(y*CELL_SIZE, x*CELL_SIZE, CELL_SIZE, CELL_SIZE);
		//It�ration sur tous les Items de la cellule
		for(Item element : oneCell.getList())
		{
			//R�cup�ration de l'image � afficher en fonction de son nom
			imageItem = new Image("file:ressources"+File.separatorChar+element.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
			//On dessine l'Item en fonction de la taille d'une cellule
			gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
		}
	}
	
	/**
	 * M�thode qui va g�rer les inputs clavier de l'utilisateur
	 */
	public static void activateKeyInputs() {
		String[][] listOfLevels = LevelManager.getListOfLevels();
		
		//Gestionnaire d'�v�nements clavier
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int direction; //Variable qui va �tre utilis� dans la fonction move()
			@Override
			public void handle(KeyEvent event) 
			{
				String code = event.getCode().toString(); //R�cup�ration de la touche press�e
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
					if(board.getLevelNumber() == -1) {
						LevelManager.readLevel(new String[] {"levels"+File.separator+"editor"+File.separator+"testEditor_0"});
					}
					else {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()]);
					}
					board = LevelManager.getActivesBoards()[0];
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "A": //Charge la prondeur pr�c�dente
					if(board.getDepthOfLevel()-1>=0)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()-1];
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "E": //Charge la pronfondeur suivante
					if(board.getDepthOfLevel()<LevelManager.getActivesBoards().length-1)
					{
						board = LevelManager.getActivesBoards()[board.getDepthOfLevel()+1];
						board.searchPlayers();
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "SPACE": //Charge la pronfondeur suivante et si elle n'existe pas alors on charge la premi�re profondeur
					board = LevelManager.getActivesBoards()[(board.getDepthOfLevel()+1)%LevelManager.getActivesBoards().length];
					board.searchPlayers();
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
					return;
				case "P": //Charge le niveau pr�c�dent
					if(board.getLevelNumber()-1>=0) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()-1]);
						board = LevelManager.getActivesBoards()[0];
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "N": //Charge le niveau suivant
					if(board.getLevelNumber()<listOfLevels.length-1 && board.getLevelNumber() != -1) {
						LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
						board = LevelManager.getActivesBoards()[0];
						if(board.getLevelNumber() == 4)
							loadAdviceStage("advice2");
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					return;
				case "F1": //Affiche les astuces
					if(board.getLevelNumber() == 0)
						loadAdviceStage("advice1");
					else if(board.getLevelNumber() == 4)
						loadAdviceStage("advice2");
					return;
				case "ESCAPE": //Retourne au menu principal
					loadMenu();
					return;
				default : return; // Si une autre touche est press�e une ne fait rien
				}
				//It�ration sur la liste des joueurs pour les d�placer
				for(Tuple player: board.getPlayers())
				{
					//D�placement d'un joueur en fonction de la direction
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}
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
						loadMenu();
						return;
					}
					//Chargement du prochain niveau
					LevelManager.readLevel(listOfLevels[board.getLevelNumber()+1]);
					board = LevelManager.getActivesBoards()[0]; //Charge le prochain niveau
					if(board.getLevelNumber() == 4)
						loadAdviceStage("advice2");
					CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
					drawBoard();
				}
			}});
	}
		
	private static File FileChooserLvl() {
		//On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le niveau qu'il veut charger dans l'�diteur de niveau
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose level to load :");
		// Le code ci-dessous provient de : "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
		// Il permet de d�finir les extensions de fichier autoris�es. Ici, on n'autorise que les fichiers textes (".txt")
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".TXT", "*.txt");
		fc.getExtensionFilters().add(fileExtensions);
		fc.setInitialDirectory(new File("levels"+File.separator+"editor")); //On d�finit l'endroit o� le FileChooser va s'ouvrir
		File fileChoose = fc.showOpenDialog(primaryStage);
		if(fileChoose != null) {
			//Code pour split le path trouv� sur : "https://stackoverflow.com/questions/1099859/how-to-split-a-path-platform-independent"
			String[] nameFileSplit = fileChoose.toString().split(Pattern.quote(File.separator));
			if(nameFileSplit[nameFileSplit.length-1].equals("cleanEditor.txt")) {
				return null;
			}
		}
		return fileChoose;
	}
	
	// START MENU 
	@FXML
	public void playButtonClicked() {
		loadLevel(LevelManager.getListOfLevels()[0], true);
	}
	
	@FXML
	public void exitButtonClicked() {
		primaryStage.close();
		secondaryStage.close();
	}
	
	@FXML
	public void loadSaveButtonClicked() {
		loadLevel(new String[] {"save"}, true); //Chargement du niveau sauvegard�
	}
	
	@FXML
	public void editorButtonClicked() {
		File file = new File("levels"+File.separator+"editor"+File.separator+"testEditor_0.txt");
		if(file.exists()) { //Si le fichier existe c'est que l'utilisateur � d�j� commenc� � composer un niveau
			String name = LevelManager.loadUserLvl(file);
			loadEditor(name); //On retire le ".txt"
		}
		else //On charge l'�diteur de base
			loadEditor("levels"+File.separator+"cleanEditor");
	}
	
	@FXML
	public void levelsButtonClicked() {
		loadLevelsMenu();
	}
	
	//Gestion des animations dans le menu principal
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
	public void cadreLevelsMouseEntered() {
		cadreLevels.setOpacity(1);
	}
	
	@FXML
	public void cadreLevelsMouseExited() {
		cadreLevels.setOpacity(0);
	}
	
	@FXML
	public void cadreExitMouseEntered() {
		cadreExit.setOpacity(1);
	}
	
	@FXML
	public void cadreExitMouseExited() {
		cadreExit.setOpacity(0);
	}
	
	
	// EDITOR
	/**
	 * M�thode qui va changer la zone de texte
	 * @return ?
	 */
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
	
	/**
	 * M�thode qui va s'occuper de la gestion du boutton "Reset"
	 * @return
	 */
	private static EventHandler<MouseEvent> resetButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				//On supprime les niveaux temporaires pour que si l'utilisateur quitte et reviens dans l'�diteur de niveau l'�diteur soit vide
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
	
	/**
	 * M�thode qui sa s'occuper de la gestion du boutton "LOAD"
	 * @return
	 */
	private static EventHandler<MouseEvent> loadButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				 loadEditor(LevelManager.loadUserLvl(FileChooserLvl())); //Chargement du niveau choisi par l'utilisateur dans l'�diteur de niveau
			}
		};
	}	
	/**
	 * M�thode qui va s'occuper de la gestion du boutton "TEST"
	 * @return
	 */
	private static EventHandler<MouseEvent> testButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				LevelManager.saveLvlEditor("testEditor"); //On sauvegarde le fichier dans le bon format
				String[] testEditor = {"levels"+File.separator+"editor"+File.separatorChar+"testEditor_0"};
				loadLevel(testEditor, true); //On charge le fichier qu'on vient de sauvegarder
			}
		};
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}

