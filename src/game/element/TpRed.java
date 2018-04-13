package game.element;

/**
 * Objet "TPRED"
 *
 */
public class TpRed extends Item implements IRealItem {

	private Class<?> typeText; // Variable qui sert à comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextRock)
	
	public TpRed(Class<?> typeText)
	{
		this.typeText = typeText;
		setPriority(1);
		setName("TpRed");
		setGraphicsName("TpRed");
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
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Rock)
			return true;
		return false;
	}
	
}