package com.tuongky.flagfan;

//import static xiangqi.Initialization.*;

class TT_Entry {
	
	final static int LBOUND = 0;
	final static int VALID = 1;
	final static int UBOUND = 2;
	
	long lock;
	int score, move;
	byte subDepth, flag;

	public TT_Entry(long lock, int score, int move, byte subDepth, byte flag) {
		this.lock = lock;
		this.score = score;
		this.move = move;
		this.subDepth = subDepth;
		this.flag = flag;
	}
	
}

public class TranspositionTable {

	final static int BSIZE = 20;
	final static int TSIZE = 1<<BSIZE;
	TT_Entry data[];
	
	public TranspositionTable() {
		data = new TT_Entry[TSIZE];
//		for (int i=0; i<TSIZE; i++) data[i] = new TT_Entry();
	}
	
	public TT_Entry retrieve(long hashkey) {
		int pos = (int) (hashkey & (TSIZE-1));
		if (data[pos]!=null&&data[pos].lock==hashkey) return data[pos];
		return null;
	}

	public void store(TT_Entry entry) {
		int pos = (int) (entry.lock & (TSIZE-1));
		if (data[pos]==null||entry.subDepth>=data[pos].subDepth) data[pos] = entry;
	}
	
}
