package game.element;

/**
 * Mot R�gle "ROCK"
 */
public class TextRock extends RuleItem {

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