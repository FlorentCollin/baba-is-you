package game.element;

/**
 * Mot Règle "STOP"
 */
public class TextStop extends RuleItem {

	public TextStop()
	{
		setPriority(3);
		setName("TextStop");
		setGraphicsName("TextStop");
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