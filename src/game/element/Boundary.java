package game.element;

/**
 * Objet repr�sentant la bordure (limite) de la map
 *
 */
public class Boundary extends Item {

	public Boundary()
	{
		setName("####");
		setPriority(0);
	}
	
	public boolean isPushable()
	{
		return false;
	}
	
	public boolean isStop()
	{
		return true;
	}
}
