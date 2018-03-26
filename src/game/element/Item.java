package game.element;

/**
 * Classe abstraite servant de base � tous les �l�ments du jeu.
 * 
 */
abstract class Item {
	
	//Position
	int x;
	int y;
	int priority; //Ordre de priorit� utilis� pour l'affichage (0 = fond, 0>> = baba)
	
	public Item()
	{
		//nothing to do here ?
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
}
