package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;

public class TTable {
	
	TTEntry[] data;
	
	public TTable() {
		data = new TTEntry[TSIZE];
	}

	public TTEntry retrieve(long hashkey) {
		int pos = (int) (hashkey&(TSIZE-1));
		if (data[pos]!=null&&data[pos].lock==hashkey) return data[pos];
		return null; 
	}

	public void store(long lock, int score, int move, byte depth, byte flag) {
		int pos = (int) (lock & (TSIZE-1));
		if (data[pos]==null||data[pos].depth<=depth) {
			TTEntry entry = new TTEntry(lock, score, move, depth, flag);
			data[pos] = entry;
		}
	}
	
}
