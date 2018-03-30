package game.element;

/**
 * Objet "WALL"
 *
 */
public class Wall extends Item implements IRealItem {

	Class typeText; // Variable qui sert à comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextWall)
	
	public Wall(Class typeText)
	{
		this.typeText = typeText;
		setPriority(1);
		setName("wall");
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
