package game.element;

/**
 * Mot R�gle "SINK"
 */
public class TextSink extends RuleItem {

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