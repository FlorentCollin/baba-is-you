package game.element;

/**
 * Mot Règle "STOP"
 */
public class TextStop extends RuleItem {

	public TextStop()
	{
		setPriority(3);
		setName("STOP");
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