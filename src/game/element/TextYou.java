package game.element;

/**
 * Mot Règle "YOU"
 */
public class TextYou extends RuleItem {

	public TextYou(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(2);
		setName("YOU ");
	}
}