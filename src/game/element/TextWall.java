package game.element;

/**
 * Mot R�gle "WALL"
 */
public class TextWall extends RuleItem {

	public TextWall(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(0);
		setName("WALL");
	}
}

