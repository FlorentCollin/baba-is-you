package game.element;

/**
 * Objet repr�sentant la bordure (limite) de la map
 *
 */
public class Boundary extends Item {

	public Boundary() {
		setPriority(0);
		setName("Boundary");
	}

	public boolean isPushable() {
		return false;
	}

	public boolean isStop() {
		return true; // Les bordures stop toujours les Item pour les emp�cher de sortir de la map
	}
}
