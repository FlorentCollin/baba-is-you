package tests;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import game.element.Background;
import game.element.Board;
import game.element.Cell;
import game.levelManager.LevelManager;

class TestLevelManager {

	@Test
	public void test1()
	{
		Background back = new Background();
		Board board = LevelManager.readLevel("testLvl");
		for(Cell[] element1 : board.getBoard())
		{
			for(Cell element2 : element1)
			{
				if(! element2.getItem(0).getClass().equals(back.getClass()))
				{
					assertTrue(false);
				}
			}
		}
	}

}
