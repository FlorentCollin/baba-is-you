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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Sous classe de Level qui va charger l'�diteur de niveau
 *
 */
public class Editor extends Level {

	//Chemin d'accès
	private static final String ressourcesPath ="code" + File.separator + "ressources" + File.separator;
	private static final String levelsPath = "code" + File.separator + "levels" + File.separator;	

	private static Item selectedItem;

	private static TextField saveText;
	private static Button saveButton;
	private static Button testButton;
	private static Button resetButton;
	private static Button loadButton;
	private static Text textZone;
	private static Text textSelectedItem;
	private static Text buttonText;
	private static Text numberBoardText;
	// Variable servant � savoir si l'�diteur � �t� charg� au moins une fois depuis
	// le lancement de l'application
	private static boolean wasInit = false;

	/**
	 * M�thode qui va initialiser l'ensemble des bouttons et textes pr�sents sur
	 * l'�diteur (pour ne pas les reinitilaliser d�s que l'utilisateur veut acc�der
	 * � l'�diteur)
	 */
	private static void initButton() {
		wasInit = true; // Permet de ne pas recharger les boutons � chaque fois qu'on acc�de � l'�diteur
		// On initialise l'enti�ret� des bouttons et zone de textes qui permettent de
		// charger, tester, sauvegarder un niveau

		// TextField pour entrer le nom du niveau � sauvegarder
		saveText = new TextField();
		saveText.setFont(fontMadness);
		saveText.setPromptText("name");
		saveText.setLayoutX(53);
		saveText.setLayoutY(45);

		// Boutton qui permet de charger un niveau
		loadButton = new Button("LOAD");
		loadButton.setFont(fontMadness);
		loadButton.setOnMouseClicked(loadButtonClicked());
		loadButton.setLayoutX(250);
		loadButton.setLayoutY(45);

		// Boutton qui va permettre � l'utilisateur de sauvegarder le niveau qu'il a
		// cr�e
		saveButton = new Button("SAVE");
		saveButton.setFont(fontMadness);
		saveButton.setOnMouseClicked(saveButtonClicked());
		saveButton.setLayoutX(330);
		saveButton.setLayoutY(45);

		// Boutton qui va reset l'�diteur de niveau
		resetButton = new Button("RESET");
		resetButton.setFont(fontMadness);
		resetButton.setOnMouseClicked(resetButtonClicked());
		resetButton.setLayoutX(410);
		resetButton.setLayoutY(45);

		// Boutton qui va permettre � l'utilisateur de tester le niveau qu'il vient de
		// cr�er
		testButton = new Button("TEST");
		testButton.setFont(fontMadness);
		testButton.setOnMouseClicked(testButtonClicked());
		testButton.setLayoutX(500);
		testButton.setLayoutY(45);

		// Texte qui va indiquer si le niveau � bien �t� sauvegarder
		textZone = new Text("");
		textZone.setFont(fontMadness);
		textZone.setFill(Color.WHITE);
		textZone.setScaleX(1.5);
		textZone.setScaleY(1.5);
		textZone.setLayoutX(80);
		textZone.setLayoutY(110);

		// Texte qui va dire � quel monde parall�le se trouve le joueur (ex: 2/3)
		numberBoardText = new Text();
		numberBoardText.setFont(fontMadness);
		numberBoardText.setFill(Color.WHITE);
		numberBoardText.setLayoutX(60);
		numberBoardText.setLayoutY(145);
		numberBoardText.setScaleX(1.3);
		numberBoardText.setScaleX(1.3);

		// Texte qui va donner des informations sur les bouttons qui permettent
		// d'ajouter/supprimer des mondes parall�les
		buttonText = new Text();
		buttonText.setLayoutX(280);
		buttonText.setLayoutY(145);
		buttonText.setFont(fontMadness);
		buttonText.setFill(Color.WHITE);

		// Text "Selected Item : " (en haut � droite)
		textSelectedItem = new Text("SELECTED  ITEM  : ");
		textSelectedItem.setFont(fontMadness);
		textSelectedItem.setFill(Color.WHITE);
		textSelectedItem.setScaleX(1.5);
		textSelectedItem.setScaleY(1.5);
		textSelectedItem.setLayoutX(685);
		textSelectedItem.setLayoutY(65);
	}

