package game.element;

/**
 * Mot Règle "ON"
 */
public class TextOn extends RuleItem {

	public TextOn()
	{
		setPriority(3);
		setName("TextOn");
		setGraphicsName("TextOn");
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