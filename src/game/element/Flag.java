package game.element;

/**
 * Objet "FLAG"
 *
 */
public class Flag extends Item {

	public Flag(int x, int y)
	{
		super(x,y);
		setPriority(1);
		setName("flag");
	}
}