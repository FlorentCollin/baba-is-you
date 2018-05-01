package game.element;
/**
 * Interface qui est utilis� dans RuleItem
 * Cette interface d�finie deux m�thodes bool�enne isVerb() qui retourne true si l'objet RuleItem est un verbe,
 * isWord() qui retourne true si l'objet RuleItem est un nom commun et isAction qui retourne true si l'objet est une Action.
 */
public interface IRule {

	boolean isVerb();
	boolean isWord();
	boolean isAction();
	boolean isEffect();
}
