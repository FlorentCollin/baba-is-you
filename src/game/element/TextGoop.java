package game.element;

/**
 * Mot R�gle "GOOP"
 */
public class TextGoop extends RuleItem {

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