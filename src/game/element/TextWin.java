package game.element;

/**
 * Mot Règle "WIN"
 */
public class TextWin extends RuleItem {

	public TextWin(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(2);
		setName("WIN ");
	}
}