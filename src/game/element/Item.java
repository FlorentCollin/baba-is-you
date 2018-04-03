package game.element;

/**
 * Classe abstraite servant de base � tous les �l�ments du jeu.
 * 
 */
public abstract class Item {
	
	private int priority; //Ordre de priorit� utilis� pour l'affichage (0 = fond, 0>> = baba)
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
	 * M�thode bool�enne qui indique si un Item est poussable
	 * @return true si l'objet est poussable false sinon
	 * 
	 */
	public boolean isPushable()
	{ 
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextPush && isRepresentedBy(element[0]))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * M�thode bool�enne qui indique si un Item "stop" les autres Items
	 * @return true si l'objet est "stop" false sinon
	 * 
	 */
	public boolean isStop()
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextStop && isRepresentedBy(element[0]))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * M�thode bool�enne qui indique si un Item signifie la victoire
	 * @return true si l'objet est "win" false sinon
	 * 
	 */
	public boolean isWin()
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextWin && isRepresentedBy(element[0]))
			{
				return true;
			}
		}
		return false;
	}
	/**
	 * M�thode qui indique si un Item signifie la mort de cette Item (ie quand l'Item va sur la case il disparrait)
	 * @return true si l'Item est mortelle false sinon
	 */
	public boolean isDeadly()
	{
		for(IRule[] element : Rules.getListOfRulesActives())
		{
			if(element[2] instanceof TextSink && isRepresentedBy(element[0]))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isRepresentedBy(IRule wordInRule)
	{
		return false;
	}
	
	public String toString()
	{
		return getClass().toString();
	}
	
	
}
