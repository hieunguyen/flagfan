package com.tuongky.flagfan.engine;

import java.util.Arrays;

import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;

import static com.tuongky.flagfan.engine.Piece.*;

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

	private void init(int[] board90, int turn) {
		this.turn = turn;
		int[] startTag = {0,1,3,5,7,9,11};		
		int[][] count = new int[2][7];
		Arrays.fill(count[0], 0);
		Arrays.fill(count[1], 0);
		for (int i=0; i<90; i++) 
		if (board90[i]!=0) {
			int pType, pieceTag, side, rank, file, square;
			pType = Math.abs(board90[i]) - 1;
			side = board90[i]>0 ? RED : BLACK;
			pieceTag = 16 + (side<<4);
			rank = i/9; file = i%9;
			square = (rank+3)<<4|(file+3);
			addPiece(square, pieceTag+startTag[pType]+count[side][pType]);
			count[side][pType]++;
		}				
	}
	
	public Position(int[] board90, int turn) {
		this();
		init(board90, turn);
	}
	
	public Position(String fen) throws FENException {
		this();
		FEN f = new FEN(fen);
		init(f.getBoard90(), f.getTurn());
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
