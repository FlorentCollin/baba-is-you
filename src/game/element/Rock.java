package game.element;

/**
 * Objet "ROCK"
 *
 */
public class Rock extends Item {

	public Rock(int x, int y)
	{
		super(x,y);
		setPriority(1);
		setName("rock");
	}
}