	/**
	 * M�thode qui va charger l'Editeur de niveau
	 * 
	 * @param levelName
	 *            Nom du niveau � charger
	 */
	public static void loadEditor(String levelName, boolean eraseBoards) {
		if (!wasInit) // Initilisation des bouttons et des textes pr�sents dans l'�diteur
			initButton();

		/*
		 * On charge le niveau comme un niveau normal car l'�diteur est juste une sorte
		 * de gros niveau en 22x22 (voir le fichier "levels/cleanEditor.txt pour mieux
		 * comprendre
		 */
		loadLevel(levelName, eraseBoards, false); // Le false indique qu'on n'active pas les inputs clavier de
													// l'utilisateur
		// On met � jour le texte qui affiche dans quel monde l'utilisateur se trouve
		numberBoardText.setText(board.getDepthOfLevel() + 1 + " | " + LevelManager.getActivesBoards().size());

		// Images qui permettent de rajouter/supprimer une pronfondeur de niveau
		String[] imagesToLoad = { "file:" + ressourcesPath + File.separator + "+.png",
		"file:" + ressourcesPath + File.separator + "-.png", "file:" + ressourcesPath + File.separator + "leftArrow.png",
		"file:" + ressourcesPath + File.separator + "rightArrow.png" };
		// Texte qui va correspondre � l'image ci-dessus
		String[] textCorresonding = { "Add one parralel world", "Delete the last parralel world",
				"Show the previous parralel world", "Show the next parralel world" };
		int x1 = 80;
		// Boucle qui affiche les boutons "+ - --> <--"
		for (int index = 0; index < imagesToLoad.length; index++) {
			ImageView image = new ImageView(new Image(imagesToLoad[index]));
			image.setX(x1);
			image.setY(90);
			image.setScaleX(0.3);
			image.setScaleY(0.3);
			image.setOpacity(0.5);
			image.setPickOnBounds(true);

			final int i = index; // permet d'utiliser i dans la fonction ci-dessous

			image.setOnMousePressed((MouseEvent event) -> {
				// Switch qui permet de savoir dans quelle image l'utilisateur � appuyer
				switch (i) {
				case 0: // + | Ajout d'un monde suppl�mentaire
					loadEditor(levelsPath + File.separator + "cleanEditor", false);
					break;
				case 1: // - | Retrait du dernier monde
					if (LevelManager.getActivesBoards().size() > 1) {
						LevelManager.removeLastBoard();
						if (board.getDepthOfLevel() > 0) {
							board = LevelManager.getActivesBoards().get(board.getDepthOfLevel() - 1);
						}
						drawBoard();
					}
					break;
				case 2: // <-- | Charge le monde pr�c�dent
					if (board.getDepthOfLevel() - 1 >= 0) {
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel() - 1);
						drawBoard(); // On est oblig� de re peintre le niveau car il a compl�tement chang�
					}
					break;
				case 3: // --> | Charge le monde suivant
					if (board.getDepthOfLevel() < LevelManager.getActivesBoards().size() - 1) {
						board = LevelManager.getActivesBoards().get(board.getDepthOfLevel() + 1);
						drawBoard();
					}
					break;
				}
				// On met � jour le texte qui affiche dans quel monde l'utilisateur se trouve
				numberBoardText.setText(board.getDepthOfLevel() + 1 + " | " + LevelManager.getActivesBoards().size());

			});
			image.setOnMouseEntered((MouseEvent event) -> {
				image.setOpacity(1);
				buttonText.setText(textCorresonding[i]); // Mise � jour du texte d'information
			});
			image.setOnMouseExited((MouseEvent event) -> {
				image.setOpacity(0.5);
				buttonText.setText(""); // Mise � jour du texte d'information
			});

			x1 += 35;
			root.getChildren().add(image);
		}

		// On ajoute tout les bouttons initialis�s � la sc�ne
		root.getChildren().addAll(loadButton, saveText, saveButton, testButton, resetButton, textZone, textSelectedItem,
				numberBoardText, buttonText);

