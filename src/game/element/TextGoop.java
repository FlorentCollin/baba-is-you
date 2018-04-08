package game.element;

/**
 * Mot Règle "GOOP"
 */
public class TextGoop extends RuleItem {

	public TextGoop()
	{
		setPriority(3);
		setName("TextGoop");
		setGraphicsName("TextGoop");
	}

	@Override
	public boolean isVerb() {
		return false;
	}

	@Override
	public boolean isWord() {
		return true;
	}

	@Override
	public boolean isAction() {
		return false;
	}
}