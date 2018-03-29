package game.element;


public class Baba extends Item{
	
	public Baba(int x, int y)
	{
		super(x,y);
		setPriority(3);
		setName("baba");
	}
	
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
