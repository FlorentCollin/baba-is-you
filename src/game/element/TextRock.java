package game.element;

/**
 * Mot Règle "ROCK"
 */
public class TextRock extends RuleItem {

	public TextRock()
	{
		setPriority(3);
		setName("text_rock");
		setGraphicsName("TextRock");
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