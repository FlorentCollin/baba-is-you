package game.element;

/**
 * Classe abstraite servant de base � tous les �l�ments du jeu.
 * 
 */
public abstract class Item {
	
	//Position
	private int x;
	private int y;
	private int priority; //Ordre de priorit� utilis� pour l'affichage (0 = fond, 0>> = baba)
	
	public Item() {}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * M�thode bool�enne qui indique si un Item est poussable
	 * @return true si l'objet est poussable false sinon
	 * 
	 */
	public boolean isPushable()
	{
		//TODO
		return true;
	}
	/**
	 * M�thode bool�enne qui indique si un Item "stop" les autres Items
	 * @return true si l'objet est "stop" false sinon
	 * 
	 */
	public boolean isStop()
	{
		//TODO
		return false;
	}
	/**
	 * M�thode bool�enne qui indique si un Item signifie la victoire
	 * @return true si l'objet est "win" false sinon
	 * 
	 */
	public boolean isWin()
	{
		//TODO
		return false;
	}
/**
 * M�thode qui bouge un objet dans une direction
 * @param direction Entier repr�sentant la direction UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3
 */
	public void move(int direction)
	{
		
	}
}
