package game.element;

/**
 * Classe abstraite servant de base à tous les éléments du jeu.
 * 
 */
abstract class Item {
	
	//Position
	int x;
	int y;
	int priority; //Ordre de priorité utilisé pour l'affichage (0 = fond, 0>> = baba)
	
	public Item()
	{
		//nothing to do here ?
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
}
