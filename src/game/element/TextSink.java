package game.element;

/**
 * Mot R�gle "SINK"
 */
public class TextSink extends RuleItem {

	public TextSink()
	{
		setPriority(3);
		setName("SINK");
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