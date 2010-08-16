package com.tuongky.flagfan;

import static com.tuongky.flagfan.Initialization.*;
import static com.tuongky.flagfan.GameInfor.*;
import static com.tuongky.flagfan.TT_Entry.*;

public class SearchEngine {
	
	Position p;
	TranspositionTable table;
	long[][] historyTable;
	
	long startTime, availableTime;
	int globalMove, bestMove;
	
	public SearchEngine(Position p) {
		this.p = p;
		table = new TranspositionTable();
	}

	void setupTime() {
		startTime = System.currentTimeMillis();
		availableTime = 10*1000; // 10s
	}
	
	void setup() {
		setupTime();
		historyTable = new long[256][256];
	}
	
	boolean isTimeUp() {
		return System.currentTimeMillis()-startTime>availableTime;
	}
	
	String time2str() {
		long t = System.currentTimeMillis() - startTime;
		return (t*0.001) + "s";
	}
	
	int searchMain() {
		setup();
		int move = searchBestMove();
		moveInfo(move);
		System.out.println("Time: "+time2str());
		return move;
	}
	
	int searchBestMove() {
		table = new TranspositionTable();
		int guess = 0, newGuess = 0;
		int delta = 30;
		for (int depth=2; depth<=MAX_DEPTH; depth++) {			
			newGuess = negaScout(guess-delta, guess+delta, 0, depth);
			System.out.println(depth+" "+guess+" "+delta+" "+newGuess);
			if (newGuess<=guess-delta||newGuess>=guess+delta) {
				System.out.println("Re-search...");
				newGuess = negaScout(-oo, oo, 0, depth);
			}
			guess = newGuess;
			globalMove = bestMove;
		}
		System.out.println("Value: "+newGuess);
		return globalMove;
	}
	
	int quiescentSearch(int alpha, int beta, int ply, int depth) {
		
		if (p.checked(p.player)) return negaScout(alpha, beta, ply-1, depth);
			
		int value, bestScore;
		
		value = p.material();
		if (value>=beta) return value;
		if (value>alpha) alpha = value;
		
		int[] mlist = new int[MAX_BRANCH];
		int sl = p.genCaptureMoves(mlist);
		if (sl==0) return value;
		
		MoveSort ms = new MoveSort(mlist, historyTable, p);
		int move;		
		
		bestScore = -WIN_VALUE;
		for (int i=0; i<sl; i++) {
			move = ms.nextMove();
			if (alpha<bestScore) { alpha = bestScore; } 
			if (p.makeMove(move)) {
				value = -quiescentSearch(-beta, -alpha, ply+1, depth);
				p.undoMakeMove();				
				if (value>bestScore) {
					bestScore = value;
					if (bestScore>=beta) break;
				}
			}
		}
		
		return bestScore;
	}
	
