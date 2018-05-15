package tests;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import game.levelManager.LevelManager;

public class LevelTest {
	
	@Test
	public void loadLevelTest() {
		LevelManager.readLevel("levels"+File.separator+"lvl4", true);
		Assert.assertTrue(LevelManager.getActivesBoards().size() == 2);
	}
	
	@Test
	public void bigLevelTest() {
		LevelManager.readLevel("levels"+File.separator+"tests"+File.separator+"bigLevel", true);
		Assert.assertTrue(LevelManager.getActivesBoards().get(0).getRows() == 202);
	}

	@Test 
	public void badLevelTest1() {
		LevelManager.readLevel("levels"+File.separator+"tests"+File.separator+"badLevel1", true);
		Assert.assertTrue(LevelManager.getActivesBoards().get(0).getLevelNumber() == 0);
	}
	
	@Test(expected = NullPointerException.class)
	public void badLevelTest2() {
		LevelManager.readLevel("levels"+File.separator+"tests"+File.separator+"badLevel2", true);
	}

}
