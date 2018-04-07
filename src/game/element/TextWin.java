package game.element;

/**
 * Mot R�gle "WIN"
 */
public class TextWin extends RuleItem {

	public TextWin()
	{
		setPriority(3);
		setName("WIN ");
		setGraphicsName("TextWin");
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