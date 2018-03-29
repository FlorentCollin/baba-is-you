package game.element;

/**
 * Mot Règle "SINK"
 */
public class TextSink extends RuleItem {

	public TextSink(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setOrderInRule(2);
		setName("SINK");
	}
}