package game.element;

/**
 * Mot R�gle "YOU"
 */
public class TextYou extends RuleItem {

	public TextYou()
	{
		setPriority(3);
		setName("YOU ");
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