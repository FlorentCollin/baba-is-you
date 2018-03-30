package game.element;

/**
 *  R�gle "FLAG"
 */
public class TextFlag extends RuleItem {

	public TextFlag()
	{
		setPriority(3);
		setName("FLAG");
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