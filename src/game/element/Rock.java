package game.element;

/**
 * Objet "ROCK"
 *
 */
public class Rock extends Item implements IRealItem {

	private Class<?> typeText; // Variable qui sert � comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextRock)
	
	public Rock(Class<?> typeText)
	{
		this.typeText = typeText;
		setPriority(1);
		setName("Rock");
		setGraphicsName("rock");
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
		if(other instanceof Rock)
			return true;
		return false;
	}
	
}