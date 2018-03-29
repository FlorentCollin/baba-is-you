package game.element;

/**
 * Mot R�gle "STOP"
 */
public class TextStop extends RuleItem {

	public TextStop(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(2);
		setName("STOP");
	}
}