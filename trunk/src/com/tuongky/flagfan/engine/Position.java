package com.tuongky.flagfan.engine;

import java.util.Arrays;
import java.util.Random;

import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;

import static com.tuongky.flagfan.engine.Piece.*;
import static com.tuongky.flagfan.engine.Evaluator.*;

public class Position {
	
	final static int RANKS = 10;
	final static int FILES = 9;
	final static int BOARD_SIZE = 256;
	final static int MAX_MOVE_NUM = 400;
	final static int NULL_MOVE = 0;
	
	final static int RND_SEED = 1234567;

	final static int[] PIECE_TYPES = 
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	 0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,
	 0,1,1,2,2,3,3,4,4,5,5,6,6,6,6,6,};	
	
	int[] board;
	int[] pieces;
	
	int[] bitRanks;
	int[] bitFiles;
	
	int turn;
	
	int[] moveHistory;
	int num;
	
	long[] lockHist;
	int[] lockHash;	
	
	long[][] zobristLockTable;
	long zobristLockPlayer;
	
	long zobristLock;
	
	int redPower, blackPower;
	
	MoveGenerator mg;

	public Position() {
		board = new int[BOARD_SIZE];
		pieces = new int[48];
		bitRanks = new int[16];
		bitFiles = new int[16];
		moveHistory = new int[MAX_MOVE_NUM];
		num = 0;
		mg = MoveGenerator.getInstance();
		redPower = blackPower = 0;
		lockHist = new long[MAX_MOVE_NUM];
		lockHash = new int[1<<16];
		initZobris();
	}
	
	void initZobris() {
		Random rnd = new Random(RND_SEED);
		zobristLockTable = new long[14][256];
		for (int i=0; i<14; i++)
		for (int j=0; j<256; j++) zobristLockTable[i][j] = rnd.nextLong();
		zobristLockPlayer = rnd.nextLong();
		zobristLock = 0;
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
		lockHist[0] = zobristLock;
		lockHash[(int)(zobristLock&0xffff)]++;		
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
	
	public void addPiece(int square, int piece) {
		board[square] = piece;
		pieces[piece] = square;
		bitRanks[square>>4] ^= 1 << (square&0xf);
		bitFiles[square&0xf] ^= 1 << (square>>4);
		int pType = PIECE_TYPES[piece];
		if (piece<32) {
			redPower += POS_VALUES[pType][square];
		} else {
			blackPower += POS_VALUES[pType][254-square];
			pType += 7;
		}
		zobristLock ^= zobristLockTable[pType][square];
	}

	public void removePiece(int square, int piece) {
		board[piece] = 0;
		pieces[piece] = 0;
		bitRanks[square>>4] ^= 1 << (square&0xf);
		bitFiles[square&0xf] ^= 1 << (square>>4);		
		int pType = PIECE_TYPES[piece];
		if (piece<32) {
			redPower -= POS_VALUES[pType][square];
		} else {
			blackPower -= POS_VALUES[pType][254-square];
			pType += 7;
		}
		zobristLock ^= zobristLockTable[pType][square];
	}
	
	public int movePiece(int move) {
		int src, dst, movedPiece, capturedPiece, pType;
		src = (move>>8)&0xff;
		dst = move&0xff;
		movedPiece = board[src];
		capturedPiece = board[dst];
		if (capturedPiece==0) {
			bitRanks[dst>>4] ^= 1<<(dst&0xf);
			bitFiles[dst&0xf] ^= 1<<(dst>>4);
		} else {
			pieces[capturedPiece] = 0;
			pType = PIECE_TYPES[capturedPiece];
			if (capturedPiece<32) redPower -= POS_VALUES[pType][dst]; 
			else {
				blackPower -= POS_VALUES[pType][254-dst];
				pType += 7;
			}
			zobristLock ^= zobristLockTable[pType][dst];
		}
		board[src] = 0;
		board[dst] = movedPiece;
		pieces[movedPiece] = dst;
		bitRanks[src>>4] ^= 1<<(src&0xf);
		bitFiles[src&0xf] ^= 1<<(src>>4);

		pType = PIECE_TYPES[movedPiece];
		if (movedPiece<32) redPower += POS_VALUES[pType][dst] - POS_VALUES[pType][src];
		else {
			blackPower += POS_VALUES[pType][254-dst] - POS_VALUES[pType][254-src];
			pType += 7;
		}
		
		zobristLock ^= zobristLockTable[pType][src];
		zobristLock ^= zobristLockTable[pType][dst];
		
		return capturedPiece;
	}
	
	public void undoMovePiece(int move, int capturedPiece) {
		int src, dst, movedPiece, pType;
		src = (move>>8)&0xff;
		dst = move&0xff;
		movedPiece = board[dst];
		board[src] = movedPiece;
		board[dst] = capturedPiece;
		pieces[movedPiece] = src;
		bitRanks[src>>4] ^= 1<<(src&0xf);
		bitFiles[src&0xf] ^= 1<<(src>>4);
		
		pType = PIECE_TYPES[movedPiece];
		if (movedPiece<32) redPower += POS_VALUES[pType][src] - POS_VALUES[pType][dst];
		else {
			blackPower += POS_VALUES[pType][254-src] - POS_VALUES[pType][254-dst];
			pType += 7;
		}
		zobristLock ^= zobristLockTable[pType][dst];
		zobristLock ^= zobristLockTable[pType][src];
		
		if (capturedPiece==0) {
			bitRanks[dst>>4] ^= 1<<(dst&0xf);
			bitFiles[dst&0xf] ^= 1<<(dst>>4);			
		} else {
			pieces[capturedPiece] = dst;
			pType = PIECE_TYPES[capturedPiece];
			if (capturedPiece<32) redPower += POS_VALUES[pType][dst]; 
			else {
				blackPower += POS_VALUES[pType][254-dst];
				pType += 7;
			}
			zobristLock ^= zobristLockTable[pType][dst];			
		}
	}
	
	public boolean makeMove(int move) {
		int capturedPiece = movePiece(move);
		if (mg.isChecked(this, turn)) {
			undoMovePiece(move, capturedPiece);
			return false;
		}
		turn ^= 1;
		move |= capturedPiece<<16;
		if (isChecked()) move |= 0x01000000; 
		moveHistory[num++] = move;
		zobristLock ^= zobristLockPlayer;
		lockHist[num] = zobristLock;
		lockHash[(int)(zobristLock&0xffff)]++;
		return true;
	}
	
	public void undoMakeMove() {
		num--;
		turn ^= 1;
		zobristLock ^= zobristLockPlayer;
		undoMovePiece(moveHistory[num], (moveHistory[num]>>16)&0xff);
	}
	
	public boolean isNullMoveOK() {
		return num==0 || moveHistory[num-1] != NULL_MOVE;
	}
	
	public void makeNullMove() {
		turn ^= 1;
		zobristLock ^= zobristLockPlayer;
		moveHistory[num++] = NULL_MOVE;
		lockHist[num] = zobristLock;
		lockHash[(int)(zobristLock&0xffff)]++;
	}
	
	public void undoMakeNullMove() {
		turn ^= 1;
		zobristLock ^= zobristLockPlayer;
		num--;
	}
	
	public int boardRepeated() {
		if (lockHash[(int)(zobristLock&0xffff)]==0) return 0;		
		boolean opp_check = true;
		boolean me_check = true;
		int rep = 0;
		for (int index=num-1; index>0; index-=2) {
			if (opp_check&&(moveHistory[index]&0xff000000)==0) opp_check = false;
			if (me_check&&(moveHistory[index-1]&0xff000000)==0) me_check = false;
			
			if ((moveHistory[index]&0xffff)==NULL_MOVE||(moveHistory[index]&0xff0000)!=0
				||(moveHistory[index-1]&0xffff)==NULL_MOVE||(moveHistory[index-1]&0xff0000)!=0) break;
			
			if (lockHist[index-1]==zobristLock) {
				rep++;
				if (rep>=2) return 1 + (me_check? 2:0) + (opp_check? 4:0);
			}
		}
		return 0;
	}
	
	public void printMoveForHuman(int move) {
		String moveStr = moveForHuman(move);
		System.out.println(moveStr);
	}

	public String moveForHuman(int move) {
		return moveForHuman(move, turn);
	}
	
	public String moveForHuman(int move, int side) {
		if (move<=0) return "No moves found!";
		int src, dst, r1, f1, r2, f2;
		String moveStr, dir;
		src = (move>>8)&0xff;
		dst = move&0xff;
		char pc = PIECE_CHARS.charAt(PIECE_TYPES[board[src]]);
		r1 = (src>>4)-2;
		f1 = (src&0xf)-2;
		r2 = (dst>>4)-2;
		f2 = (dst&0xf)-2;
		if (side==RED) { f1 = 10-f1; f2 = 10-f2; }
		if (r1!=r2) {
			if (side==RED^r2<r1) dir = "-"; else dir = "+";
			if (f1==f2) {
				moveStr = ""+pc+f1+dir+Math.abs(r1-r2);			
			} else {
				moveStr = ""+pc+f1+dir+f2;
			}
		} else {
			dir = ".";
			moveStr = ""+pc+f1+dir+f2;			
		}
		return moveStr;
	}
	
	public boolean legalMove(int move) {
		return mg.legalMove(this, move);
	}
	
	public boolean isChecked() {
		return mg.isChecked(this, turn);
	}
	
	public boolean isChecked(int side) {
		return mg.isChecked(this, side);
	}

	public int material() {
		int r = redPower - blackPower;
		return turn==RED ?  r : -r;   		
	}

	public int see(int src, int dst) {
		
		return 0;
	}

	public int see(int move) {
		return see(move>>8&0xff, move&0xff);
	}
	
}
