package game.element;

/**
 * Classe abstraite repr�sentant les T�l�porteurs
 */
public abstract class TextTp extends RuleItem {

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