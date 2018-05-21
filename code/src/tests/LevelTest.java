package tests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import game.levelManager.LevelManager;
import game.boardController.Board;
import game.boardController.Cell;
import game.element.Boundary;

public class LevelTest {
	
	/**
	 *Test d'un niveau avec plusieurs mondes parrallèles
	 */
	@Test
	public void loadLevelTest() {
		LevelManager.readLevel("code" + File.separator + "levels"+File.separator+"lvl4", true);
		Assert.assertTrue(LevelManager.getActivesBoards().size() == 2);
	}
	
	/**
	 * Test d'un grand niveau (200*200)
	 */
	@Test
	public void bigLevelTest() {
		LevelManager.readLevel("code" + File.separator + "levels"+File.separator+"tests"+File.separator+"bigLevel", true);
		Assert.assertTrue(LevelManager.getActivesBoards().get(0).getRows() == 202);
	}

	/**
	 * Test d'un niveau où un item est placé en dehors des limites du jeu
	 */
	@Test 
	public void badLevelTest1() {
		LevelManager.readLevel("code" + File.separator + "levels"+File.separator+"tests"+File.separator+"badLevel1", true);
		Board board = LevelManager.getActivesBoards().get(0);
		for(Cell[] element1 : board.getBoard()) {
			for(Cell element2 : element1) {
				Assert.assertTrue(element2.isEmpty() || element2.getLastItem() instanceof Boundary);
			}
		}
	}
	
	/**
	 * Test d'un niveau où le nom d'un item a mal été écrit
	 */
	@Test(expected = NullPointerException.class)
	public void badLevelTest2() {
		LevelManager.readLevel("code" + File.separator + "levels"+File.separator+"tests"+File.separator+"badLevel2", true);
	}

}
