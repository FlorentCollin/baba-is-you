package game.boardController;

import java.util.ArrayList;

import game.levelManager.LevelManager;

public class AllBoards {

	private ArrayList<Board> boards;
	private String[] listOfLevels;
	private int depthIndex = 0;
	private int levelIndex;
	
	public AllBoards(ArrayList<Board> boards) {
		this.boards = boards;
		levelIndex = boards.get(0).getLevelNumber();
		listOfLevels = LevelManager.getListOfLevels();
	}
	
	public Board getBoard(int i) {
		return boards.get(i);
	}
	
	public Board getNextBoard() {
		if(levelIndex<listOfLevels.length-1 && levelIndex != -1) {
			levelIndex++;
			LevelManager.readLevel(listOfLevels[levelIndex], true);
			return LevelManager.getActivesBoards().get(0);
		}
		return boards.get(depthIndex);
	}
}
