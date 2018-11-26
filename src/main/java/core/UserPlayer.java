package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UserPlayer extends Player{
	
	PlayerStrategy<? super UserPlayer> strategy;
	
	public boolean playedTilesOnTurn = false;

	public UserPlayer() {
		super();
	}
	
	public UserPlayer(String name, Model model, PlayerStrategy<? super UserPlayer> strategy) {
		super(name, model);
		this.strategy = strategy;
	}
	
	public void playTurn() {
		strategy.executeStrategy(this);
		meldsInHand.clear();
	}
	
	public ArrayList<ArrayList<Tile>> findRuns(){
		
		// finds runs in player hand
		// note that if 12345 is in the hand, findSets() will only return 12345
		// it will not return 123, 234, 345, 2345, 1234

		ArrayList<ArrayList<Tile>> runs = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> matchedTiles = new ArrayList<Tile>();
		char initialColour;
		for (Tile t: this.hand.getTiles()) {
			if (matchedTiles.contains(t)) continue; //if we've found a run using this tile, don't use it to find more runs
		
			ArrayList<Tile> possibleRunList = new ArrayList<Tile>();
			
			possibleRunList.add(t);
			initialColour = t.getColour();
		
			int currentNum = t.getRank();
			for (Tile possibleTile: this.hand.getTiles()) {
				if (possibleTile.getColour() == initialColour) {
					if (possibleTile.getRank() == currentNum+1) {
						possibleRunList.add(possibleTile);
						currentNum = possibleTile.getRank();
					}
					else if (possibleTile.getRank() == currentNum) {
						continue;
					}
				}
			}
			
			if (possibleRunList.size() >= 3) {
				runs.add(possibleRunList); 
				for (Tile matched : possibleRunList) matchedTiles.add(matched);
			}
		}
		return runs;
	}
	
	public ArrayList<ArrayList<Tile>> findSets(){
		
		// finds sets in player hand
		// note that if 4444 is in the hand, findSets() will only return 4444
		// it will not return 444 444 444
		
		ArrayList<ArrayList<Tile>> sets = new ArrayList<ArrayList<Tile>>();
		ArrayList<Tile> matchedTiles = new ArrayList<Tile>();
		
		for (Tile t: this.hand.getTiles()) {
			if (matchedTiles.contains(t)) continue; // if we've found a set using this tile, don't use it to find more sets
			ArrayList<Tile> possibleSetList = new ArrayList<Tile>();
			
			possibleSetList.add(t);
			ArrayList<Character> prevColours = new ArrayList<Character>(); 
			prevColours.add(t.getColour());
			for (Tile possibleTile: this.hand.getTiles()) {
				if (possibleTile.getRank() == t.getRank()) {
					if (!prevColours.contains(possibleTile.getColour())) {
						possibleSetList.add(possibleTile);
						prevColours.add(possibleTile.getColour());
					}
				}
			}
		
			if (possibleSetList.size() >= 3) {
				sets.add(possibleSetList);
				for (Tile matched: possibleSetList) matchedTiles.add(matched);
			}
		}
		return sets;
	}
	
	public Meld createMeld(ArrayList<Tile> selectedTiles, Hand availableTiles) {
		
		if(meldValidatorService.isValidMeld(selectedTiles)) {
			Meld meld = new Meld();
			
			for(int i = 0; i < selectedTiles.size();i++) {
				meld.add(selectedTiles.get(i));
				availableTiles.remove(selectedTiles.get(i));
			}
			return meld;
		}

		return null;
	}
	
	public ArrayList<Tile> selectTiles(ArrayList<Integer> tileIndices, Hand availableTiles){
		ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
		
		for(int i = 0; i < tileIndices.size(); i++) {
			selectedTiles.add(availableTiles.getTile(tileIndices.get(i)));
			
		}
		
		return selectedTiles;
	}
	
	public void tileSelectionInput(Scanner reader, Hand availableTiles) {
		int tileSelected;
		
		ArrayList<Integer> tileIndices = new ArrayList<Integer>();
		
		//Select tiles to create meld
		while(true) {
			System.out.println("Enter the index of the Tile you want to select. (-1) to stop selecting");
			while(!reader.hasNextInt()) {
				System.out.println("Wrong input. Please input again.");
				System.out.println("Enter the index of the Tile you want to select. (-1) to stop selecting");
				reader.nextLine();
			}
			tileSelected = reader.nextInt();
			
			if(tileSelected >= availableTiles.size() || tileSelected < -1) {//Invalid Tile
				System.out.println("Invalid tile, try again.");
			}
			else if(tileSelected == -1) {//Finished selecting
				Meld meld = createMeld(selectTiles(tileIndices, availableTiles), availableTiles);
				if(meld != null) {
					meldsInHand.add(meld);
				}else {
					System.out.println("Invalid Meld");
				}
				
				tileIndices.clear();
				break;
			}
			else {//Select tile
				tileIndices.add(tileSelected);
			}
		}
	}

}
