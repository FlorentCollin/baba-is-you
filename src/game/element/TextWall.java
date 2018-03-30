package game.element;

/**
 * Mot R�gle "WALL"
 */
public class TextWall extends RuleItem {

	public TextWall()
	{
		setPriority(3);
		setName("WALL");
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