		/*
		 * Gestionnaire d'�v�nements pour la souris qui va permettre de s�lectionner un
		 * Item et de pouvoir le placer dans le niveau
		 */
		game.setOnMouseClicked((MouseEvent event) -> {
			// On r�cup�re la position en X et en Y de la souris
			double mouseX = event.getX();
			double mouseY = event.getY();
			// Si le boutton droit de la souris est press� on simule un clic sur la gomme
			if (event.getButton() == MouseButton.SECONDARY) {
				selectedItem = new Eraser();
				board.setCell(22, 1, new Cell(selectedItem));
				drawCell(22, 1);
			}
			/*
			 * Si la souris se situe dans la zone des items s�lectionnables (voir en jeu
			 * pour mieux comprendre) alors on cherche l'item dans le board. S'il existe on
			 * le place dans la variable Item selectedItem
			 */
			if (mouseX >= 20 * CELL_SIZE && mouseX <= 23 * CELL_SIZE && mouseY >= 4 * CELL_SIZE
					&& mouseY <= 23 * CELL_SIZE) { // D�finit un rectangle de pixels
				selectedItem = board.getBoard()[(int) (mouseY / CELL_SIZE)][(int) (mouseX / CELL_SIZE)].getLastItem();
				if (selectedItem != null) {
					board.setCell(22, 1, new Cell(selectedItem));
					drawCell(22, 1);
				}
				return;
			}
			/*
			 * Si la souris se trouve dans la zone o� l'utilisateur peut composer son propre
			 * niveau alors l'utilisateur peut int�ragir avec lui
			 */
			else if (mouseX >= 1 * CELL_SIZE && mouseX < 19 * CELL_SIZE && mouseY >= 5 * CELL_SIZE
					&& mouseY <= 23 * CELL_SIZE) { // D�finit un rectangle de pixels
				if (selectedItem == null) { // On v�rifie tout de m�me s'il a bien s�lectionner un Item � placer
					return;
				}
				// Coordonn�es de la cellule o� placer l'Item
				int x = (int) (mouseX / CELL_SIZE);
				int y = (int) (mouseY / CELL_SIZE);

				// Si l'utilisateur � s�lectionner l'outil gomme alors on supprime ce qui se
				// trouve dans la cellule
				if (selectedItem instanceof Eraser) {
					board.setCell(x, y, new Cell());
				}
				// S'il y a d�j� un Item dans la cellule on y ajoute l'Item s�lectionn�
				else if (board.getCell(x, y).getList().size() > 0) {
					board.getCell(x, y).add(selectedItem);
				} else {
					board.setCell(x, y, new Cell(selectedItem)); // On cr�e une nouvelle cellule qui ne contiendra
																		// que l'Item s�lectionn�
				}
				textZone.setText(""); // On met la textZone � "" pour ne pas afficher que le niveau a �t� sauvegarder
										// alors qu'il ne l'est pas
				drawCell(x, y); // Mise � jour de la cellule qui a �t� modifi�e
			}
		});

		// Gestionnaire d'�v�nements du clavier : Comme on se situe dans l'�diteur de
		// niveau si l'utilisateur appuie sur "ESCAPE" on affiche le menu principal
		game.setOnKeyPressed((KeyEvent event) -> {
			String code = event.getCode().toString();
			if (code.equals("ESCAPE")) {
				// Sauvegarde automatique du niveau que l'utilisateur est en train de composer
				// pour ne pas qu'il le perde s'il appuie par erreur sur "ESCAPE"
				LevelManager.saveLvlEditor("testEditor"); // On sauvegarde le fichier dans le bon format
				Menu.loadMenu();
			}
		});
	}

	// Clic sur les diff�rents bouttons
	/**
	 * M�thode qui va changer la zone de texte
	 * 
	 * @return l'EventHandler qui g�re l'appui sur le bouton "save"
	 */
	private static EventHandler<MouseEvent> saveButtonClicked() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (saveText.getText().equals("testEditor")) { // Fichier n�cessaire au bon fonctionnement du jeu
					textZone.setText("ENTER AN OTHER NAME.");
				} else if (saveText.getText().length() > 0) { // Si la sauvegarde � bien eu lieu
					if(success.unlock("Creator"))
						showSuccessUnlocked();
					LevelManager.saveLvlEditor(saveText.getText());
					textZone.setText("LEVEL SAVED !");
				} else { // Si la zone de texte est vide
					textZone.setText("ENTER A NAME !");
				}
			}

		};
	}

	/**
	 * M�thode qui va s'occuper de la gestion du boutton "Reset"
	 * 
	 * @return l'EventHandler qui g�re l'appui sur le bouton "reset"
	 */
	private static EventHandler<MouseEvent> resetButtonClicked() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// On supprime les niveaux temporaires pour que si l'utilisateur quitte et
				// reviens dans l'�diteur de niveau l'�diteur soit vide
				File[] files = { new File(levelsPath + File.separator + "editor" + File.separator + "cleanEditor.txt"),
						new File(levelsPath + File.separator + "editor" + File.separator + "testEditor.txt") };
				for (File file : files) {
					if (file.exists())
						file.delete();
				}
				loadEditor(levelsPath + File.separator + "cleanEditor", true); // Chargement de l'�diteur vide
			}
		};
	}

	/**
	 * M�thode qui sa s'occuper de la gestion du boutton "LOAD"
	 * 
	 * @return l'EventHandler qui g�re l'appui sur le boutton "load"
	 */
	private static EventHandler<MouseEvent> loadButtonClicked() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				File file = FileChooserLvl();
				if (file == null) {
					return;
				}
				loadEditor(LevelManager.loadUserLvl(file), true); // Chargement du niveau choisi par l'utilisateur dans
																	// l'�diteur de niveau
			}
		};
	}

	/**
	 * M�thode qui va s'occuper de la gestion du boutton "TEST"
	 * 
	 * @return l'EventHandler qui g�re l'appui sur le bouton "test"
	 */
	private static EventHandler<MouseEvent> testButtonClicked() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				LevelManager.saveLvlEditor("testEditor"); // On sauvegarde le fichier dans le bon format
				// On charge le fichier qu'on vient de sauvegarder
				loadLevel(levelsPath + File.separator + "editor" + File.separatorChar + "testEditor", true, true);
			}
		};
	}
}
