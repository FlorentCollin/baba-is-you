package game.element;

/**
 * Classe gérant l'entièreté des Items de type "Règles" (ex : is, text_wall, push, ...)
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
	// Retourne "true" car un objet "règle" est toujours poussable et n'est jamais modifier par les règles
	public boolean isPushable()
	{
		return true;
	}
	// Retourne "false" car un objet "règle" ne bloque jamais un joueur et n'est jamais modifier par les règles	
	public boolean isStop()
	{
		return false;
	}
	// Retourne "false" car un objet "règle" n'est pas un objet qui permet de réussir le niveau en cours
	public boolean isWin()
	{
		return false; 
	}
}
