package game.element;

/**
 * Mot Règle "SINK"
 */
public class TextSink extends RuleItem {

	public TextSink()
	{
		setPriority(3);
		setName("sink");
		setGraphicsName("TextSink");
	}

	@Override
	public boolean isVerb() {
		return false;
	}

	@Override
	public boolean isWord() {
		return true;
	}
}