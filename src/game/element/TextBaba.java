package game.element;

/**
 * Mot Règle "BABA"
 */
public class TextBaba extends RuleItem {

	public TextBaba()
	{
		setPriority(3);
		setName("text_baba");
		setGraphicsName("TextBaba");
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