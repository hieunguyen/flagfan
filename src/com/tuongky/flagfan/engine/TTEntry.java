package com.tuongky.flagfan.engine;

public class TTEntry {
	
	final static int LOWER_BOUND = 0;
	final static int EXACT_SCORE = 1;
	final static int UPPER_BOUND = 2;
	
	long lock;
	int score;
	int move;
	byte depth;
	byte flag;

	public TTEntry(long lock, int score, int move, byte depth, byte flag) {
		this.lock = lock;
		this.score = score;
		this.move = move;
		this.depth = depth;
		this.flag = flag;
	}

}
