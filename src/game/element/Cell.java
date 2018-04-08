package game.element;

import java.util.ArrayList;

/**
 * Classe représentant une cellule de la map et qui contient une liste d'Item ce qui permettra d'avoir plusieurs Items superposés
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
//		Background back = new Background();
//		list.add(back);
	}
	
	public Cell(int x, int y, Item itemtoAdd)
	{
		this(x, y); // Appel le constructeur sans arguments
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
		return list.size()<=0;
	}
	
	public Item lastItem()
	{
		return list.get(list.size()-1);
	}
}
