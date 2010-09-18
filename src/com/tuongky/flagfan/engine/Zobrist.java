package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;

import java.util.Random;

public class Zobrist {

	private static Zobrist instance = null;
	private static Random rnd = new Random(RANDOM_SEED);

	public static final long[][] ZOBRIST_LOCK_TABLE = new long[48][BOARD_SIZE];
	public static final long ZOBRIST_LOCK_PLAYER = rnd.nextLong();
	
	
	public static Zobrist getInstance() {
		if (instance==null) instance = new Zobrist();
		return instance;
	}
	
	void initAll() {
		long[][] zobristLockTable = new long[14][BOARD_SIZE];
		for (int i=0; i<14; i++)
		for (int j=0; j<BOARD_SIZE; j++) zobristLockTable[i][j] = rnd.nextLong();
		
		for (int piece=16; piece<48; piece++)
			for (int square=0; square<BOARD_SIZE; square++) {
				int pType = PIECE_TYPES[piece];
				if (piece>=32) pType += 7;
				ZOBRIST_LOCK_TABLE[piece][square] = zobristLockTable[pType][square];
			}
	}
	
	private Zobrist() {
		initAll();
	}

}
