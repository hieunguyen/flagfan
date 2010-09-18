package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;

public class GameHistory {

	long[] lockHist;
	int[] lockHash;	
	int[] moves;
	int num;

	public GameHistory(long zlock) {
		num = 0;
		moves = new int[MAX_MOVE_NUM];
		lockHist = new long[MAX_MOVE_NUM+1];
		lockHash = new int[1<<16];
		lockHist[0] = zlock;
		lockHash[(int)(zlock & 0xffff)]++;				
	}

	public void makeMove(int move, long zlock) {
		moves[num++] = move;
		lockHist[num] = zlock;
		lockHash[(int) (zlock & 0xffff)]++;
	}
	
	public void unmakeMove() {
		num--;
	}

	public int popLastMove() {
		num--;
		return moves[num];
	}

	public int getLastMove() {
		return moves[num-1];
	}

	public int boardRepeated(long zlock) {
		if (lockHash[(int)(zlock&0xffff)]==0) return 0;		
		boolean opp_check = true;
		boolean me_check = true;
		int rep = 0;
		for (int index=num-1; index>0; index-=2) {
			if (opp_check&&(moves[index]&0xff000000)==0) opp_check = false;
			if (me_check&&(moves[index-1]&0xff000000)==0) me_check = false;
			
			if ((moves[index]&0xffff)==NULL_MOVE||(moves[index]&0xff0000)!=0
				||(moves[index-1]&0xffff)==NULL_MOVE||(moves[index-1]&0xff0000)!=0) break;
			
			if (lockHist[index-1]==zlock) {
				rep++;
				if (rep>=2) return 1 + (me_check? 2:0) + (opp_check? 4:0);
			}
		}
		return 0;
	}

	public boolean isNullMoveOK() {
		return num<=1 || moves[num-1]!=NULL_MOVE || moves[num-2]!=NULL_MOVE; // Double Nullmove
	}
	
}
