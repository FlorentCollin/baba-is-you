package game.element;

/**
 * Mot R�gle "ROCK"
 */
public class TextRock extends RuleItem {

	public TextRock(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(0);
		setName("ROCK");
	}
}