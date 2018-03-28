package game.element;
// Note : lors de l'impl�mentation de la fonction qui change les r�gles on peut s'arr�ter apr�s le dernier objet de type.
// Genre si on veut modifier tous les rochers si le prochaine objet n'est pas un objet ou arr�te l'itt�ration ? 


import java.util.ArrayList;

/**
 * Classe qui contient une liste d'Item et qui permettra d'organiser l'affichage graphique.
 * 
 */
public class Cell {

	private ArrayList<Item> list;

	public Cell()
	{
		list = new ArrayList<>();
		Background back = new Background();
		list.add(back);
	}
	
	public Cell(Item itemtoAdd)
	{
		this(); // Appel le constructor sans arguments
		list.add(itemtoAdd);
		
		
	}
	
	public Item getItem(int index)
	{
		return list.get(index);
	}
	
	public void changeCell(Item element)
	{
		//TODO
	}
	public void add(Item itemToAdd)
	{
		for(int i = 0; i < list.size(); i++) // Place l'item en fonction de sa priorit� d'affichage
		{
			if(list.get(i).getPriority() > itemToAdd.getPriority())
				list.set(i, itemToAdd);
		}
	}
	
	public void remove(Item element)
	{
		
	}
}
