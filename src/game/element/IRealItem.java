package game.element;

/**
 * Interface qui va gérer les "Vrais" Item (par exemple les Walls, Rocks, Flags, etc,...)
 *  
 */
public interface IRealItem {
	
	boolean isRepresentedBy(IRule wordInRule);
}
