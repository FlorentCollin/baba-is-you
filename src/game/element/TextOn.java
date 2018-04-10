package game.element;

/**
 * Mot R�gle "ON"
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