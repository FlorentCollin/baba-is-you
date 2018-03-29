package game.element;

/**
 * Objet "WALL"
 *
 */
public class Wall extends Item {

	public Wall(int x, int y)
	{
		super(x,y);
		setPriority(1);
		setName("wall");
	}
}
