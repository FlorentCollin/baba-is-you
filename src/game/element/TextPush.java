package game.element;

/**
 * Mot R�gle "PUSH"
 */
public class TextPush extends RuleItem {

	public TextPush(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(2);
		setName("PUSH");
	}
}