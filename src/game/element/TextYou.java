package game.element;

/**
 * Mot R�gle "YOU"
 */
public class TextYou extends RuleItem {

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