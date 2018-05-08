package gui;

import java.io.File;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Sous classe de l'application principale qui va g�rer l'affichage des diff�rent(e)s conseils/astuces durant la partie
 *
 */
public class Advice extends BabaIsYouApp {

	//Fen�tre secondaire (d'astuces)
	private static Scene advice;
	private static Canvas canvasAdvice;
	private static Group rootAdvice;
	private static Text close;
	private static boolean wasInit = false;
	
	/**
	 * M�thode qui va initialiser l'ensemble de la fen�tre
	 */
	private static void initAll() {
		wasInit = true;
		//Initialisation de la stage
		rootAdvice = new Group();
		advice = new Scene(rootAdvice, 600, 600);
		secondaryStage.setScene(advice);
		canvasAdvice = new Canvas(600, 600);
		rootAdvice.getChildren().add(canvasAdvice);
		//R�glage de la positon en X et Y par rapport � la fen�tre principale
		//Ce qui permet de bien repositionner la fen�tre d'astuces lorsque la fen�tre principale est boug�e
		secondaryStage.setX(primaryStage.getX()+180);
		secondaryStage.setY(primaryStage.getY()+180);
		
		//Bouton "X" qui permet de fermer la fen�tre
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
		//R�glage de l'effet quand on passe sur le bouton
		close.setOnMouseEntered((MouseEvent e) -> {
			close.setOpacity(1);
		});
		close.setOnMouseExited((MouseEvent e) -> {
			close.setOpacity(0.5);
		});
		rootAdvice.getChildren().addAll(close); //Ajout du bouton 
	}
	/**
	 * M�thode qui va afficher la fen�tre d'astuce dans certains niveaux (ex: la fen�tre qui s'ouvre quand on joue au niveau 1)
	 * @param name Le nom de la fen�tre a ouvrir
	 * 
	 */
	public static void loadAdviceStage(String name) {
		if(!wasInit)
			initAll();
		//R�cup�ration de l'outil qui va nous permettre de dessinner sur le canvas
		GraphicsContext gc = canvasAdvice.getGraphicsContext2D();
		gc.drawImage(new Image("file:ressources"+File.separator+name+".png"), 0, 0); // Affichage du fond

		//Permet de fermer la fen�tre lorsque la touche "ESCAPE" est press�e
		advice.setOnKeyPressed((KeyEvent e) -> {
			if(e.getCode().toString().equals("ESCAPE"))
				secondaryStage.close();
		});
		
		secondaryStage.show();
	}
}
