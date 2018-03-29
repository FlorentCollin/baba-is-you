package game.element;

/**
 * Mot Règle "GOOP"
 */
public class TextGoop extends RuleItem {

	public TextGoop(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(0);
		setName("GOOP");
	}
}