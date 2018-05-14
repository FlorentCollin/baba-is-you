package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import userProfile.Success;

/**
 * Classe qui sert � controller le menu des succ�s
 * @author Florent
 *
 */
public class SuccessMenu extends Menu {

	private static boolean wasInit = false;
	private static double unlockRate = 0;
	private static Text unlockRateText;

	/**
	 * Initialise l'ensemble des textes et des boutons pour ne pas devoir �
	 * chaque fois les recharger
	 */
	private static void initButton() {
		wasInit = true;

		// Bouton qui permet de revenir au menu principal
		close = new Text("X");
		close.setFont(fontMadness);
		close.setFill(Color.WHITE);
		close.setOpacity(0.5);
		close.setScaleX(3);
		close.setScaleY(3);
		close.setX(910);
		close.setY(45);
		close.setOnMouseClicked((MouseEvent event) -> {
			loadMenu();
		});
		// Gestion de l'effet lorsqu'on passe sur le bouton
		close.setOnMouseEntered((MouseEvent event) -> {
			close.setOpacity(1);
		});
		close.setOnMouseExited((MouseEvent event) -> {
			close.setOpacity(0.5);
		});

		// Texte qui indique le pourcentage de succ�s d�j� r�alis�
		unlockRateText = new Text();
		unlockRateText.setFill(Color.WHITE);
		unlockRateText.setFont(fontMadnessBig);
		unlockRateText.setY(130);
		unlockRateText.setX(400);
		unlockRateText.setScaleX(1.6);
		unlockRateText.setScaleY(1.6);

	}

	/**
	 * M�thode qui va afficher le menu des niveaux o� l'utilisateur pourra choisir
	 * le niveau qu'il d�sire ou charger un de ses propres niveaux
	 */
	public static void loadSuccessMenu() {
		if (!wasInit)
			initButton();

		// Initialisation
		root = new Group();
		canvas = new Canvas(960, 960);
		menu = new Scene(root, 960, 960);
		root.getChildren().add(canvas);
		primaryStage.setScene(menu);
		// Ce StackPane est repr�sente le fond fonc�
		StackPane holder = new StackPane();
		holder.getChildren().add(canvas);
		holder.setStyle("-fx-background-color: #1c1f22");
		root.getChildren().add(holder);

		// R�cup�ration de l'outil qui va permettre de dessiner sur le canvas
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(new Image("file:ressources" + File.separator + "successMenu.png"), 0, 0); // Affichage de l'image
																								// de
																								// fond

		// Boucle permettant d'afficher les diff�rents succ�s.
		Map<String, ArrayList<Object>> allSuccess = Success.getSuccess();
		int y = 220;
		//It�ration sur l'ensemble des valeurs des succ�s
		for (ArrayList<Object> value : allSuccess.values()) {
			Text successText = new Text("- " + (String) value.get(1));
			successText.setFill(Color.WHITE);
			successText.setFont(fontMadnessBig);
			// Si l'utilisateur � r�ussi le succ�s alors on change son opacit�
			if ((boolean) value.get(0)) {
				successText.setOpacity(1);
				unlockRate++; // Incr�mentation du compteur de succ�s d�bloqu�s pour pouvoir en faire un pourcentage
			} else
				successText.setOpacity(0.5);
			successText.setLayoutX(35);
			successText.setLayoutY(y);
			y += 30;
			root.getChildren().add(successText); // Ajout de l'image � la sc�ne
		}
		//Calcul du pourcentage de succ�s d�bloqu�s
		unlockRateText.setText((int)(unlockRate/allSuccess.size()*100) + "% UNLOCKED");
		unlockRate = 0; //Re initialisation du pourcentage
		root.getChildren().addAll(close, unlockRateText); // Ajout du boutton close et du texte qui indique le
															// pourcentage de succ�s d�j� r�alis�s
		// Permet lorsque la touche "ESCAPE" est press�e de revenir au menu principal
		menu.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode().toString() == "ESCAPE")
				loadMenu();
		});
	}
}
