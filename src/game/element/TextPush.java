package game.element;

/**
 * Mot R�gle "PUSH"
 */
public class TextPush extends RuleItem {

	public TextPush()
	{
		setPriority(3);
		setName("push");
		setGraphicsName("TextPush");
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