package game.element;

/**
 * Objet "BABA"
 *
 */
public class Baba extends Item implements IRealItem{

	Class typeText; // Variable qui sert � comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextRock)
	
	public Baba(Class typeText)
	{
		this.typeText = typeText;
		setPriority(4);
		setName("baba");
		setGraphicsName("baba");
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
		if(other instanceof Baba)
			return true;
		return false;
	}
	
}