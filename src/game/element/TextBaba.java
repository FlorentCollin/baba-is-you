package game.element;

/**
 * Mot Règle "BABA"
 */
public class TextBaba extends RuleItem {

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