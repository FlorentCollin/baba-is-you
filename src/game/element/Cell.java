package game.element;
// Note : lors de l'implémentation de la fonction qui change les règles on peut s'arrêter après le dernier objet de type.
// Genre si on veut modifier tous les rochers si le prochaine objet n'est pas un objet ou arrête l'ittération ? 


import java.util.ArrayList;

/**
 * Classe qui contient une liste d'Item et qui permettra d'organiser l'affichage graphique.
 * 
 */
public class Cell {
	//Liste de tous les Item qui sont sur la même cellule
	private ArrayList<Item> list;
	//Position
	private int x;
	private int y;

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Cell(int x, int y)
	{
		this.x = x;
		this.y = y;
		list = new ArrayList<>();
		Background back = new Background();
		list.add(back);
	}
	
	public Cell(int x, int y, Item itemtoAdd)
	{
		this(x, y); // Appel le constructor sans arguments
		list.add(itemtoAdd);		
	}
	
	public ArrayList<Item> getList()
	{
		return list;
	}
	
	public Item getItem(int index)
	{
		return list.get(index);
	}
	
	public boolean add(Item itemToAdd)
	{
		Item itemOfIndex;
		for(int i = 0; i < list.size(); i++) // Place l'item en fonction de sa priorité d'affichage
		{
			itemOfIndex = list.get(i);
//			if(itemToAdd.equals(itemOfIndex))
//			{
//				return false;
//			}
			if (itemOfIndex.getPriority() > itemToAdd.getPriority())
			{
				list.add(i, itemToAdd);;
				return true;
			}
		}
		list.add(itemToAdd);
		return true;
		
		
	}
	
	public Item remove(int index)
	{
		return list.remove(index);
	}
	
	public boolean removeItem(Item itemToRemove)
	{
		return list.remove(itemToRemove);
	}
	
	public boolean isEmpty()
	{
		return list.size()<=1;
	}
	
	public Item lastItem()
	{
		return list.get(list.size()-1);
	}
}
