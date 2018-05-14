package game.element;

/**
 * Classe gérant l'entièreté des Items de type "Règles" (ex : is, text_wall,
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

	// Retourne "true" car un objet "règle" est toujours poussable et n'est jamais
	// modifier par les règles
	@Override
	public boolean isPushable() {
		return true;
	}

	// Retourne "false" car un objet "règle" ne bloque jamais un joueur et n'est
	// jamais modifier par les règles
	@Override
	public boolean isStop() {
		return false;
	}

	// Retourne "false" car un objet "règle" n'est pas un objet qui peut pas faire
	// mourir un autre Item
	@Override
	public boolean isSink() {
		return false;
	}
}
