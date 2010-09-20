package com.tuongky.flagfan.engine;

import static com.tuongky.flagfan.engine.Constants.*;
import static com.tuongky.flagfan.engine.MoveGenerator.*;
import static com.tuongky.flagfan.engine.Evaluator.*;
import static com.tuongky.flagfan.engine.Zobrist.*;

import java.util.Arrays;

import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;

public class Position {

	int[] board;
	int[] pieces;

	int[] bitRanks;
	int[] bitFiles;

	int turn;
	int material;
	long zobristLock;

	GameHistory gameHistory;

	public Position() {
		board = new int[BOARD_SIZE];
		pieces = new int[48];
		bitRanks = new int[16];
		bitFiles = new int[16];
		material = 0;
		zobristLock = 0;
		MoveGenerator.getInstance();
		Evaluator.getInstance();
		Zobrist.getInstance();
	}

	private void init(int[] board90, int turn) {
		this.turn = turn;
		int[] startTag = { 0, 1, 3, 5, 7, 9, 11 };
		int[][] count = new int[3][7];
		Arrays.fill(count[0], 0);
		Arrays.fill(count[1], 0);
		for (int i = 0; i < 90; i++)
			if (board90[i] != 0) {
				int pType, pieceTag, side, rank, file, square;
				pType = Math.abs(board90[i]) - 1;
				side = board90[i] > 0 ? RED : BLACK;
				pieceTag = 16 + (side << 4);
				rank = i / 9;
				file = i % 9;
				square = (rank + 3) << 4 | (file + 3);
				addPiece(square, pieceTag + startTag[pType]
						+ count[side][pType]);
				count[side][pType]++;
			}
		gameHistory = new GameHistory(zobristLock);
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
		bitRanks[square >> 4] ^= 1 << (square & 0xf);
		bitFiles[square & 0xf] ^= 1 << (square >> 4);
		material += PIECE_POS_VALUES[piece][square];
		zobristLock ^= ZOBRIST_LOCK_TABLE[piece][square];
	}

	public void removePiece(int square, int piece) {
		board[piece] = 0;
		pieces[piece] = 0;
		bitRanks[square >> 4] ^= 1 << (square & 0xf);
		bitFiles[square & 0xf] ^= 1 << (square >> 4);
		material -= PIECE_POS_VALUES[piece][square];
		zobristLock ^= ZOBRIST_LOCK_TABLE[piece][square];
	}
	
	public int getMeterial() {
		return turn==RED ? material : -material;
	}

	public int movePiece(int move) {
		int src, dst, movedPiece, capturedPiece;
		src = move >> 8 & 0xff;
		dst = move & 0xff;
		movedPiece = board[src];
		capturedPiece = board[dst];
		if (capturedPiece == 0) {
			bitRanks[dst >> 4] ^= 1 << (dst & 0xf);
			bitFiles[dst & 0xf] ^= 1 << (dst >> 4);
		} else {
			pieces[capturedPiece] = 0;
			material -= PIECE_POS_VALUES[capturedPiece][dst];
			zobristLock ^= ZOBRIST_LOCK_TABLE[capturedPiece][dst];
		}
		board[src] = 0;
		board[dst] = movedPiece;
		pieces[movedPiece] = dst;
		bitRanks[src >> 4] ^= 1 << (src & 0xf);
		bitFiles[src & 0xf] ^= 1 << (src >> 4);
		material += PIECE_POS_VALUES[movedPiece][dst] - PIECE_POS_VALUES[movedPiece][src];
		zobristLock ^= ZOBRIST_LOCK_TABLE[movedPiece][src] ^ ZOBRIST_LOCK_TABLE[movedPiece][dst];
		return capturedPiece;
	}

