package game.element;

/**
 * Mot R�gle "GOOP"
 */
public class TextGoop extends RuleItem {

	public TextGoop()
	{
		setPriority(3);
		setName("GOOP");
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