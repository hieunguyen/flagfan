package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.TTEntry.*;

import com.tuongky.utils.Misc;
import com.tuongky.utils.MyTimer;

public class Search {

	final static int oo 		= 1001001;
	final static int WIN 		= 10000;
	final static int MAX_DEPTH 	= 10;
	final static int DEEPEST 	= 100;
	final static int MAX_WIDTH 	= 100;
	final static int TIME_LIMIT = 5*60*1000; // 5 mins
	
	Position p;
	TTable tt;
	MoveGenerator mg;
	Evaluator evaluator;
	
	long[][] historyTable;
	int[][] moveLists;
	int bestMove;
	
	MyTimer timer;
	int maxDepth;
	long timeLimit;
	
	public Search(Position p) {
		this.p = p;
		mg = MoveGenerator.getInstance();
		evaluator = Evaluator.getInstance();
		maxDepth = MAX_DEPTH;
		timeLimit = TIME_LIMIT;
		moveLists = new int[DEEPEST][MAX_WIDTH];
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	int quiesce(int ply, int alpha, int beta) {

		int score = evaluator.eval(p);
		if (score>=beta) return score;
		if (score>alpha) alpha = score;
		
		boolean foundPV = false;
		
//		int[] moveList = new int[MAX_WIDTH];
		int[] moveList = moveLists[ply];
		int num = mg.genCaptureMoves(p, moveList);
		
		if (num==0) return alpha;
		
		MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);
		
		for (int i=0; i<num; i++) {
			int move = ms.nextMove();
			if (p.makeMove(move)) {
				if (!foundPV) {
					score = -quiesce(ply+1, -beta, -alpha);
				} else {
					score = -quiesce(ply+1, -alpha-1, -alpha);
					if (alpha<score&&score<beta) { // re-search
						score = -quiesce(ply+1, -beta, -alpha);
					}
				}				
				p.undoMakeMove();
				if (score>alpha) {
					foundPV = true;
					alpha = score;
					if (alpha>=beta) return beta;
				}
			}
		}
		
		return alpha;
	}
	
	int PVS(int ply, int depth, int alpha, int beta) {
		
		if (alpha<-WIN+ply) {
			alpha = -WIN+ply;
			if (alpha>=beta) return beta;
		}
		
		TTEntry ttEntry = tt.retrieve(p.zobristLock);
		
		if (ttEntry!=null&&ttEntry.depth>=depth) {
			switch (ttEntry.flag) {
				case LOWER_BOUND:
					if (ttEntry.score>alpha) alpha = ttEntry.score;
					break;
				case UPPER_BOUND:
					if (ttEntry.score<beta) beta = ttEntry.score;
					break;
				case EXACT_SCORE:
					if (ttEntry.score<alpha) return alpha;
					if (ttEntry.score>beta) return beta;
					return ttEntry.score;
			}
			if (alpha>=beta) return beta;
		}
		
		if (depth<=0) return quiesce(ply, alpha, beta);
		
		int score, bestLocalMove = -1;

		// Null Move
		if (p.isNullMoveOK() && depth>2 && !mg.isChecked(p, p.turn)) {
			p.makeNullMove();
			int nmr = (depth>6) ? 3 : 2;
			score = -PVS(ply+1, depth-1-nmr, -beta, -alpha);
			p.undoMakeNullMove();
			if (score>=beta) return beta;	
			if (score>alpha) { alpha = score; }
		}		
		
		int a = alpha;		
		boolean foundPV = false;
		
		// Transposition Table Move
		if (ttEntry!=null&&mg.legalMove(p, ttEntry.move)) {
			int ttMove = ttEntry.move;
			if (p.makeMove(ttMove)) {
				if (!foundPV) {
					score = -PVS(ply+1, depth-1, -beta, -alpha);
				} else {
					score = -PVS(ply+1, depth-1, -alpha-1, -alpha);
					if (alpha<score&&score<beta) { // re-search
						score = -PVS(ply+1, depth-1, -beta, -alpha);
					}
				}				
				p.undoMakeMove();
				if (score>alpha) {
					foundPV = true;
					alpha = score;
					bestLocalMove = ttMove;
					if (alpha>=beta) alpha = beta;
				}
			}			
		}
		
		if (alpha<beta) {
//			int[] moveList = new int[MAX_WIDTH];
			int[] moveList = moveLists[ply];
			int num = mg.genAllMoves(p, moveList);
			MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);
			
			for (int i=0; i<num; i++) {
				int move = ms.nextMove();
				if (p.makeMove(move)) {
					if (!foundPV) {
						score = -PVS(ply+1, depth-1, -beta, -alpha);
					} else {
						score = -PVS(ply+1, depth-1, -alpha-1, -alpha);
						if (alpha<score&&score<beta) { // re-search
							score = -PVS(ply+1, depth-1, -beta, -alpha);
						}
					}				
					p.undoMakeMove();
					if (score>alpha) {
						foundPV = true;
						alpha = score;
						bestLocalMove = move;
						if (alpha>=beta) {
							alpha = beta;
							break;
						}
					}
				}
			}
		}
		
		if (ply==0&&bestLocalMove!=-1) bestMove = bestLocalMove;
		
		if (bestLocalMove!=-1) historyTable[bestLocalMove>>8&0xff][bestLocalMove&0xff] += 1L<<depth;
		
		byte flag = EXACT_SCORE;
		if (alpha>=beta) flag = LOWER_BOUND; else
			if (alpha<=a) flag = UPPER_BOUND;
		tt.store(p.zobristLock, alpha, bestLocalMove, (byte) depth, flag);
		
		return alpha;
	}
	
	void beforeSearch() {
//		timer = new MyTimer();
		tt = new TTable();
		historyTable = new long[256][256];
	}
	
	void aspirationSearch() {
		bestMove = -1;
		int bestScore = PVS(0, 1, -oo, +oo);
		int delta = 30;
		for (int d=2; d<=maxDepth; d++) {
			int alpha = bestScore - delta;
			int beta = bestScore + delta;
			int score = PVS(0, d, alpha, beta);
			if (score<=alpha||score>=beta) {
				System.out.println("Re-searching...");
				score = PVS(0, d, -oo, +oo);
			}
			bestScore = score;
			Misc.debug(d, bestScore);
			p.printMoveForHuman(bestMove);
		}		
	}
	
	void iterativeDeepning() {
		bestMove = -1;
		int bestScore;
		for (int d=1; d<=maxDepth; d++) {
			bestScore = PVS(0, d, -oo, +oo);
			Misc.debug(d, bestScore);
			p.printMoveForHuman(bestMove);
		}		
		
	}
	
	public int findBestMove() {
		beforeSearch();
//		aspirationSearch();
		iterativeDeepning();
		return bestMove;
	}
	
}
