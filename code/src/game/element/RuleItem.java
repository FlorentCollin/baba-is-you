package game.element;

/**
 * Classe g�rant l'enti�ret� des Items de type "R�gles" (ex : is, text_wall,
 * push, ...)
 *
 */
public abstract class RuleItem extends Item implements IRule {

	public boolean isVerb() {
		return false;
	}

	public boolean isWord() {
		return false;
	}

	public boolean isAction() {
		return false;
	}

	public boolean isEffect() {
		return false;
	}

	public RuleItem() {
		setPriority(3);
		setName(this.getClass().getSimpleName());
	}

	// Retourne "true" car un objet "r�gle" est toujours poussable et n'est jamais
	// modifier par les r�gles
	@Override
	public boolean isPushable() {
		return true;
	}

	// Retourne "false" car un objet "r�gle" ne bloque jamais un joueur et n'est
	// jamais modifier par les r�gles
	@Override
	public boolean isStop() {
		return false;
	}

	// Retourne "false" car un objet "r�gle" n'est pas un objet qui peut pas faire
	// mourir un autre Item
	@Override
	public boolean isSink() {
		return false;
	}
}
