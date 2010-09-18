package com.tuongky.flagfan.engine;

public class TTEntry {
	
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
