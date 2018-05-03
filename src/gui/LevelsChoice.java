package gui;

import java.io.File;
import java.util.regex.Pattern;

import game.levelManager.LevelManager;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LevelsChoice extends Menu{
	
	private static ImageView loadImage;
	private static boolean wasInit = false;
	
	private static void initButton() {
		wasInit = true;
		//Gestion de l'image "LOAD" qui permet de charger un niveau de l'utilisateur
		loadImage= new ImageView(new Image("file:ressources"+File.separator+"load.png"));
		loadImage.setPickOnBounds(true);
		loadImage.setOpacity(0.5);
		loadImage.setScaleX(0.75);
		loadImage.setScaleY(0.75);
		loadImage.setLayoutX(750);
		loadImage.setLayoutY(690);
		loadImage.setOnMouseClicked((MouseEvent e) -> {
			File fileChoose = FileChooserLvl();
			if(fileChoose == null)
				return;
			musicMenu.stop();
			Level.loadLevel(fileChoose.getPath().split(Pattern.quote("."))[0], true, true);
		});
		//Gestion de l'effet lorsqu'on passe sur l'image
		loadImage.setOnMouseEntered((MouseEvent e) -> {
			loadImage.setOpacity(1);
		});
		loadImage.setOnMouseExited((MouseEvent e) -> {
			loadImage.setOpacity(0.5);
		});
		
		//Bouton qui permet de revenir au menu principal
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
		//Gestion de l'effet lorsqu'on passe sur le bouton
		close.setOnMouseEntered((MouseEvent event) -> {
			close.setOpacity(1);
		});
		close.setOnMouseExited((MouseEvent event) -> {
			close.setOpacity(0.5);
		});
	}
	/**
	 * Méthode qui va afficher le menu des niveaux où l'utilisateur pourra choisir le niveau qu'il désire
	 * ou charger un de ses propres niveaux
	 */
	public static void loadLevelsMenu() {
		if(! wasInit)
			initButton();
		
		//Initialisation
		root = new Group();
		canvas = new Canvas(960,960);
		menu = new Scene(root, 960, 960);
		root.getChildren().add(canvas);
		primaryStage.setScene(menu);
		//Ce StackPane est représente le fond foncé qu'il y a sur les différents niveaux
		//Cela permet de ne pas devoir afficher un fond de cette couleur à chaque fois qu'on redessine une cellule
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		holder.setStyle("-fx-background-color: #1c1f22");
		root.getChildren().add(holder);
		//Récupération de l'outil qui va permettre de dessiner sur le canvas
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(new Image("file:ressources"+File.separator+"levelsMenu.png"), 0, 0); //Affichage de l'image de fond
		//Boucle permettant d'afficher et de contrôler les différents bouttons de niveaux (ex : LVL 1, LVL 2,...)
		int x = -65;
		for(int index = 0; index<=LevelManager.getListOfLevels().length; index++) {
			ImageView levelImage = new ImageView(new Image("file:ressources"+File.separator+"level"+index+".png"));
			levelImage.setPickOnBounds(true); //permet de cliquer sur l'entireté de l'image et non pas juste sur les pixels visibles
			levelImage.setOpacity(0.5);
			levelImage.setLayoutX(x);
			levelImage.setLayoutY(200);
			levelImage.setScaleX(0.75);
			levelImage.setScaleY(0.75);
			x+=100;
			final int i = index;
			levelImage.setOnMouseClicked((MouseEvent e) -> {
				musicMenu.stop();
				Level.loadLevel(LevelManager.getListOfLevels()[i-1], true, true);
			});
			//Réglage de l'effet lorsqu'on passe sur l'image
			levelImage.setOnMouseEntered((MouseEvent e) -> {
				levelImage.setOpacity(1);
			});
			levelImage.setOnMouseExited((MouseEvent e) -> {
				levelImage.setOpacity(0.5);
			});
			root.getChildren().add(levelImage); //Ajout de l'image à la scène
		}
		root.getChildren().addAll(loadImage, close); // Ajout des bouttons close et loadImage définient dans initButton()
		//Permet lorsque la touche "ESCAPE" est pressée de revenir au menu principal
		menu.setOnKeyPressed((KeyEvent event) -> {
			if(event.getCode().toString() == "ESCAPE")
				loadMenu();
		});
	}
}