	public void undoMovePiece(int move, int capturedPiece) {
		int src, dst, movedPiece;
		src = (move >> 8) & 0xff;
		dst = move & 0xff;
		movedPiece = board[dst];
		board[src] = movedPiece;
		board[dst] = capturedPiece;
		pieces[movedPiece] = src;
		bitRanks[src >> 4] ^= 1 << (src & 0xf);
		bitFiles[src & 0xf] ^= 1 << (src >> 4);
		material -= PIECE_POS_VALUES[movedPiece][dst] - PIECE_POS_VALUES[movedPiece][src];
		zobristLock ^= ZOBRIST_LOCK_TABLE[movedPiece][src] ^ ZOBRIST_LOCK_TABLE[movedPiece][dst];
		if (capturedPiece == 0) {
			bitRanks[dst >> 4] ^= 1 << (dst & 0xf);
			bitFiles[dst & 0xf] ^= 1 << (dst >> 4);
		} else {
			pieces[capturedPiece] = dst;
			material += PIECE_POS_VALUES[capturedPiece][dst];
			zobristLock ^= ZOBRIST_LOCK_TABLE[capturedPiece][dst];
		}
	}

	public boolean makeMove(int move) {
		int capturedPiece = movePiece(move);
		if (isChecked()) {
			undoMovePiece(move, capturedPiece);
			return false;
		}
		turn ^= 1;
		zobristLock ^= ZOBRIST_LOCK_PLAYER;
		move |= capturedPiece << 16;
		if (isChecked()) move |= 0x01000000; // move gives check
		gameHistory.makeMove(move, zobristLock);
		return true;
	}

	public void undoMakeMove() {
		turn ^= 1;
		zobristLock ^= ZOBRIST_LOCK_PLAYER;
		int move = gameHistory.popLastMove();
		undoMovePiece(move, (move >> 16) & 0xff);
	}

	public boolean isNullMoveOK() {
		return gameHistory.isNullMoveOK();
	}

	public void makeNullMove() {
		turn ^= 1;
		zobristLock ^= ZOBRIST_LOCK_PLAYER;
		gameHistory.makeMove(NULL_MOVE, zobristLock);
	}

	public void undoMakeNullMove() {
		turn ^= 1;
		zobristLock ^= ZOBRIST_LOCK_PLAYER;
		gameHistory.popLastMove();
	}

	public int boardRepeated() {
		return gameHistory.boardRepeated(zobristLock);
	}

	boolean legalMove(int move) {
		int src, dst, rank, file;
		int pieceTag, movedPiece, capturedPiece;
		
		src = (move>>8)&0xff;
		dst = move&0xff;
		pieceTag = 16 + (turn<<4);
		movedPiece = board[src];
		capturedPiece = board[dst];
		
		if ((movedPiece&pieceTag)==0) return false;
		if ((capturedPiece&pieceTag)!=0) return false;
		
		switch (PIECE_TYPES[movedPiece]) {
			case KING:
				return IN_PALACE[dst] && LEGAL_MOVE_TAB[dst-src+256]==1;
			case ADVISOR:
				return IN_PALACE[dst] && LEGAL_MOVE_TAB[dst-src+256]==2;
			case ELEPHANT:
				return ((src^dst)&0x80)==0 && LEGAL_MOVE_TAB[dst-src+256]==3 && board[(src+dst)>>1]==0;
			case ROOK:
				rank = src >> 4;
				file = src & 0xf;				
				if (file==(dst&0xf)) {					
					if (capturedPiece==0) {						
						return (ROOK_FILE_NO_CAP_MASK[rank-3][bitFiles[file]>>3] & (1 << (dst>>4))) != 0;
					} else {
						return (ROOK_FILE_CAP_MASK[rank-3][bitFiles[file]>>3] & (1 << (dst>>4))) != 0;
					}
				} else
				if (rank==(dst>>4)) {
					if (capturedPiece==0) {
						return (ROOK_RANK_NO_CAP_MASK[file-3][bitRanks[rank]>>3] & (1 << (dst&0xf))) != 0;
					} else {
						return (ROOK_RANK_CAP_MASK[file-3][bitRanks[rank]>>3] & (1 << (dst&0xf))) != 0;
					}
				}
				return false;
			case CANNON:
				rank = src >> 4;
				file = src & 0xf;
				if (file==(dst&0xf)) {
					if (capturedPiece==0) {
						return (ROOK_FILE_NO_CAP_MASK[rank-3][bitFiles[file]>>3] & (1 << (dst>>4))) != 0;
					} else {
						return (CANNON_FILE_CAP_MASK[rank-3][bitFiles[file]>>3] & (1 << (dst>>4))) != 0;
					}
				} else
				if (rank==(dst>>4)) {
					if (capturedPiece==0) {
						return (ROOK_RANK_NO_CAP_MASK[file-3][bitRanks[rank]>>3] & (1 << (dst&0xf))) != 0;
					} else {
						return (CANNON_RANK_CAP_MASK[file-3][bitRanks[rank]>>3] & (1 << (dst&0xf))) != 0;
					}
				}
				return false;
			case HORSE:
				int hleg = HORSE_LEG_TAB[dst-src+256];
				return hleg!=0 && board[src+hleg]==0;
			default: // PAWN
				if (pieceTag==16)
					return dst==src-16 || ((dst&0x80)==0&&Math.abs(dst-src)==1);
				else
					return dst==src+16 || ((dst&0x80)!=0&&Math.abs(dst-src)==1);
		}
	}
	
