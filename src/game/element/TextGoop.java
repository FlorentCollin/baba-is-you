package game.element;

/**
 * Mot Règle "GOOP"
 */
public class TextGoop extends RuleItem {

	public TextGoop()
	{
		setPriority(3);
		setName("text_goop");
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
}