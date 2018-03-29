package game.element;

/**
 * Classe g�rant l'enti�ret� des Items de type "R�gles" (ex : is, text_wall, push, ...)
 *
 */
public abstract class RuleItem extends Item {
	
	private int orderInRule;
	
	public RuleItem(int x, int y)
	{
		super(x,y);
	}
	
	public int getOrderInRule() {
		return orderInRule;
	}
	public void setOrderInRule(int orderInRule) {
		this.orderInRule = orderInRule;
	}
	// Retourne "true" car un objet "r�gle" est toujours poussable et n'est jamais modifier par les r�gles
	public boolean isPushable()
	{
		return true;
	}
	// Retourne "false" car un objet "r�gle" ne bloque jamais un joueur et n'est jamais modifier par les r�gles	
	public boolean isStop()
	{
		return false;
	}
	// Retourne "false" car un objet "r�gle" n'est pas un objet qui permet de r�ussir le niveau en cours
	public boolean isWin()
	{
		return false; 
	}
}
