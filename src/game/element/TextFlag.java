package game.element;

/**
 *  Règle "FLAG"
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