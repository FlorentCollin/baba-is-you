package game.boardController;

import java.util.ArrayList;

import game.element.Item;

/**
 * Classe représentant une cellule de la map et qui contient une liste d'Item ce
 * qui permettra d'avoir plusieurs Items superposés
 * 
 */
public class Cell {
	// Liste de tous les Item qui sont sur la même cellule
	private ArrayList<Item> list;

	public Cell() {
		list = new ArrayList<>();
	}

	public Cell(Item itemtoAdd) {
		this(); // Appel le constructeur sans arguments
		list.add(itemtoAdd);
	}

	public ArrayList<Item> getList() {
		return list;
	}

	public Item getItem(int index) {
		return list.get(index);
	}
	/**
	 * Retourne le dernier item de la liste
	 * @return le dernier item
	 */
	public Item getLastItem() {
		return list.get(list.size() - 1);
	}

	/**
	 * Ajoute un item dans la liste
	 * @param itemToAdd l'item a ajouter
	 * @return true si l'item a été ajouté, false sinon
	 */
	public boolean add(Item itemToAdd) {
		Item itemOfIndex;
		for (int i = 0; i < list.size(); i++) // Place l'item en fonction de sa priorité d'affichage
		{
			itemOfIndex = list.get(i);
			if (itemOfIndex.getPriority() > itemToAdd.getPriority()) {
				list.add(i, itemToAdd);
				;
				return true;
			}
		}
		list.add(itemToAdd);
		return true;

	}

	public Item remove(int index) {
		return list.remove(index);
	}

	public boolean removeItem(Item itemToRemove) {
		return list.remove(itemToRemove);
	}

	public boolean isEmpty() {
		return list.size() <= 0;
	}

	/**
	 * Regarde dans la liste si un item "isSink"
	 * @return true s'il existe un item, false sinon
	 */
	public boolean oneItemIsSink() {
		for (Item item : list) {
			if (item.isSink())
				return true;
		}
		return false;
	}

	/**
	 * Regarde dans la liste si un item "isKill"
	 * @param isPlayer Utilisé dans item.isKill(isPlayer)
	 * @return vrai si un item "isKill", false sinon
	 */
	public boolean oneItemIsKill(boolean isPlayer) {
		for (Item item : list) {
			if (item.isKill(isPlayer))
				return true;
		}
		return false;
	}
	
	/**
	 * Regarde dans la liste si un item "isStop"
	 * @return vrai si un item "isStop", false sinon
	 */
	public boolean oneItemIsStop() {
		for (Item item : list) {
			if (item.isStop())
				return true;
		}
		return false;
	}
}
