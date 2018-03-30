package game.element;

/**
 * Classe abstraite servant de base à tous les éléments du jeu.
 * 
 */
public abstract class Item {
	
	private int priority; //Ordre de priorité utilisé pour l'affichage (0 = fond, 0>> = baba)
	private String name;
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Méthode booléenne qui indique si un Item est poussable
	 * @return true si l'objet est poussable false sinon
	 * 
	 */
	public boolean isPushable()
	{
		//TODO
		return true;
	}
	/**
	 * Méthode booléenne qui indique si un Item "stop" les autres Items
	 * @return true si l'objet est "stop" false sinon
	 * 
	 */
	public boolean isStop()
	{
		//TODO
		return false;
	}
	/**
	 * Méthode booléenne qui indique si un Item signifie la victoire
	 * @return true si l'objet est "win" false sinon
	 * 
	 */
	public boolean isWin()
	{
		//TODO
		return false;
	}
/**
 * Méthode qui bouge un objet dans une direction
 * @param direction Entier représentant la direction UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3
 */
	public void move(int direction)
	{
		//TODO
	}
	
	public String toString()
	{
		return getClass().toString();
	}
}
