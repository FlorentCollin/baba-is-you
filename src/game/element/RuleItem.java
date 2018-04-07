package game.element;

/**
 * Classe g�rant l'enti�ret� des Items de type "R�gles" (ex : is, text_wall, push, ...)
 *
 */
public abstract class RuleItem extends Item implements IRule {
	
	
	public abstract boolean isVerb();
	public abstract boolean isWord();
	
	
	// Retourne "true" car un objet "r�gle" est toujours poussable et n'est jamais modifier par les r�gles
	@Override
	public boolean isPushable()
	{
		return true;
	}
	// Retourne "false" car un objet "r�gle" ne bloque jamais un joueur et n'est jamais modifier par les r�gles
	@Override
	public boolean isStop()
	{
		return false;
	}
	// Retourne "false" car un objet "r�gle" n'est pas un objet qui permet de r�ussir le niveau en cours
	@Override
	public boolean isWin()
	{
		return false; 
	}
	// Retourne "false" car un objet "r�gle" n'est pas un objet qui peut mourir
	@Override
	public boolean isDeadly()
	{
		return false; 
	}
}
