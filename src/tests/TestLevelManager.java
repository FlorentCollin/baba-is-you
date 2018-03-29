package tests;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import game.element.Background;
import game.element.Cell;
import game.levelManager.LevelManager;

class TestLevelManager {

	@Test
	public void test1()
	{
		Background back = new Background(0,0);
		Cell[][] board = LevelManager.readLevel("testLvl");
		for(Cell[] element1 : board)
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
