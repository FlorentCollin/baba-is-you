package game.boardController;

import java.util.ArrayList;

import game.element.IRule;
import game.element.Item;
import game.element.TpBlue;
import game.element.TpGreen;
import game.element.TpRed;
import game.levelManager.LevelManager;
import game.levelManager.Tuple;
import gui.BabaIsYouApp;
import gui.SoundFX;

/**
 * Classe qui va controller tous les déplacements dans chaque Board
 *
 */
public class MoveController {

	/**
	 * Méthode récursive qui va bouger un item dans une direction
	 * 
	 * @param x1
	 *            position en x de l'item
	 * @param y1
	 *            position en y de l'item
	 * @param z
	 *            position dans la liste de la cellule[y][x] (si z == -1 alors z =
	 *            le dernier item de la liste de la cellule[y][x])
	 * @param direction
	 *            direction dans laquelle l'item sera déplacer (0 == UP, 1 == RIGHT,
	 *            2 == DOWN, 3 == LEFT)
	 * @return true si l'objet à été bougé, false sinon
	 */
	public static boolean move(Board board, int x1, int y1, int z, int direction) {
		// Copie des coordonnées
		int x2 = x1;
		int y2 = y1;
		ArrayList<Board> activesBoard = LevelManager.getActivesBoards();
		Cell cellToMove = board.getBoard()[y1][x1];
		if (cellToMove.isEmpty()) // Si la cellule est vide alors on peut forément bouger tous les éléments
									// précédents
		{
			return true;
		}
		boolean isPlayer = false;
		for (IRule player : board.whoIsPlayer()) { // A améliorer en utilisant une variable de classe pour ne pas devoir
													// le refaire à chaque fois
			if (cellToMove.getLastItem().isRepresentedBy(player))
				isPlayer = true;
		}
		if (z == -1 || z > cellToMove.getList().size() - 1) // On remet l'index de la liste de la Cellule s'il est trop
															// loin au dernier élement
		{
			z = cellToMove.getList().size() - 1;
		}
		switch (direction) {
		case 0:
			y2--;
			break; // UP
		case 1:
			x2++;
			break; // RIGHT
		case 2:
			y2++;
			break; // DOWN
		case 3:
			x2--;
			break; // LEFT
		}
		Cell nextCell = board.getBoard()[y2][x2];
		if (!nextCell.isEmpty()) {
			// On vérifie en premier s'il y a un item dans la prochaine cellule qui est sous la règle "STOP", si c'est
			// le cas on retourne false qui indique qu'on ne peut pas bouger cellToMove
			
			if (nextCell.oneItemIsStop()) {
				return false;
			}
			// Si la prochaine cellule est "SINK" alors on supprime ce l'item qui devait
			// être bougé et le cellule SINK
			else if (nextCell.oneItemIsSink()) {
				// Ajout des cellules qui doivent être repeinte par la partie graphique
				board.addChangedCell(new Tuple(x1, y1, 0));
				board.addChangedCell(new Tuple(x2, y2, 0));
				cellToMove.remove(z); // On "tue" la cellule à bouger
				nextCell.removeItem(nextCell.getLastItem()); // On retire la cellule qui "tue"
				return true;
			}
			// Si la prochaine cellule est "Kill" et que c'est un joueur qui va dessus alors
			// on supprime le joueur car il est "mort"
			else if (nextCell.oneItemIsKill(isPlayer)) {
				// Ajout des cellules qui doivent être repeinte par la partie graphique
				board.addChangedCell(new Tuple(x1, y1, 0));
				cellToMove.remove(z);
				return true;
			}
			// PARTIE RECURSIVE !
			// Si le prochain item est sous la règle "PUSH" alors on applique la méthode
			// "move" au prochain item
			else if (nextCell.getLastItem().isPushable()) {
				if (move(board, x2, y2, -1, direction)) { // Comme "move" est une fonction booléenne si l'item suivant à
															// été bouger alors cellToMove peut aussi bouger
					board.changeItemCell(x1, y1, x2, y2, z);
					return true;
				}
			}
			// Si la prochaine case n'est pas vide mais qu'aucune des règles qui modifient
			// "move" ne sont actives dessus alors on peut bouger cellToMove
			else {
				/*
				 * ICI on doit vérifier si la prochaine cellule est un téléporteur actif Si
				 * c'est le cas alors on retire l'item de cellToMove dans le board et on ajoute
				 * dans le board suivant ou précédent l'item que l'on vient de retirer (en
				 * fonction de si c'est un TP bleue ou un TP rouge)
				 */
				Item itemToAdd;
				for (Item element : nextCell.getList()) {
					if (element instanceof TpGreen && board.isAnActiveTp(element)) {
						board.addChangedCell(board.getCorrespondingTp()[0]);
						board.addChangedCell(board.getCorrespondingTp()[1]);
						board.addChangedCell(new Tuple(x1, y1, 0));
						itemToAdd = cellToMove.remove(z);
						Tuple[] correspondingTp = board.getCorrespondingTp();
						if(correspondingTp[0].getX()==x2 && correspondingTp[0].getY()==y2) {
							nextCell = board.getCell(correspondingTp[1].getX(), correspondingTp[1].getY());
							nextCell.add(itemToAdd);
						}
						else if(correspondingTp[1].getX()==x2 && correspondingTp[1].getY()==y2) {
							nextCell = board.getCell(correspondingTp[0].getX(), correspondingTp[0].getY());
							nextCell.add(itemToAdd);
						}	
						SoundFX.play("tpUsed.wav");
						return true;
					}
					if (element instanceof TpBlue && board.getDepthOfLevel() + 1 < activesBoard.size()
							&& board.isAnActiveTp(element)) {
						board.addChangedCell(new Tuple(x1, y1, 0));
						board.addChangedCell(new Tuple(x2, y2, 0));
						itemToAdd = cellToMove.remove(z);
						activesBoard.get(board.getDepthOfLevel() + 1).getCell(x2, y2).add(itemToAdd);
						SoundFX.play("tpUsed.wav");
						if(BabaIsYouApp.success.unlock("DiscoverParallelWorld"))
							BabaIsYouApp.showSuccessUnlocked();
						return true;
					}
					if (element instanceof TpRed && board.getDepthOfLevel() - 1 >= 0 && board.isAnActiveTp(element)) {
						board.addChangedCell(new Tuple(x1, y1, 0));
						board.addChangedCell(new Tuple(x2, y2, 0));
						itemToAdd = cellToMove.remove(z);
						activesBoard.get(board.getDepthOfLevel() - 1).getCell(x2, y2).add(itemToAdd);
						SoundFX.play("tpUsed.wav");
						if(BabaIsYouApp.success.unlock("DiscoverParallelWorld"))
							BabaIsYouApp.showSuccessUnlocked();
						return true;
					}
				}
				board.changeItemCell(x1, y1, x2, y2, z);
				return true;
			}
		}
		// Si la prochaine case est vide alors on peut forcément bouger cellToMove
		else {
			board.changeItemCell(x1, y1, x2, y2, z);
			return true;
		}
		// Comme l'entièreté des "return" sont dans des if else on retourne false pour
		// être sûr d'avoir une valeur de retour
		return false;
	}
}
