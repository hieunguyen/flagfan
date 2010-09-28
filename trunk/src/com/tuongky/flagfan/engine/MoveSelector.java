package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;
import static com.tuongky.flagfan.engine.Evaluator.*;

public class MoveSelector {

	public static final int HASH 			= 1; // Hash Table or IID Move
	public static final int GOOD_CAPTURE 	= 2; // Good Captures (SEE>=0) MVV/LVA
	public static final int KILLER 			= 3; // Killer Moves
	public static final int NON_CAPTURE		= 4; // Non-Capture Moves
	public static final int BAD_CAPTURE		= 5; // Bad Captures (SEE<0) MVV/LVA
	public static final int STOP			= 6; // STOP FLAG
	
	public static final int FULL_SEARCH 	= 0;
	public static final int QUIESCENCE 		= 1;
	public static final int TEST 			= 2;
	
	public static final int[][] PHASE_ORDERS = {
		{HASH, GOOD_CAPTURE, KILLER, NON_CAPTURE, BAD_CAPTURE, STOP},
		{HASH, GOOD_CAPTURE, STOP},
		{HASH, GOOD_CAPTURE, KILLER, NON_CAPTURE, BAD_CAPTURE, STOP}
	};

	int searchStage;
	int phaseIndex, phase;
	int ttMove;
	
	Position p;
	long[][] historyTable;
	int[] killers;
	
	int[] moveList;
	int num;
	
	int[] badCaptures;
	int bc;
	
	long[] scores;
	int[] queue;
	int first, last;
	
	MoveGenerator mg;
	
	public MoveSelector(Position p, int searchStage, int ttMove, long[][] historyTable, int[] killers) {
		this.p = p;
		this.searchStage = searchStage;
		this.ttMove = ttMove;
		this.historyTable = historyTable;
		this.killers = killers;
		
		mg = MoveGenerator.getInstance();
		
		queue = new int[MAX_WIDTH];
		first = 0; last = 0;
		
		moveList = new int[MAX_WIDTH];
		scores = new long[MAX_WIDTH];
		
		badCaptures = new int[MAX_DEPTH];
		bc = 0;
		
		phaseIndex = -1;
		goNextPhase();
	}

	void evaluateCaptures() {
		for (int i=first; i<last; i++) {
			int move = queue[i];
			int attacker, victim;
			attacker = p.board[move>>8&0xff];
			victim = p.board[move&0xff];
			scores[i] = (PIECE_VALUES[victim]<<15)+PIECE_VALUES[attacker]; // MVV-LVA
		}
	}
	
	void evaluateNonCaptures() {
		for (int i=first; i<last; i++) {
			int move = queue[i];
			scores[i] = historyTable[move>>8&0xff][move&0xff];
		}
	}
	
	void goNextPhase() {
		phaseIndex++;
		phase = PHASE_ORDERS[searchStage][phaseIndex];
		switch (phase) {
			case HASH:
				if (ttMove!=NO_MOVE) queue[last++] = ttMove;
				break;
			case GOOD_CAPTURE:
				num = mg.genCaptureMoves(p, moveList);
				for (int i=0; i<num; i++) {
					queue[last++] = moveList[i];
				}
				evaluateCaptures();
				break;
			case KILLER:
				for (int i=0; i<2; i++)
					if (killers[i]!=NO_MOVE && p.legalMove(killers[i])) queue[last++] = killers[i];
				break;
			case NON_CAPTURE:
				num = mg.genNonCaptureMoves(p, moveList);
				for (int i=0; i<num; i++) queue[last++] = moveList[i];
				evaluateNonCaptures();
				break;
			case BAD_CAPTURE:
				for (int i=0; i<bc; i++) queue[last++] = badCaptures[i];
				evaluateCaptures();
				break;
			case STOP:
				break;
		}
	}
	
	void moveBestToTop() {
		long max = Long.MIN_VALUE;
		int index = -1;
		for (int i=first; i<last; i++)
			if (scores[i]>max) {
				max = scores[i];
				index = i;
			}
		if (first<index) exchange(first, index);
	}
	
	void exchange(int i, int j) {
		long tmp;
		tmp = queue[i];
		queue[i] = queue[j];
		queue[j] = (int) tmp;
		
		tmp = scores[i];
		scores[i] = scores[j];
		scores[j] = tmp;
	}
	
	public int nextMove() {
		int move;
		while (phase!=STOP) {
			while (first<last) {
				switch (phase) {
					case HASH:
						return queue[first++];
					case GOOD_CAPTURE:
						moveBestToTop();
						move = queue[first++];
						if (p.goodCapture(move)) return move;
						badCaptures[bc++] = move;
						break;
					case KILLER:
						return queue[first++];
					case NON_CAPTURE:
						moveBestToTop();
						return queue[first++];
					case BAD_CAPTURE:
						moveBestToTop();
						return queue[first++];
					case STOP:
						return NO_MOVE;
				}
			}
			goNextPhase();
		}
		return NO_MOVE;
	}

	public int getPhase() {
		return phase;
	}

	public boolean inReductionPhase() {
		return phase==NON_CAPTURE || phase==BAD_CAPTURE;
	}
	
}
