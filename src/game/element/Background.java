package game.element;

/**
 * Item qui repr�sente le fond du plateau
 *
 */

public class Background extends Item {
	
	public Background(int x, int y)
	{
		super(x,y);
		setPriority(0);
		setName("----");
	}
}
