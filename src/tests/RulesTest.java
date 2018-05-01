package tests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import game.boardController.Rules;
import game.element.IRule;
import game.element.Item;
import game.levelManager.LevelManager;

public class RulesTest {

	@Test
	public void rulesInBoard() {
		ArrayList<String[]> realRules = new ArrayList<>();
		realRules.add(new String[] {"TextWall","TextIs","TextStop"});
		realRules.add(new String[] {"TextRock","TextIs","TextPush"});
		realRules.add(new String[] {"TextBaba","TextIs","TextYou"});
		realRules.add(new String[] {"TextFlag","TextIs","TextWin"});
		LevelManager.readLevel(LevelManager.getListOfLevels()[0]); //Lecture du premier niveau
		ArrayList<IRule[]> listOfRules = Rules.getListOfRulesActives();
		for(int i=0; i<listOfRules.size(); i++) {
			for(int j=0; j<listOfRules.get(i).length; j++) {
				Assert.assertTrue(realRules.get(i)[j].equals(((Item)listOfRules.get(i)[j]).getName()));
			}
		}
	}
}