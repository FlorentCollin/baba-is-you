package game.element;


public class Baba extends Item{
	
	public Baba()
	{
		setPriority(3);
		setName("baba");
	}
	// A MODIFIER TODO car baba r�agit aussi au r�gles
	public boolean isPushable()
	{
		return true;
	}
	
	public boolean isStop()
	{
		return false;
	}
	
	public boolean isWin()
	{
		return false;
	}
}
