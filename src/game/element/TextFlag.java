package game.element;

/**
 *  R�gle "FLAG"
 */
public class TextFlag extends RuleItem {

	public TextFlag(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(0);
		setName("FLAG");
	}
}