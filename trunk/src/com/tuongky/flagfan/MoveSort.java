package com.tuongky.flagfan;

import static com.tuongky.flagfan.Initialization.*;

public class MoveSort {

	final static int MOVE_SORT_RANGE = 7;
	final static long BIG_FACTOR = 1001001001;
	final static long[] piecePowers = {1000, 2, 3, 10, 5, 4, 1};	
	
	int[] mlist;
	long[][] historyTable;
	Position p;
	int moveIndex;
	
	public MoveSort(int[] mlist, long[][] historyTable, Position p) {
		this.mlist = mlist;
		this.historyTable = historyTable;
		this.p = p;
		moveIndex = 0;		
	}
	
	long priority(int src, int dst) {
		return historyTable[src][dst];
	}
	
	public int nextMove() {
		if (moveIndex>MOVE_SORT_RANGE) return mlist[moveIndex++];
		long max = -oo;
		int index = -1;
		for (int i=moveIndex; i<mlist.length; i++) {
			int src = mlist[i]>>8&0xff, dst = mlist[i]&0xff, captured = p.board[dst];
			long p = historyTable[src][dst];
			if (captured!=0) p += piecePowers[pieceTypes[captured]]<<30;
			if (p>max) {
				max = p;
				index = i;
			}
		}
		if (index!=-1&&index!=moveIndex) {
			int tmp = mlist[moveIndex];
			mlist[moveIndex] = mlist[index];
			mlist[index] = tmp;
		}
		return mlist[moveIndex++];
	}
	
}
