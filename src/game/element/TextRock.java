package game.element;

/**
 * Mot R�gle "ROCK"
 */
public class TextRock extends RuleItem {

	public TextRock()
	{
		setPriority(3);
		setName("ROCK");
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