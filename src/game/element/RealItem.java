package game.element;

/**
 * Interface qui va g�rer les "Vrais" Item (par exemple les Walls, Rocks, Flags, etc,...)
 *  
 */
public abstract class RealItem extends Item {
	
	private Class<?> typeText; // Variable qui sert � comparer un Item avec sa comparaison en RuleItem (ex: Rock --> TextRock)
	
	public RealItem(Class<?> typeText) {
		setTypeText(typeText);
		setPriority(1);
		setName(this.getClass().getSimpleName());
	}
	
	public void setTypeText(Class<?> typeText) {
		this.typeText = typeText;
	}


	/**
	 * M�thode qui regarde si l'Item correspond � sa comparaison en RuleItem (ex: Rock == TextRock retourne true)
	 * @param wordInRule Mot d'une R�gle
	 */
	public boolean isRepresentedBy(IRule wordInRule)
	{
		
		return wordInRule.getClass().equals(typeText);
		
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other.getClass() == this.getClass())
			return true;
		return false;
	}
}
