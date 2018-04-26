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
 * Classe principale qui va gérer le jeu en mode interface graphique.
 * 
 */
public class BabaIsYouApp extends Application {
	
	//Fenêtre principale
	private static Stage primaryStage;
	private static Board board;
	private static Scene menu, game;
	private static Canvas canvas = new Canvas(960, 960);
	private static Group root;
	private static double CELL_SIZE = 48;
	private static Class<?> thisClass;
	private static Item selectedItem;
	
	//Fenêtre secondaire (d'astuces)
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
			thisClass = getClass(); // On en a besoin d'en d'autres méthodes et on ne peut pas y accéder par un getClass()
			//Chargement du Menu principal
			loadMenu();
			primaryStage.setTitle("BABA IS YOU");
			primaryStage.getIcons().add(new Image("file:ressources"+File.separatorChar+"baba.png"));
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			secondaryStage.initStyle(StageStyle.UNDECORATED);
			
			//Changement de la police d'écriture en 8_Bit_Madness qui est une police d'écriture gratuite d'utilisation sauf pour une utilisation commerciale
			fontMadness = Font.loadFont("file:ressources"+File.separatorChar+"fonts"+File.separatorChar+"8-Bit Madness.ttf", 21);
			if(fontMadness == null)
				throw new IOException("Custom font was not find");

