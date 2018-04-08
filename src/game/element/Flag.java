package game.element;

/**
 * Objet "FLAG"
 *
 */
public class Flag extends Item implements IRealItem {

	Class<?> typeText; // Variable qui sert � comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextWall)
	
	public Flag(Class<?> typeText)
	{
		this.typeText = typeText;
		setPriority(1);
		setName("Flag");
		setGraphicsName("flag");
	}
	
	/**
	 * M�thode qui regarde si l'Item correspond � sa comparaison en RuleItem (ex: Rock == TextRock retourne true)
	 * @param wordInRule Mot d'une R�gle
	 */
	@Override
	public boolean isRepresentedBy(IRule wordInRule)
	{
		
		return wordInRule.getClass().equals(typeText);
		
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Flag)
			return true;
		return false;
	}
}