	int negaScout(int alpha, int beta, int ply, int depth) {
		
		// Look up in Transposition Table
		TT_Entry entry = table.retrieve(p.zobristLock);
		
		if (entry!=null&&depth-ply<=entry.subDepth) {
			switch (entry.flag) {
				case VALID:
					return entry.score;
				case LBOUND:
					alpha = Math.max(alpha, entry.score);
					break;
				case UBOUND:
					beta = Math.min(beta, entry.score);
					break;
			}
			if (alpha>=beta) return entry.score;
		}
		
		// check board repetition rules
		int repValue = p.boardRepeated();
		
		if (repValue==1||repValue==7) return 0;
		if (repValue==3) return -WIN_VALUE;
		if (repValue==5) return +WIN_VALUE;
		
		// reached frontier node
		if (ply>=depth)	return quiescentSearch(alpha, beta, ply, depth);
		
		int ttMove=NULL_MOVE, iidMove=NULL_MOVE, moveDone = 0, possibleMove = NULL_MOVE;
		int newDepth;
		int value, bestLocalMove, bestScore;
		byte flag;
		bestScore = -WIN_VALUE + ply;
		bestLocalMove = NULL_MOVE;
			
		// Null Move
		if (p.isNullMoveOK()&&!p.checked(p.player)&&depth-ply>=3) {
			p.makeNullMove();
			int nmr = 2;
			if (depth-ply>=6) nmr = 3;
			value = -negaScout(-beta, -alpha, ply+nmr+1, depth);
			p.undoMakeNullMove();
			if (value>=beta) return value;	
			if (value>alpha) { alpha = value; }
		}
		
		// Transposition Move
		if (entry!=null&&entry.move!=NULL_MOVE&&p.legalMove(entry.move)) {
			if (p.makeMove(entry.move)) {
				ttMove = entry.move;
				moveDone++; possibleMove = ttMove;
				newDepth = p.lastMoveIsCheck()?depth+1:depth;
				value = -negaScout(-beta, -alpha, ply+1, newDepth);
				p.undoMakeMove();
				if (value>bestScore) {
					bestScore = value;
					bestLocalMove = entry.move;
					if (ply==0) bestMove = bestLocalMove;
					if (ply==0) moveInfo(bestLocalMove);					
					if (bestScore>=beta) return bestScore;
				}
			}
		}
		
		boolean foundPV = false;
		
		// Internal Iterative Deepening
		if (depth-ply>=3) {
			value = negaScout(alpha, beta, ply+2, depth);
			entry = table.retrieve(p.zobristLock);
			if (entry!=null&&entry.move!=NULL_MOVE&&entry.move!=ttMove&&p.legalMove(entry.move)) {
				if (p.makeMove(entry.move)) {
					iidMove = entry.move;
					moveDone++; possibleMove = iidMove;
					newDepth = p.lastMoveIsCheck()?depth+1:depth;
					value = -negaScout(-beta, -alpha, ply+1, newDepth);
					p.undoMakeMove();
					if (value>bestScore) {
						bestScore = value;
						bestLocalMove = entry.move;
						if (ply==0) moveInfo(bestLocalMove);
						if (ply==0) bestMove = bestLocalMove;
						if (bestScore>=beta) return bestScore;
					}
					if (bestScore>alpha) { alpha = bestScore; foundPV = true; } 
				}
			}
		}
		
		// Normal search
		int[] mlist = new int[MAX_BRANCH];
		int sl = p.genAllMoves(mlist);
		
		MoveSort ms = new MoveSort(mlist, historyTable, p);
		int move;

		for (int i=0; i<sl; i++) {
			move = ms.nextMove();			
			if (move==ttMove||move==iidMove) continue;
			if (alpha<bestScore) { alpha = bestScore; foundPV = true; } 
			if (p.makeMove(move)) {
				moveDone++; possibleMove = move;
				newDepth = p.lastMoveIsCheck()?depth+1:depth;
				if (foundPV) {
					value = -negaScout(-alpha-1, -alpha, ply+1, newDepth);
					if (value>alpha&&value<beta) value = -negaScout(-beta, -alpha, ply+1, newDepth);
				} else	value = -negaScout(-beta, -alpha, ply+1, newDepth);
				p.undoMakeMove();				
				if (value>bestScore) {
					bestScore = value;
					bestLocalMove = move;
					if (ply==0) moveInfo(move);
					if (bestScore>=beta) break;
				}
			}
		}
/*		
		if (moveDone==1) {
			moveInfo(possibleMove);
			move = possibleMove;
			if (p.makeMove(move)) {
				if (foundPV) {
					value = -negaScout(-alpha-1, -alpha, ply, depth);
					if (value>alpha&&value<beta) value = -negaScout(-beta, -alpha, ply, depth);
				} else	value = -negaScout(-beta, -alpha, ply, depth);
				p.undoMakeMove();				
				if (value>bestScore) {
					bestScore = value;
					bestLocalMove = move;
					if (ply==0) moveInfo(move);
				}
			}
		}
*/		
		if (ply==0) bestMove = bestLocalMove;
		
		flag = VALID;
		if (bestScore<=alpha) flag = UBOUND; else
			if (bestScore>=beta) flag = LBOUND;
		
		entry = new TT_Entry(p.zobristLock, bestScore, bestLocalMove, (byte) (depth-ply), flag); 
		table.store(entry);
		
		if (bestLocalMove!=NULL_MOVE)
			historyTable[(bestLocalMove>>8)&0xff][bestLocalMove&0xff] += 1<<(depth-ply);
			                                       
		return bestScore;
	}

}