			//Ouverture de l'application
			primaryStage.show();
	}
	
	/**
	 * Méthode qui va charger le menu principal 
	 */
	private static void loadMenu() {
		Parent root = null; 
//		Media media = new Media(new File("ressources/musicMenu.mp3").toURI().toString());
//		MediaPlayer playerMusic = new MediaPlayer(media);
//		playerMusic.play();
//		playerMusic.setCycleCount(MediaPlayer.INDEFINITE); //Si tu veux la jouer en boucle
		
		
		//Chargement du fichier @FXML qui représente le menu principal
		try {
			root = FXMLLoader.load(thisClass.getResource("startMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		menu = new Scene(root, 960, 960);
		primaryStage.setScene(menu);
		
		/*
		 * Gestionnaire d'évènements du clavier, si la touche ESCAPE est pressée alors on ferme l'application
		 * Sinon si la touche ENTER est pressée alors on charge le premier niveau
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
		//Ce StackPane est représente le fond foncé qu'il y a sur les différents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur à chaque fois qu'on redessine une cellule
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
	 * Méthode qui va charger l'Editeur de niveau
	 * @param levelName Nom du niveau à charger
	 */
	private static void loadEditor(String levelName) {
		/* On charge le niveau comme un niveau normal car l'éditeur est juste une sorte de gros niveau en 22*22
		 * (voir le fichier "levels/cleanEditor.txt pour mieux comprendre */
		loadLevel(new String[] {levelName}, false);
		
		//On initialise l'entièreté des bouttons et zone de textes qui permettent de charger, tester, sauvegarder un niveau
		
		//TextField pour entrer le nom du niveau à sauvegarder
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
		
//		Button bouton= new Button("demo");
//		bouton.setOnAction((????? event) -> {
//			
//		});
//		
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
		
		//Text "Selected Item : " (en haut à droite) 
		textSelectedItem = new Text("SELECTED  ITEM  : ");
		textSelectedItem.setFont(fontMadness);
		textSelectedItem.setFill(Color.WHITE);
		textSelectedItem.setScaleX(1.5);
		textSelectedItem.setScaleY(1.5);
		textSelectedItem.setLayoutX(685);
		textSelectedItem.setLayoutY(65);
		
		//Image qui permet de rajouter une pronfondeur de niveau supplémentaire
		String[] imagesToLoad = {"file:ressources"+File.separator+"+.png", "file:ressources"+File.separator+"-.png",
				"file:ressources"+File.separator+"flècheGauche.png","file:ressources"+File.separator+"flècheDroite.png"};
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
		
		
		//On ajout le tout à la scène
		root.getChildren().addAll(loadButton, saveText, saveButton, testButton, resetButton, textZone, textSelectedItem);
		
		/*
		 * Gestionnaire d'évènements pour la souris qui va permettre de sélectionner un Item et de pouvoir le placer dans le niveau
		 */
		game.setOnMouseClicked((MouseEvent event) -> {
				//On récupère la position en X et en Y de la souris
				double mouseX = event.getX();
				double mouseY = event.getY();
				/*
				 * Si la souris se situe dans la zone des items sélectionnables (voir en jeu pour mieux comprendre)
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
				 * Si la souris se trouve dans la zone où l'utilisateur peut composer son propre niveau alors l'utilisateur peut intéragir avec lui
				 */
				else if(mouseX >= 1*CELL_SIZE && mouseX < 19*CELL_SIZE && mouseY >= 5*CELL_SIZE && mouseY <= 23*CELL_SIZE) {
					if(selectedItem == null) { // On vérifie tout de même s'il a bien sélectionner un Item à placer
						return;
					}
					//Coordonnées de la cellule où placer l'Item
					int x = (int)(mouseX/CELL_SIZE);
					int y = (int)(mouseY/CELL_SIZE);
					
					//Si l'utilisateur à sélectionner l'outil gomme alors on supprime ce qui se trouve dans la cellule
					if(selectedItem instanceof Eraser) {
						board.setCell(x, y, new Cell(y, x));
					}
					// S'il y a déjà un Item dans la cellule on y ajoute l'Item sélectionné
					else if(board.getCell(x, y).getList().size() > 0) {
						board.getCell(x, y).add(selectedItem);
					}
					else {
						board.setCell(x, y, new Cell(y, x, selectedItem)); //On crée une nouvelle cellule qui ne contiendra que l'Item sélectionné
					}
					textZone.setText(""); //On met la textZone à "" pour ne pas afficher que le niveau a été sauvegarder alors qu'il ne l'est pas
					drawCell(board.getCell(x, y)); //Mise à jour de la cellule qui a été modifiée
				}
		});

		//Gestionnaire d'événements du clavier : Comme on se situe dans l'éditeur de niveau si l'utilisateur appuie sur "ESCAPE" on affiche le menu principal
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
	 * Méthode qui va charger un niveau et l'afficher
	 * @param names les noms des fichiers des différentes pronfondeur d'un niveau
	 * @param activateInputs Est ce que les "Inputs" du clavier peuvent être utilisé
	 */
	private static void loadLevel(String[] names, boolean activateInputs)
	{
		if(names[0].equals("save")) //Chargement du niveau sauvegardé
			LevelManager.loadSaveLvl();
		else
			LevelManager.readLevel(names); //Chargement normal du niveau donné en paramètre
		board = LevelManager.getActivesBoards()[0]; //Récupération de la première pronfondeur du niveau
		root = new Group();
		game = new Scene(root, 960, 960);
		primaryStage.setScene(game);
		root.getChildren().add(canvas);
		CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows()); //De combien doit être la taille d'une cellule
		
		//Ce StackPane est représente le fond foncé qu'il y a sur les différents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur à chaque fois qu'on redessine une cellule
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
	 * Méthode qui va dessiner le niveau en cours dans le canvas
	 */
	public static void drawBoard()
	{
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		
		//Vidage de la cellule dans le canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		//Itération sur l'entièreté du Board pour afficher tous les Items
		for(Cell[] element1 : board.getBoard())
		{
			for(Cell element2 : element1)
			{
				//Itération sur tous les Items de la cellule
				for(Item element3 : element2.getList())
				{
					//Récupération de l'image à afficher en fonction de son nom
					imageItem = new Image("file:ressources"+File.separatorChar+element3.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
					//On dessine l'Item en fonction de la taille d'une cellule
					gc.drawImage(imageItem, element2.getY()*CELL_SIZE, element2.getX()*CELL_SIZE);
				}
			}
		}
	}
	
	/**
	 * Méthode qui va redessiner une cellule
	 * @param oneCell la cellule à redessiner
	 */
	private static void drawCell(Cell oneCell) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Image imageItem;
		//Récupération des coordonnées de la cellule
		int x = oneCell.getX();
		int y = oneCell.getY();
		//Vidage de la cellule dans le canvas
		gc.clearRect(y*CELL_SIZE, x*CELL_SIZE, CELL_SIZE, CELL_SIZE);
		//Itération sur tous les Items de la cellule
		for(Item element : oneCell.getList())
		{
			//Récupération de l'image à afficher en fonction de son nom
			imageItem = new Image("file:ressources"+File.separatorChar+element.getGraphicName()+".png", CELL_SIZE, CELL_SIZE, true, true);
			//On dessine l'Item en fonction de la taille d'une cellule
			gc.drawImage(imageItem, y*CELL_SIZE, x*CELL_SIZE);
		}
	}
	
	/**
	 * Méthode qui va gérer les inputs clavier de l'utilisateur
	 */
	public static void activateKeyInputs() {
		String[][] listOfLevels = LevelManager.getListOfLevels();
		
		//Gestionnaire d'événements clavier
		game.setOnKeyPressed(new EventHandler<KeyEvent>() {
			int direction; //Variable qui va être utilisé dans la fonction move()
			@Override
			public void handle(KeyEvent event) 
			{
				String code = event.getCode().toString(); //Récupération de la touche pressée
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
				case "A": //Charge la prondeur précédente
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
				case "SPACE": //Charge la pronfondeur suivante et si elle n'existe pas alors on charge la première profondeur
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
				default : return; // Si une autre touche est pressée une ne fait rien
				}
				//Itération sur la liste des joueurs pour les déplacer
				for(Tuple player: board.getPlayers())
				{
					//Déplacement d'un joueur en fonction de la direction
					MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction);
				}
				//On redessine les cellules qui ont été modifiées
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
		//On ouvre l'explorateur de fichier pour que l'utilisateur puisse choisir le niveau qu'il veut charger dans l'éditeur de niveau
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose level to load :");
		// Le code ci-dessous provient de : "https://stackoverflow.com/questions/13634576/javafx-filechooser-how-to-set-file-filters"
		// Il permet de définir les extensions de fichier autorisées. Ici, on n'autorise que les fichiers textes (".txt")
		FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(".TXT", "*.txt");
		fc.getExtensionFilters().add(fileExtensions);
		fc.setInitialDirectory(new File("levels"+File.separator+"editor")); //On définit l'endroit où le FileChooser va s'ouvrir
		File fileChoose = fc.showOpenDialog(primaryStage);
		if(fileChoose != null) {
			//Code pour split le path trouvé sur : "https://stackoverflow.com/questions/1099859/how-to-split-a-path-platform-independent"
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
		loadLevel(new String[] {"save"}, true); //Chargement du niveau sauvegardé
	}
	
	@FXML
	public void editorButtonClicked() {
		File file = new File("levels"+File.separator+"editor"+File.separator+"testEditor_0.txt");
		if(file.exists()) { //Si le fichier existe c'est que l'utilisateur à déjà commencé à composer un niveau
			String name = LevelManager.loadUserLvl(file);
			loadEditor(name); //On retire le ".txt"
		}
		else //On charge l'éditeur de base
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
	 * Méthode qui va changer la zone de texte
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
	 * Méthode qui va s'occuper de la gestion du boutton "Reset"
	 * @return
	 */
	private static EventHandler<MouseEvent> resetButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				//On supprime les niveaux temporaires pour que si l'utilisateur quitte et reviens dans l'éditeur de niveau l'éditeur soit vide
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
	 * Méthode qui sa s'occuper de la gestion du boutton "LOAD"
	 * @return
	 */
	private static EventHandler<MouseEvent> loadButtonClicked() {
		return new EventHandler<MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				 loadEditor(LevelManager.loadUserLvl(FileChooserLvl())); //Chargement du niveau choisi par l'utilisateur dans l'éditeur de niveau
			}
		};
	}	
	/**
	 * Méthode qui va s'occuper de la gestion du boutton "TEST"
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

