package game.element;

/**
 * Mot Règle "IS"
 */
public class TextIs extends RuleItem {

	public TextIs()
	{
		setPriority(3);
		setName("TextIs");
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

	@Override
	public boolean isAction() {
		return false;
	}
}