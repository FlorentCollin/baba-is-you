package game.element;

/**
 * Objet "BABA"
 *
 */
public class Baba extends Item implements IRealItem{

	Class typeText; // Variable qui sert à comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextWall)
	
	public Baba(Class typeText)
	{
		this.typeText = typeText;
		setPriority(4);
		setName("baba");
	}
	
	/**
	 * Méthode qui regarde si l'Item correspond à sa comparaison en RuleItem (ex: Rock == TextRock retourne true)
	 * @param wordInRule Mot d'une Règle
	 */
	@Override
	public boolean isRepresentedBy(IRule wordInRule)
	{
		
		return wordInRule.getClass().equals(typeText);
		
	}
	
}