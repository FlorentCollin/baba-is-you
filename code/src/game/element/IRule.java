package game.element;

/**
 * Interface qui est utilis� dans RuleItem Cette interface d�finie 4 m�thodes
 * bool�enne isVerb() qui retourne true si l'objet RuleItem est un verbe,
 * isWord() qui retourne true si l'objet RuleItem est un nom commun, isAction
 * qui retourne true si l'objet est une Action et isEffect qui retourne true c'est un effet.
 */
public interface IRule {

	boolean isVerb();

	boolean isWord();

	boolean isAction();

	boolean isEffect();
}
