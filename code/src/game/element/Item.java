package game.element;

import java.util.ArrayList;

import game.boardController.Rules;

/**
 * Classe abstraite servant de base à tous les éléments du jeu.
 * 
 */
public abstract class Item {

	private int priority; // Ordre de priorité utilisé pour l'affichage (0 = fond, 0>> = baba)
	private String name;
	protected ArrayList<Item> effects = new ArrayList<>();

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Méthode booléenne qui indique si un Item est poussable
	 * 
	 * @return true si l'objet est poussable false sinon
	 * 
	 */
	public boolean isPushable() {
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextPush && isRepresentedBy(element[0])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Méthode booléenne qui indique si un Item "stop" les autres Items
	 * 
	 * @return true si l'objet est "stop" false sinon
	 * 
	 */
	public boolean isStop() {
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextStop && isRepresentedBy(element[0])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Méthode qui indique si un Item signifie la "noyade" de cette Item (ie quand
	 * l'Item va sur la case il disparrait)
	 * 
	 * @return true si l'Item est mortelle false sinon
	 */
	public boolean isSink() {
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (element[2] instanceof TextSink && isRepresentedBy(element[0])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Méthode qui indique si un Item signifie la "mort" de cette Item (ie quand
	 * l'Item va sur la case il disparrait) A la différence de isSink() seuls les
	 * joueurs peuvent mourrir
	 * 
	 * @return true si l'Item est mortelle false sinon
	 */
	public boolean isKill(boolean isPlayer) {
		for (IRule[] element : Rules.getListOfRulesActives()) {
			if (isPlayer && element[2] instanceof TextKill && isRepresentedBy(element[0])) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Item> getEffects() {
		return effects;
	}

	public boolean isRepresentedBy(IRule wordInRule) {
		return false;
	}

	public String toString() {
		return getClass().toString();
	}
}
