package game.element;

/**
 * Mot Règle "IS"
 */
public class TextIs extends RuleItem {

	public TextIs(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(1);
		setName(" IS ");
	}
}