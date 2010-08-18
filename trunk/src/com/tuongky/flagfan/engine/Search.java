package com.tuongky.flagfan.engine;

public class Search {

	final static int oo 		= 1001001;
	final static int MAX_DEPTH 	= 6;
	final static int DEEPEST 	= 100;
	final static int MAX_WIDTH 	= 100;
	
	Position p;
	MoveGenerator mg;
	Evaluator evaluator;
	int bestMove;
	int[][] moveLists;
	
	public Search(Position p) {
		this.p = p;
		mg = MoveGenerator.getInstance();
		evaluator = Evaluator.getInstance();
		moveLists = new int[DEEPEST][MAX_WIDTH];
	}
	
	int alphaBeta(int ply, int depth, int alpha, int beta) {
		if (ply>=depth) return evaluator.eval(p);
		int score, value, bestLocalMove;
		score = alpha;
		bestLocalMove = -1;
		int[] moveList = moveLists[ply];
		int num = mg.genAllMoves(p, moveList);
		for (int i=0; i<num; i++) {
			if (p.makeMove(moveList[i])) {
				value = -alphaBeta(ply+1, depth, -beta, -score);
				p.undoMakeMove();
				if (value>score) {
					bestLocalMove = moveList[i];
					score = value;
					if (score>=beta) break;
				}
			}
		}
		if (ply==0) bestMove = bestLocalMove;
		return score;
	}
	
	public int findBestMove() {
		bestMove = -1;
		int bestScore = alphaBeta(0, MAX_DEPTH, -oo, +oo);
		System.out.println("bestScore = "+bestScore);
		return bestMove;
	}
	
}
