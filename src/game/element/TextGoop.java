package game.element;

/**
 * Mot R�gle "GOOP"
 */
public class TextGoop extends RuleItem {

	public TextGoop()
	{
		setPriority(3);
		setName("GOOP");
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