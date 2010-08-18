package com.tuongky.flagfan.engine;

import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class FlagFanEngine {

	Search search;
	Position p;
	
	public FlagFanEngine(int[] board90, int turn) {
		p = new Position(board90, turn);
		search = new Search(p);		
	}
	
	public void showNextMove() {
		MyTimer timer = new MyTimer();
		System.out.println("Thinking...");
		int move = findNextMove();
		p.printMoveForHuman(move);
		double nps = Evaluator.getInstance().nodeCount/(timer.elapsedTime()*0.001)*1e-6;
		timer.printElapsedTime();
		System.out.println("Node Count = "+Evaluator.getInstance().nodeCount);
		System.out.println("Node Per Second = "+String.valueOf(nps).substring(0, 5)+" millions");
	}
	
	public int findNextMove() {
		return search.findBestMove();
	}
	
	public static void main(String[] args) throws FENException {
		String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
		FEN f = new FEN(fen);
		FlagFanEngine ffe = new FlagFanEngine(f.getBoard90(), f.getTurn());
		ffe.showNextMove();
	}

}
