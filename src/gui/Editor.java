package gui;


import java.io.File;

import game.boardController.Cell;
import game.element.Eraser;
import game.element.Item;
import game.levelManager.LevelManager;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Editor extends Level {

	private static Item selectedItem;

	private static TextField saveText;
	private static Button saveButton;
	private static Button testButton;
	private static Button resetButton;
	private static Button loadButton;
	private static Text textZone;
	private static Text textSelectedItem;
	
	private static boolean wasInit = false;
	
	
	private static void initButton() {
		wasInit = true; //Permet de ne pas recharger les boutons à chaque fois qu'on accède à l'éditeur
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
	}
	/**
	 * Méthode qui va charger l'Editeur de niveau
	 * @param levelName Nom du niveau à charger
	 */
	public static void loadEditor(String levelName, boolean eraseBoards) {
		if(!wasInit)
			initButton();
		/* On charge le niveau comme un niveau normal car l'éditeur est juste une sorte de gros niveau en 22*22
		 * (voir le fichier "levels/cleanEditor.txt pour mieux comprendre */
		loadLevel(levelName, eraseBoards, false);
		
		//Image qui permet de rajouter une pronfondeur de niveau supplémentaire
		String[] imagesToLoad = {"file:ressources"+File.separator+"+.png", "file:ressources"+File.separator+"-.png",
				"file:ressources"+File.separator+"leftArrow.png","file:ressources"+File.separator+"rightArrow.png"};
		int x1 = 100;
		//Boucle qui affiche les boutons  "+  -  -->  <--"
		for(int index = 0; index<4; index++) {
			ImageView image = new ImageView(new Image(imagesToLoad[index]));
			image.setX(x1);
			image.setY(90);
			image.setScaleX(0.3);
			image.setScaleY(0.3);
			image.setOpacity(0.5);
			image.setPickOnBounds(true);
			final int i = index;
			//TODO
			image.setOnMousePressed((MouseEvent event) -> {
				switch(i) {
				case 0:
					loadEditor("levels"+File.separator+"cleanEditor", false);
					System.out.println(LevelManager.getActivesBoards().size());
					break;
				case 1:
					System.out.println("-");
					break;
				case 2:
					if(board.getDepthOfLevel()-1>=0)
					{
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel()-1);
						System.out.println(board.getDepthOfLevel());
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
					break;
				case 3:
					if(board.getDepthOfLevel()<LevelManager.getActivesBoards().size()-1)
					{
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel()+1);
						System.out.println(board.getDepthOfLevel());
						CELL_SIZE = canvas.getHeight()/Math.max(board.getCols(), board.getRows());
						drawBoard();
					}
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
					Menu.loadMenu();
				}
		});
	}
	
	// Click boutton
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
				loadEditor("levels"+File.separator+"cleanEditor", true);
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
				File file = FileChooserLvl();
				if(file == null)
				{
					return;
				}
				 loadEditor(LevelManager.loadUserLvl(file), true); //Chargement du niveau choisi par l'utilisateur dans l'éditeur de niveau
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
				loadLevel("levels"+File.separator+"editor"+File.separatorChar+"testEditor", true, true); //On charge le fichier qu'on vient de sauvegarder
			}
		};
	}
}
