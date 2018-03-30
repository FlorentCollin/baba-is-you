package game.element;
/**
 * Interface qui est utilisé dans RuleItem
 * Cette interface définie deux méthodes booléenne isVerb() qui retourne true si l'objet RuleItem est un verbe
 * et isWord() qui retourne true sur l'objet RuleItem est un nom commun.
 */
public interface IRule {

	boolean isVerb();
	boolean isWord();
}
