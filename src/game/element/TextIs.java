package game.element;

/**
 * Mot R�gle "IS"
 */
public class TextIs extends RuleItem {

	public TextIs()
	{
		setPriority(3);
		setName("is");
		setGraphicsName("TextIs");
	}

	@Override
	public boolean isVerb() {
		return true;
	}

	@Override
	public boolean isWord() {
		return false;
	}
}