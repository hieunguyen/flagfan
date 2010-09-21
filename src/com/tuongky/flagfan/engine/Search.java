package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;

import java.util.Arrays;

import com.tuongky.utils.Misc;
import com.tuongky.utils.MyTimer;

public class Search {

	Position p;
	MoveGenerator mg;
	Evaluator evaluator;
	TTable tt;
	
	MyTimer timer;
	int maxDepth;
	long timeLimit;
	
	long[][] historyTable;
	int[][] moveLists;
	int bestMove;
	
	int[] rootMoves;
	int[] rootMoveScores;
	int rootNum, rootMoveIndex;
	
	long nonPV, pv, pvsNode, qNode;
	
	long lmrFail, lmr;
	
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
		
		qNode++;
		
		TTEntry ttEntry = tt.retrieve(p.zobristLock);
		
		if (ttEntry!=null) {
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
		
		int score = evaluator.eval(p);
		if (score>=beta) return score;
		
		if (score>alpha) alpha = score;
		
		int a = alpha;
		int bestLocalMove = -1;
		
		boolean foundPV = false;
		
		int[] moveList = moveLists[ply];
		int num = mg.genCaptureMoves(p, moveList);
		
		if (num==0) return alpha;
		
		MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);
		
		for (int i=0; i<num; i++) {
			int move = ms.nextMove();
			if (!p.goodCapture(move)) continue;
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
					bestLocalMove = move;
					if (alpha>=beta) { alpha = beta; break; }
				}
			}
		}
		
		byte flag = EXACT_SCORE;
		if (alpha>=beta) flag = LOWER_BOUND; else
			if (alpha<=a) flag = UPPER_BOUND;
		tt.store(p.zobristLock, alpha, bestLocalMove, (byte) 0, flag);
		
		return alpha;
	}
	
	int PVS(int ply, int depth, int alpha, int beta) {
		
		if (timer.expired(timeLimit)) return alpha; // fail low if pass time limit

		pvsNode++;
		
		boolean pvNode = alpha+1<beta;
		
		if (pvNode) pv++; else nonPV++;
		
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
		
		if (depth<=0) return quiesce(ply, alpha, beta);
		
		int score, bestLocalMove = -1;

		// Null Move
		if (depth>2 && p.isNullMoveOK() && !p.isChecked()) {
			p.makeNullMove();
			int nmr = (depth>6) ? 3 : 2;
			score = -PVS(ply+1, depth-1-nmr, -beta, -alpha);
			p.undoMakeNullMove();
			if (score>=beta) return beta;	
			if (score>alpha) { alpha = score; }
		}		
		
		int a = alpha;		
		boolean foundPV = false;

		int ttMove = -1, iidMove = -1;
		
		// Transposition Table Move
		if (ttEntry!=null && p.legalMove(ttEntry.move)) {
			ttMove = ttEntry.move;
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
		
		// IID - Internal Iterative Deepening
		if (alpha<beta && depth>=3) {
			score = PVS(ply, depth-3, alpha, beta);
			TTEntry entry = tt.retrieve(p.zobristLock);			
			if (entry!=null && p.legalMove(entry.move)) {
				iidMove = entry.move;
				if (p.makeMove(iidMove)) {
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
						bestLocalMove = iidMove;
						if (alpha>=beta) alpha = beta; 
					}
				}				
			}
		}
		
		if (alpha<beta) {
			int[] moveList = moveLists[ply];
			int num = mg.genAllMoves(p, moveList);
			MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);		
			for (int i=0; i<num; i++) {
				int move = ms.nextMove();
				if (move==ttMove||move==iidMove) continue;
				boolean isCaptured = p.isCaptureMove(move);
				if (p.makeMove(move)) {
					if (pvNode && !foundPV) {
						score = -PVS(ply+1, depth-1, -beta, -alpha);
					} else {
						if (!pvNode && i>3 && depth>=2 && !isCaptured) {
							score = -PVS(ply+1, depth-2, -alpha-1, -alpha);
							lmr++;
							if (score>alpha) lmrFail++;
						} else score = alpha+1;
						if (score>alpha) {
//							System.out.println("LMR fail");
							score = -PVS(ply+1, depth-1, -alpha-1, -alpha);
							if (alpha<score && score<beta) score = -PVS(ply+1, depth-1, -beta, -alpha);
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
		
		byte flag = EXACT_SCORE;
		if (alpha>=beta) flag = LOWER_BOUND; else
			if (alpha<=a) flag = UPPER_BOUND;
		tt.store(p.zobristLock, alpha, bestLocalMove, (byte) depth, flag);
		
		if (flag==UPPER_BOUND && (ply&1)==1) rootMoveScores[rootMoveIndex]++;
		
		if (bestLocalMove!=-1) {
			historyTable[bestLocalMove>>8&0xff][bestLocalMove&0xff] += 1L<<depth;
		}
		
		return alpha;
	}
	
	void beforeSearch() {
		timer = new MyTimer();
		tt = new TTable();
		historyTable = new long[256][256];		
		bestMove = -1;
		initRootMoves();
		
		// stats
		nonPV = 0;
		pv = 0;
		pvsNode = 0;
		qNode = 0;
		lmr = lmrFail = 0;
	}
	
	void initRootMoves() {
		rootMoves = new int[MAX_WIDTH];
		rootMoveScores = new int[MAX_WIDTH];
		rootNum = mg.genAllMoves(p, rootMoves);
		for (int i=0; i<rootNum; i++) {
			int move = rootMoves[i];
			if (p.makeMove(move)) {
				rootMoveScores[i] = quiesce(0, -oo, +oo);
				p.undoMakeMove();
			}
		}
	}
	
	void sortRootMoves() {
		for (int i=0; i<rootNum; i++)
			if (rootMoves[i]==bestMove) {
				rootMoveScores[i] += 1<<30;
				break;
			}
		
		for (int i=0; i<rootNum; i++)
			for (int j=i+1; j<rootNum; j++) 
			if (rootMoveScores[i]<rootMoveScores[j]) {
				int tmp;
				tmp = rootMoveScores[i];
				rootMoveScores[i] = rootMoveScores[j];
				rootMoveScores[j] = tmp;
				tmp = rootMoves[i];
				rootMoves[i] = rootMoves[j];
				rootMoves[j] = tmp;
			}
		
		rootMoveScores[0] -= 1<<30;
	}
	
	int searchRoot(int depth, int alpha, int beta) {
		int score;
		boolean foundPV = false;
		for (int i=0; i<rootNum; i++) {
			rootMoveIndex = i;
			int move = rootMoves[i];
			if (p.makeMove(move)) {
				if (!foundPV) {
					score = -PVS(1, depth-1, -beta, -alpha);
				} else {
					score = -PVS(1, depth-1, -alpha-1, -alpha);
					if (alpha<score && score<beta) score = -PVS(1, depth-1, -beta, -alpha);
				}
				if (score>alpha && !timer.expired(timeLimit)) {
					alpha = score;
					bestMove = move;
					foundPV = true;
				}
				p.undoMakeMove();
			}
		}
		tt.store(p.zobristLock, alpha, bestMove, (byte) depth, EXACT_SCORE);
		return alpha;
	}
	
	void iterativeDeepning() {
		for (int d=1; d<=maxDepth; d++) {
			sortRootMoves();
			if (d==1) Arrays.fill(rootMoveScores, 0);
			int bestScore = searchRoot(d, -oo, +oo);
			if (timer.expired(timeLimit)) break;
			long t = (long) (timer.elapsedTime()*0.1);
			System.out.println(d+" "+bestScore+" "+t+" "+"0"+" "+Misc.moveForHuman(p, bestMove));
		}
		stats();
	}
	
	void stats() {
		System.out.println();
		System.out.println("--------------------------");
		double nps = 1e-3*pvsNode/(timer.elapsedTime());
		System.out.println("NPS = "+String.valueOf(nps).substring(0, 5)+" mil");
		System.out.println("Number of PV nodes: "+pv);
		System.out.println("Number of NON_PV nodes: "+nonPV);
		System.out.println("Percentage of PV nodes: "+100.0*pv/(pv+nonPV));
		System.out.println("Number of PVS nodes searched: "+pvsNode);
		System.out.println("Number of Q nodes searched: "+qNode);
		System.out.println("LMR fail = "+lmrFail);
		System.out.println("LMR = "+lmr);
		System.out.println("--------------------------");
		System.out.println();
	}
	
	public int findBestMove() {
		beforeSearch();
		iterativeDeepning();
		return bestMove;
	}
	
}
