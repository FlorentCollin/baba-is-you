package game.element;

/**
 * Mot R�gle "STOP"
 */
public class TextStop extends RuleItem {

	public TextStop()
	{
		setPriority(3);
		setName("stop");
		setGraphicsName("TextStop");
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