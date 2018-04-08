package game.element;

/**
 * Mot Règle "YOU"
 */
public class TextYou extends RuleItem {

	public TextYou()
	{
		setPriority(3);
		setName("TextYou");
		setGraphicsName("TextYou");
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