package game.element;
/**
 * Interface qui est utilis� dans RuleItem
 * Cette interface d�finie deux m�thodes bool�enne isVerb() qui retourne true si l'objet RuleItem est un verbe
 * et isWord() qui retourne true sur l'objet RuleItem est un nom commun.
 */
public interface IRule {

	boolean isVerb();
	boolean isWord();
}
