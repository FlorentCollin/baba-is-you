package game.element;

/**
 * Mot Règle "PUSH"
 */
public class TextPush extends RuleItem {

	public TextPush()
	{
		setPriority(3);
		setName("TextPush");
		setGraphicsName("TextPush");
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
		// TODO Auto-generated method stub
		return true;
	}
}