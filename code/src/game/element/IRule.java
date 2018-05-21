package game.element;

/**
 * Interface qui est utilisé dans RuleItem Cette interface définie 4 méthodes
 * booléenne isVerb() qui retourne true si l'objet RuleItem est un verbe,
 * isWord() qui retourne true si l'objet RuleItem est un nom commun, isAction
 * qui retourne true si l'objet est une Action et isEffect qui retourne true c'est un effet.
 */
public interface IRule {

	boolean isVerb();

	boolean isWord();

	boolean isAction();

	boolean isEffect();
}
