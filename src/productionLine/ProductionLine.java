/*Zack Khalidov
 * 03/25/2020
 * 
 * Home coding assignment from OptTek.
 * 
 * This program manages a 10 product production line, 
 * where every product has an independent time to produce. 
 * There is also an asymmetric changeover time required to 
 * switch between products. The program will run through
 * the sequence and make one improvement swap to production
 * line until no improving swaps are available.
 *  
 * Given starting sequence (BGEACIJDHF) 
 */
package productionLine;

import java.util.Arrays;

public class ProductionLine {
	
	public final static String BEGIN_SEQ = "BGEACIJDHF";
	public final static int BEGIN_COST = 193;
	
	public static String currentSeq;
	public static int currentCost;
	public static int currentState;
	public static int changedState;
	
	public static int lowSwapPos1;
	public static int lowSwapPos2;
	public static char lowSwapProd1;
	public static char lowSwapProd2;
	
	public static boolean swapsAvailable;
	
	public static int[][] changeCost = {{0,9,13,6,7,8,3,17,7,3},
										{3,0,8,7,9,4,3,16,3,8},
										{5,0,0,8,7,9,4,3,16,5},
										{7,2,5,0,4,2,5,7,2,5},
										{4,3,4,7,0,4,3,4,7,3},
										{2,14,7,7,5,0,1,4,6,4},
										{8,7,9,4,16,3,0,4,9,8},
										{11,5,2,3,2,6,4,0,9,7},
										{4,2,12,9,1,5,5,6,0,9},
										{9,3,6,7,8,3,17,7,3,0}};
	
	// This method returns the Alpha product position in changeCost[][] (A = 0, G = 6)
	public static int productTranslator(char productChar) {
		int productInt = 0;
		
		switch (productChar) {
		case 'A':
			productInt = 0;
			break;
		case 'B':
			productInt = 1;
			break;
		case 'C':
			productInt = 2;
			break;
		case 'D':
			productInt = 3;
			break;
		case 'E':
			productInt = 4;
			break;
		case 'F':
			productInt = 5;
			break;
		case 'G':
			productInt = 6;
			break;
		case 'H':
			productInt = 7;
			break;
		case 'I':
			productInt = 8;
			break;
		case 'J':
			productInt = 9;
			break;
		}
		return productInt;
	}
	
	// Returns the change cost based on the two products being changed.
	// The row index changing from, column index changing to.
	public static int changeOverCost(int rowIndex, int colIndex) {
		int cost;
		cost = changeCost[rowIndex][colIndex];
		return cost;
	}
	
	// Given a pair the method evaluates if swapping results in improvement.
	public static boolean swapEvaluator(char neighbor1, char neighbor2) {
		int product1Pos = productTranslator(neighbor1);
		int product2Pos = productTranslator(neighbor2); 
		
		currentState = changeOverCost(product1Pos, product2Pos);
		changedState = changeOverCost(product2Pos, product1Pos);
		
		if (currentState > changedState) {
			return true;
		} else {
			return false;
		}
	}
	
	// Swaps the product Alpha's given two positions in the sequence.
	public static void productSwap(char neighbor1, char neighbor2, int pos1, int pos2) {
		char[] currentSeqArray = currentSeq.toCharArray();
		currentSeqArray[pos1] = neighbor2;
		currentSeqArray[pos2] = neighbor1;
		currentSeq = String.copyValueOf(currentSeqArray);
	}
	
	
	public static int totalTimeUnits() {
		int totalTime = 112; // Time units to produce products don't change.
		
		// Runs one loop through sequence and adds the product swaps to total time units.
		for (int i = 0; i < 9; i++ ) {
			char product1 = currentSeq.charAt(i);
			char product2 = currentSeq.charAt(i+1);
			int productInt1 = productTranslator(product1);
			int productInt2 = productTranslator(product2);
			totalTime += changeOverCost(productInt1, productInt2);
		}
		
		return totalTime;
	}
	
	public static void main(String[] args) {
		System.out.println("Starting sequence BGEACIJDHF");
		
		currentSeq = BEGIN_SEQ;
		currentCost = BEGIN_COST;
		
		do {
			int lowestSwapCost = 20;
			swapsAvailable = false; // Will be false when no possible improvements are possible.
			
			for (int i = 0; i < 9; i++ ) {
				
				char product1 = currentSeq.charAt(i);
				char product2 = currentSeq.charAt(i+1);
				
				if (swapEvaluator(product1, product2) == true) {
					int swapCost = changedState;
					swapsAvailable = true; // If at least one swap is possible do while will run again.
					
					// Now swaps the first lowest case.
					// Constraint can be <= if equal swap costs are in same sequence.
					if (swapCost < lowestSwapCost) {
						lowestSwapCost = swapCost;
						lowSwapProd1 = product1;
						lowSwapProd2 = product2;
						lowSwapPos1 = i;
						lowSwapPos2 = i+1;
					}	 
				}
			}
			
			productSwap(lowSwapProd1, lowSwapProd2, lowSwapPos1, lowSwapPos2);
			currentCost = totalTimeUnits();
			
		// This way a final run through sequence is made to check for possible swaps. 
		} while (swapsAvailable);
		
		System.out.println("Final sequence: " + currentSeq);
		System.out.println("Final time unit cost: " + currentCost);
	}
}
