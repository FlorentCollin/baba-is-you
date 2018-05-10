package tests;

import java.io.File;

import org.junit.Test;
import org.junit.Assert;

import game.boardController.Board;
import game.boardController.MoveController;
import game.boardController.Rules;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;

public class MoveTest {

	private static Board board;

	public static void move(int direction) {
		// Itération sur la liste des joueurs pour les déplacer
		for (Tuple player : board.getPlayers()) {
			// Déplacement d'un joueur en fonction de la direction
			MoveController.move(board, player.getX(), player.getY(), player.getZ(), direction); // (i+1)%4 car on veut
																								// commencer à le faire
																								// bouger à droite et
																								// non en haut
		}
		board.searchPlayers(); // On recherche les nouveaux joueurs
	}

	@Test
	public void moveWithoutRules() {
		// Liste des positions si le déplacement à bien été effectué
		Integer[] expectedX = { 2, 2, 1, 1 };
		Integer[] expectedY = { 1, 2, 2, 1 };

		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "moveTestWithoutRules", true);
		board = LevelManager.getActivesBoards().get(0);
		for (int i = 0; i < 4; i++) {
			move((i + 1) % 4);
			Tuple position = board.getPlayers().get(0);
			Assert.assertTrue(position.getX() == expectedX[i] && position.getY() == expectedY[i]);

		}
	}

	@Test
	public void moveWithRules() {
		// Liste des positions si le déplacement à bien été effectué
		Integer[] expectedX = { 1, 1, 1, 2 };
		Integer[] expectedY = { 2, 2, 2, 2 };
		Integer[] direction = { 0, 2, 3, 1 }; // On fait bouger le joueur dans un certain ordre pour avoir plus de
												// facilité à tester

		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "moveTestWithRules", true);
		board = LevelManager.getActivesBoards().get(0);
		for (int i = 0; i < 4; i++) {
			// Itération sur la liste des joueurs pour les déplacer
			move(direction[i]);
			Tuple position = board.getPlayers().get(0);
			Assert.assertTrue(position.getX() == expectedX[i] && position.getY() == expectedY[i]);

		}
	}

	@Test
	public void breakRules() {
		Integer[] direction = { 0, 3, 0, 0, 1, 2 }; // On fait bouger le joueur dans un certain ordre pour avoir plus de
													// facilité à tester
		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "moveTestBreakRules", true);
		board = LevelManager.getActivesBoards().get(0);
		for (int i = 0; i < direction.length; i++) {
			move(direction[i]);
			if (i == 0)
				Assert.assertTrue(Rules.getListOfRulesActives().size() == 1);
			if (i == 5)
				Assert.assertTrue(Rules.getListOfRulesActives().size() == 2);
		}
	}

	@Test
	public void winTest() {
		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "winTest", true);
		board = LevelManager.getActivesBoards().get(0);
		move(1); // On fait bouger les joueurs à droite
		Assert.assertTrue(board.isWin());

	}

	@Test
	public void loseTest1() {
		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "loseTest1", true);
		board = LevelManager.getActivesBoards().get(0);
		move(0); // On fait bouger les joueurs en haut
		Assert.assertTrue(board.getPlayers().size() == 0); // S'il n'y a plus de joueurs en a perdu
	}

	@Test
	public void loseTest2() {
		LevelManager.readLevel("levels" + File.separator + "tests" + File.separator + "loseTest2", true);
		board = LevelManager.getActivesBoards().get(0);
		move(0); // On fait bouger les joueurs en haut
		Assert.assertTrue(board.getPlayers().size() == 0); // S'il n'y a plus de joueurs en a perdu
	}

}