	public boolean isChecked(int side) {
		int src, dst, rank, file, pieceTag, y;
		
		pieceTag = 32 - (side<<4);
		src = pieces[48-pieceTag]; // KING
		
		rank = src >> 4;
		file = src & 0xf;

		// KING kills KING
		dst = pieces[pieceTag];
		if (dst!=0) {
			y = dst & 0xf;
			if (y==file&&(ROOK_FILE_CAP_MASK[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
		}
		
		// ROOK kills KING
		for (int i=5; i<=6; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((ROOK_RANK_CAP_MASK[file-3][bitRanks[rank]>>3]&(1<<(dst&0xf)))!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((ROOK_FILE_CAP_MASK[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
				}
			}
		}
	
		// CANNON kills KING
		for (int i=7; i<=8; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((CANNON_RANK_CAP_MASK[file-3][bitRanks[rank]>>3]&(1<<(dst&0xf)))!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((CANNON_FILE_CAP_MASK[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
				}
			}			
		}
	
		// HORSE kills KING
		for (int i=9; i<=10; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				int hleg = HORSE_LEG_TAB[src-dst+256];
				if (hleg!=0&&board[dst+hleg]==0) return true;
			}
		}

		// PAWN kills KING
		int ptmp;
		
		ptmp = board[src-1];
		if ((ptmp&pieceTag)!=0&&PIECE_TYPES[ptmp]==PAWN) return true;

		ptmp = board[src+1];
		if ((ptmp&pieceTag)!=0&&PIECE_TYPES[ptmp]==PAWN) return true;
		
		ptmp = board[src-16+(side<<5)];
		if ((ptmp&pieceTag)!=0&&PIECE_TYPES[ptmp]==PAWN) return true;
		
		return false;
	}
	
	public boolean isChecked() {
		return isChecked(turn);
	}

	public int attackers(int square, int[] ps) {
		int p, src, rank, file, x, y, i, e;
		
		rank = square >> 4;
		file = square & 0xf;
		
		int num = 0;

		// PAWN attack
		p = board[square - 1];
		if ( PIECE_TYPES[p]==PAWN && ((p>>5==RED)^((square&0x80)!=0)) ) ps[num++] = p;
		p = board[square + 1];
		if ( PIECE_TYPES[p]==PAWN && ((p>>5==RED)^((square&0x80)!=0)) ) ps[num++] = p;
		p = board[square + 16];
		if (PIECE_TYPES[p]==PAWN && p>>5==RED) ps[num++] = p;
		p = board[square - 16];
		if (PIECE_TYPES[p]==PAWN && p>>5==BLACK) ps[num++] = p;
		
		// ADVISOR attack
		if (ADVISOR_REACHABLE[square]) {
			i = 0;
			src = ADVISOR_MOVES[square][0];
			while (src!=0) {
				p = board[src];
				if (PIECE_TYPES[p]==ADVISOR) ps[num++] = p;
				i++;
				src = ADVISOR_MOVES[square][i];
			}
		}
		
		// ELEPHANT attack
		if (ELEPHANT_REACHABLE[square]) {
			i = 0;
			src = ELEPHANT_MOVES[square][0];
			while (src!=0) {
				e = ELEPHANT_EYES[square][i];
				p = board[src];
				if (board[e]==0 && PIECE_TYPES[p]==ELEPHANT) ps[num++] = p;
				i++;
				src = ELEPHANT_MOVES[square][i];
			}
		}
		
		// HORSE attack
		i = 0;
		src = HORSE_MOVES[square][i];
		while (src!=0) {
			e = square + REVERSE_HORSE_LEG_TAB[src-square+BOARD_SIZE];
			p = board[src];
			if (board[e]==0 && PIECE_TYPES[p]==HORSE) ps[num++] = p;
			i++;
			src = HORSE_MOVES[square][i];
		}

		// CANNON attack
		for (int dir=0; dir<2; dir++) {
			y = CANNON_RANK_CAP_TAB[file-3][bitRanks[rank]>>3][dir];
			if (y!=file) {
				src = rank<<4|y;
				p = board[src];
				if (PIECE_TYPES[p]==CANNON) ps[num++] = p;
			}
		}
	
		for (int dir=0; dir<2; dir++) {
			x = CANNON_FILE_CAP_TAB[rank-3][bitFiles[file]>>3][dir];
			if (x!=rank) {
				src = x<<4|file;
				p = board[src];
				if (PIECE_TYPES[p]==CANNON) ps[num++] = p;
			}
		}
	
		// ROOK attack
		for (int dir=0; dir<2; dir++) {
			y = ROOK_RANK_CAP_TAB[file-3][bitRanks[rank]>>3][dir];
			if (y!=file) {
				src = rank<<4|y;
				p = board[src];
				if (PIECE_TYPES[p]==ROOK) ps[num++] = p;
			}
		}
	
		for (int dir=0; dir<2; dir++) {
			x = ROOK_FILE_CAP_TAB[rank-3][bitFiles[file]>>3][dir];
			if (x!=rank) {
				src = x<<4|file;
				p = board[src];
				if (PIECE_TYPES[p]==ROOK) ps[num++] = p;
			}
		}
	
		
		// KING attack
		
		return num;
	}
	
	public boolean goodCapture(int move) {
		int src = move >> 8 & 0xff;
		int dst = move & 0xff;
		if (PIECE_VALUES[board[src]]<=PIECE_VALUES[board[dst]]) return true;
		return see(src, dst)>=0;
	}
	
	public int see(int src, int dst) {
		int[] ps = new int[32];
		int num = attackers(dst, ps);
//		System.out.println("attackers = "+num);
		int next = -1;
		for (int i=0; i<num; i++)
			if (board[src]==ps[i]) { next = i; break; }
		int[] values = new int[32];
		int cp = 0;
		int pieceTag = 16 + (turn<<4);
		values[cp++] = PIECE_VALUES[board[dst]];
		while (next!=-1) {
			int p = ps[next];
			values[cp++] = PIECE_VALUES[p];
//			System.out.println(PIECE_LETTERS.charAt(PIECE_TYPES[p]));
			int r, f;
			r = pieces[p] >> 4;
			f = pieces[p] & 0xf;
			bitRanks[r] ^= 1<<f;
			bitFiles[f] ^= 1<<r;
			ps[next] = -p;

			pieceTag = 48 - pieceTag;
			next = -1;
			for (int i=0; i<num; i++)
				if (ps[i]>0 && (pieceTag&ps[i])!=0) {
					next = i;
					break;
				}
		}
		for (int i=0; i<num; i++)
			if (ps[i]<0) {
				int p = -ps[i];
				int r, f;
				r = pieces[p] >> 4;
				f = pieces[p] & 0xf;
				bitRanks[r] ^= 1<<f;
				bitFiles[f] ^= 1<<r;
			}
//		Misc.debug(ps);
//		Misc.debug(values);
		values[cp-1] = 0;
		for (int i=cp-2; i>0; i--) {
			values[i] = Math.max(0, values[i]-values[i+1]);
		}
		return values[0]-values[1];
	}

	public int see(int move) {
		return see(move >> 8 & 0xff, move & 0xff);
	}
	
	public int[] getBoard() {
		return board;
	}

	public int[] getPieces() {
		return pieces;
	}

	public int getTurn() {
		return turn;
	}

}
