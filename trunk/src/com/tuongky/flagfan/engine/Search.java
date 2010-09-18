package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;

import com.tuongky.utils.Misc;
import com.tuongky.utils.MyTimer;

public class Search {

	Position p;
	MoveGenerator mg;
	Evaluator evaluator;
	TTable tt;
	
	long[][] historyTable;
	int[][] moveLists;
	int bestMove;
	
	int[][] pv;
	
	MyTimer timer;
	int maxDepth;
	long timeLimit;
	
	long qTime, gmTime;
	
	public long nodeCount;
	
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
		if (timeLimit<0) timeLimit = TIME_UNLIMITED;
		this.timeLimit = timeLimit;
	}
	
	int quiesce(int ply, int alpha, int beta) {
		
		if (timer.expired(timeLimit)) return alpha; // fail low if pass time limit
		
		if (p.isChecked()) return PVS(ply+1, 1, alpha, beta, PV_NODE);
		
		int score = evaluator.eval(p);
		if (score>=beta) return score;
		if (score>alpha) alpha = score;
		
		boolean foundPV = false;
		
		int[] moveList = moveLists[ply];
		MyTimer gmTimer = new MyTimer();
		int num = mg.genCaptureMoves(p, moveList);
		gmTime += gmTimer.elapsedTime();
		
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
//					updatePV(ply, move);					
					foundPV = true;
					alpha = score;
					if (alpha>=beta) return beta;
				}
			}
		}
		
		return alpha;
	}
	
	int PVS(int ply, int depth, int alpha, int beta, boolean pvNode) {
		
		if (timer.expired(timeLimit)) return alpha; // fail low if pass time limit
		
		nodeCount++;
		
		if (alpha<-WIN_VALUE+ply) {
			alpha = -WIN_VALUE+ply;
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
		
		// check board repetition rules
		int repValue = p.boardRepeated();
		
		if (repValue==1||repValue==7) {
			if (DRAW_VALUE<=alpha) return alpha;
			if (beta<=DRAW_VALUE) return beta;
			return DRAW_VALUE;
		}
		if (repValue==3) return Math.max(-WIN_VALUE+ply, alpha);
		if (repValue==5) return Math.min(WIN_VALUE-ply, beta);
		
		if (depth<=0) {
			MyTimer timer = new MyTimer();
			int tmp = quiesce(ply, alpha, beta);
			qTime += timer.elapsedTime();
			return tmp;
		}
		
		int score, bestLocalMove = -1;

		// Null Move
		if ((ttEntry==null||ttEntry.flag!=EXACT_SCORE) && 
				p.isNullMoveOK() && depth>2 && !p.isChecked()) {
			p.makeNullMove();
			int nmr = (depth>6) ? 3 : 2;
			score = -PVS(ply+1, depth-1-nmr, -beta, -alpha, PV_NODE);
			p.undoMakeNullMove();
			if (score>=beta) return beta;	
			if (score>alpha) { alpha = score; }
		}		
		
		int a = alpha;		
		boolean foundPV = false;
		
		int ttMove = -1, iidMove = -1;
		
		// Transposition Table Move
		if (ttEntry!=null&&p.legalMove(ttEntry.move)) {
			ttMove = ttEntry.move;
			if (p.makeMove(ttMove)) {
				if (!foundPV) {
					score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
				} else {
					score = -PVS(ply+1, depth-1, -alpha-1, -alpha, NON_PV);
					if (alpha<score&&score<beta) { // re-search
						score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
					}
				}				
				p.undoMakeMove();
				if (score>alpha) {
					foundPV = true;
					alpha = score;
					bestLocalMove = ttMove;
					if (alpha>=beta) alpha = beta; 
					else updatePV(ply, ttMove);
				}
			}			
		}
		
		// IID - Internal Iterative Deepening
		if (alpha<beta && depth>=3 && ttMove==-1) {
			score = PVS(ply, depth-2, alpha, beta, PV_NODE);
			TTEntry entry = tt.retrieve(p.zobristLock);			
			if (entry!=null && p.legalMove(entry.move)) {
				iidMove = entry.move;
				if (p.makeMove(iidMove)) {
					if (!foundPV) {
						score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
					} else {
						score = -PVS(ply+1, depth-1, -alpha-1, -alpha, NON_PV);
						if (alpha<score&&score<beta) { // re-search
							score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
						}
					}				
					p.undoMakeMove();
					if (score>alpha) {
						foundPV = true;
						alpha = score;
						bestLocalMove = iidMove;
						if (alpha>=beta) alpha = beta; 
						else updatePV(ply, iidMove);
					}
				}				
			}
		}
		
		if (alpha<beta) {
			int[] moveList = moveLists[ply];
			
			MyTimer gmTimer = new MyTimer();
			int num = mg.genAllMoves(p, moveList);
			gmTime += gmTimer.elapsedTime();
			
			MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);
			
			for (int i=0; i<num; i++) {
				int move = ms.nextMove();
				if (move==ttMove||move==iidMove) continue;

//				if (ply==0) Misc.printMoveForHuman(p, move);

				if (p.makeMove(move)) {
					
					if (!foundPV) {
						score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
					} else {
						if (i>=3 && !pvNode & depth>=2) { // ok to reduce
							score = -PVS(ply+1, depth-2, -alpha-1, -alpha, NON_PV); // LMR
						} else score = alpha+1;
						if (alpha<score) { // re-search
							score = -PVS(ply+1, depth-1, -alpha-1, -alpha, NON_PV);
							if (alpha<score && score<beta) score = -PVS(ply+1, depth-1, -beta, -alpha, PV_NODE);
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
						updatePV(ply, move);
					}
				}
			}
		}
		
		if (ply==0&&bestLocalMove!=-1) bestMove = bestLocalMove;
		
		
		byte flag = EXACT_SCORE;
		if (alpha>=beta) flag = LOWER_BOUND; else
			if (alpha<=a) flag = UPPER_BOUND;
		tt.store(p.zobristLock, alpha, bestLocalMove, (byte) depth, flag);
		
		if (bestLocalMove!=-1) {
			historyTable[bestLocalMove>>8&0xff][bestLocalMove&0xff] += 1L<<depth;
		}
		
		return alpha;
	}
	
	void updatePV(int ply, int move) {
		pv[ply][ply] = move;
		int i;
		for (i=ply+1; pv[ply+1][i]>0; i++) {
			pv[ply][i] = pv[ply+1][i];
		}
		pv[ply][i] = 0;
	}
	
	void beforeSearch() {
		timer = new MyTimer();
		tt = new TTable();
		historyTable = new long[256][256];
		pv = new int[DEEPEST+1][DEEPEST+1];
		qTime = 0;
		gmTime = 0;
		nodeCount = 0;
	}
	
	void iterativeDeepning() {
		bestMove = -1;
		int bestScore = -1;
		int preBestMove, preBestScore;
		for (int d=1; d<=maxDepth; d++) {
			preBestMove = bestMove;
			preBestScore = bestScore;
			bestScore = PVS(0, d, -oo, +oo, PV_NODE);
			if (timer.expired(timeLimit)) {
				System.out.println("Expired, use the best move from the previous iteration.");
				bestMove = preBestMove;
				bestScore = preBestScore;
			}
			long t = (long) (timer.elapsedTime()*0.1);
			System.out.println(d+" "+bestScore+" "+t+" "+"0"+" "+Misc.moveForHuman(p, bestMove));

//			for (int i=0; pv[0][i]>0; i++) {
//				System.out.print(Misc.wbMove(pv[0][i])+" ");
//			}
//			System.out.println();
			
//			for (int i=0; pv[0][i]>0; i++) {
//				System.out.print(p.moveForHuman(pv[0][i])+" ");
//				if (!p.legalMove(pv[0][i])) { System.out.println("Illegal move!"); }
//				p.makeMove(pv[0][i]);
//			}
//			for (int i=0; pv[0][i]>0; i++) p.undoMakeMove();
//			System.out.println("\n");
			if (timer.expired(timeLimit)) break;			
		}
		System.out.println("QTime = "+qTime*0.001+" s");
		System.out.println("GenMoveTime = "+gmTime*0.001+" s");
	}
	
	public int findBestMove() {
		beforeSearch();
		iterativeDeepning();
		return bestMove;
	}
	
}
