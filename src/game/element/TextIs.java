package game.element;

/**
 * Mot Règle "IS"
 */
public class TextIs extends RuleItem {

	public TextIs()
	{
		setPriority(3);
		setName(" IS ");
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