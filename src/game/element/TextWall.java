package game.element;

/**
 * Mot R�gle "WALL"
 */
public class TextWall extends RuleItem {

	public TextWall()
	{
		setPriority(3);
		setName("TextWall");
		setGraphicsName("TextWall");
	}

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

