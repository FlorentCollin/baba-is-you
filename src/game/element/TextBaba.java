package game.element;

/**
 * Mot Règle "BABA"
 */
public class TextBaba extends RuleItem {

	public TextBaba(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(0);
		setName("BABA");
	}
}