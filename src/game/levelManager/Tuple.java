package game.levelManager;

/**
 * Classe qui réprésente un Triplet d'entier Sert principalement dans la classe
 * Board pour enregistrer les positions de certains Items.
 */
public class Tuple {

	private int x;
	private int y;
	private int z;

	public Tuple(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z); // le Z correspond à la position de l'item dans la liste d'une cellule
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}
}
