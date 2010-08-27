package com.tuongky.flagfan.engine;

import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class FlagFanEngine extends XBoardBase {

	Search search;
	Position p;
	
	long maxTime;
	long timeLeft;
	long timeInc;
	int movesLeft;
	int maxMoves;
	int maxDepth;
	
	public FlagFanEngine() {
		maxTime = 10*1000; // 10s
		maxDepth = 50;
	}
	
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

	@Override
	public void init() {
		FEN f;
		try {
			f = new FEN(FEN.START_FEN);
			p = new Position(f.getBoard90(), f.getTurn());
			search = new Search(p);		
		} catch (FENException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void setTime(int time) {
		timeLeft = maxTime = time * 1000;
		timeInc = 0;
		movesLeft = maxMoves = 1;
	}

	@Override
	public void setDepth(int depth) {
		maxDepth = depth;
	}

	@Override
	public void level(int mps, int base, int inc) {
		movesLeft = mps;
		timeLeft = maxTime = base * 1000;
		timeInc = inc * 1000; 
	}

	@Override
	public void time(int n) {
		timeLeft = n*10;
	}

	@Override
	public void otim(int n) {
		// do nohting
	}

	public void protover() {
//		feature("myname", "FlagFan 1.0");
	}
	
}
