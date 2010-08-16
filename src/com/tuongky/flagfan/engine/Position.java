package com.tuongky.flagfan.engine;

public class Position {
	
	final static int RANKS = 10;
	final static int FILES = 9;
	final static int BOARD_SIZE = 256;

	final static int[] PIECE_TYPES = 
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	 0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
	 0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,};
	
	int[] board;
	int[] pieces;
	
	int[] bitRanks;
	int[] bitFiles;
	
	int turn;

	public Position() {
		board = new int[BOARD_SIZE];
		pieces = new int[48];
		bitRanks = new int[16];
		bitFiles = new int[16];
	}

	public Position(int[] board90) {
		
	}
	
	public Position(String fen) {
		this(new int[]{1,2,3});
	}
	
	void addPiece(int square, int piece) {
		board[square] = piece;
		pieces[piece] = square;
		bitRanks[square>>4] ^= 1 << (square&0xf);
		bitFiles[square&0xf] ^= 1 << (square>>4);
	}

	void removePiece(int square, int piece) {
		board[piece] = 0;
		pieces[piece] = 0;
		bitRanks[square>>4] ^= 1 << (square&0xf);
		bitFiles[square&0xf] ^= 1 << (square>>4);		
	}
	
}
