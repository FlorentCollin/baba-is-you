package game.element;

/**
 * Mot Règle "WIN"
 */
public class TextWin extends RuleItem {

	public TextWin()
	{
		setPriority(3);
		setName("TextWin");
		setGraphicsName("TextWin");
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