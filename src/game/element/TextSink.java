package game.element;

/**
 * Mot Règle "SINK"
 */
public class TextSink extends RuleItem {

	public TextSink()
	{
		setPriority(3);
		setName("TextSink");
		setGraphicsName("TextSink");
	}

	@Override
	public boolean isVerb() {
		return false;
	}

	@Override
	public boolean isWord() {
		return false;
	}

	@Override
	public boolean isAction() {
		return true;
	}
}