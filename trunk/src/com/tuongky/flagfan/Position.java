package com.tuongky.flagfan;

import java.util.Arrays;
import static com.tuongky.flagfan.Initialization.*;
import static com.tuongky.flagfan.GameInfor.*;

public class Position {
	
	int[] board;
	int[] pieces;
	int[] bitRanks, bitFiles;
	
	int[] moveList;
	long[] lockHist;
	int[] lockHash;
	
	long zobristLock;
	int player;
	int redPower, blackPower;
	int moveNum, startMove;

	public Position() {
		board = new int[256];
		pieces = new int[48];
		bitRanks = new int[16];
		bitFiles = new int[16];
		moveList = new int[MAX_MOVE_NUM];
		lockHist = new long[MAX_MOVE_NUM];
		lockHash = new int[1<<16];
		moveNum = 0;
	}
	
	public Position(GameInfor ginf) {
		this();
		clearBoard();
		
		int[] startTag = {0,1,3,5,7,9,11};		
		int[][] count = new int[2][7];
		Arrays.fill(count[0], 0);
		Arrays.fill(count[1], 0);
		
		player = ginf.side;
		
		for (int sqr=0; sqr<256; sqr++) 
		if (ginf.state[sqr]!=0) {
			int pType, pieceTag, side;
			pType = Math.abs(ginf.state[sqr]) - 1;
			if (ginf.state[sqr]>0) side = 0; else side = 1;
			pieceTag = 16 + (side<<4);
			addPiece(sqr, pieceTag+startTag[pType]+count[side][pType]);
			count[side][pType]++;
		}
		
		lockHist[0] = zobristLock;
		lockHash[(int)(zobristLock&0xffff)]++;
	}
	
	void clearBoard() {
		Arrays.fill(board, 0);
		Arrays.fill(pieces, 0);
		Arrays.fill(bitRanks, 0);
		Arrays.fill(bitFiles, 0);
		zobristLock = 0;
		redPower = blackPower = 0;
	}
	
	void changeSide() {
		player ^= 1;
		zobristLock ^= zobristLockPlayer;
	}
	
	void addPiece(int square, int piece) {
		board[square] = piece;
		pieces[piece] = square;
		
		bitRanks[square>>4] ^= bitRankMask[square];
		bitFiles[square&0xf] ^= bitFileMask[square];
		
		int pType = pieceTypes[piece];
		
		if (piece<32) redPower += posValues[pType][square];
		else {
			blackPower += posValues[pType][254-square];
			pType+=7;
		}
		
		zobristLock ^= zobristLockTable[pType][square];
	}
	
	void removePiece(int square, int piece) {
		board[square] = 0;
		pieces[piece] = 0;
		
		bitRanks[square>>4] ^= bitRankMask[square];
		bitFiles[square&0xf] ^= bitFileMask[square];
		
		int pType = pieceTypes[piece];
		
		if (piece<32) redPower -= posValues[pType][square];
		else {
			blackPower -= posValues[pType][254-square];
			pType+=7;
		}
		
		zobristLock ^= zobristLockTable[pType][square];
	}
	
	int movePiece(int move) {
		int src, dst, movedPiece, capturedPiece, pType;
		
		src = (move>>8)&0xff;
		dst = move&0xff;
		
		movedPiece = board[src];
		capturedPiece = board[dst];
		
		if (capturedPiece==0) {
			bitRanks[dst>>4] ^= bitRankMask[dst];
			bitFiles[dst&0xf] ^= bitFileMask[dst];
		} else {
			pieces[capturedPiece] = 0;
			pType = pieceTypes[capturedPiece];
			if (capturedPiece<32) redPower -= posValues[pType][dst];
			else {
				blackPower -= posValues[pType][254-dst];
				pType+=7;
			}
			zobristLock ^= zobristLockTable[pType][dst];
		}
		
		board[src] = 0;
		board[dst] = movedPiece;
		pieces[movedPiece] = dst;

		bitRanks[src>>4] ^= bitRankMask[src];
		bitFiles[src&0xf] ^= bitFileMask[src];
		
		pType = pieceTypes[movedPiece];
		if (movedPiece<32) redPower += posValues[pType][dst] - posValues[pType][src];
		else {
			blackPower += posValues[pType][254-dst] - posValues[pType][254-src];
			pType += 7;
		}
		
		zobristLock ^= zobristLockTable[pType][dst] ^ zobristLockTable[pType][src];		
		return capturedPiece;
	}
	
	void undoMovePiece(int move, int capturedPiece) {
		int src, dst, movedPiece, pType;
		
		src = (move>>8)&0xff;
		dst = move&0xff;
		
		movedPiece = board[dst];
		board[src] = movedPiece;
		board[dst] = capturedPiece;
		
		pieces[movedPiece] = src;
		
		bitRanks[src>>4] ^= bitRankMask[src];
		bitFiles[src&0xf] ^= bitFileMask[src];

		pType = pieceTypes[movedPiece];
		if (movedPiece<32) redPower += posValues[pType][src] - posValues[pType][dst];
		else {
			blackPower += posValues[pType][254-src] - posValues[pType][254-dst];
			pType += 7;
		}

		zobristLock ^= zobristLockTable[pType][src] ^ zobristLockTable[pType][dst];
		
		if (capturedPiece==0) {
			bitRanks[dst>>4] ^= bitRankMask[dst];
			bitFiles[dst&0xf] ^= bitFileMask[dst];			
		} else {
			pieces[capturedPiece] = dst;
			pType = pieceTypes[capturedPiece];
			if (capturedPiece<32) redPower += posValues[pType][dst]; 
			else {
				blackPower += posValues[pType][254-dst];
				pType+=7;
			}
			zobristLock ^= zobristLockTable[pType][dst];
		}
		
	}
	
	boolean makeMove(int move) {
		int capturedPiece = movePiece(move);
		if (checked(player)) {
			undoMovePiece(move, capturedPiece);
			return false;
		} else {
			changeSide();
			move|=capturedPiece<<16;
			if (checked(player)) move|=0x01000000; 
			moveList[moveNum++] = move;
			lockHist[moveNum] = zobristLock;
			lockHash[(int)(zobristLock&0xffff)]++;
			return true;
		}
	}
	
	boolean lastMoveIsCheck() {
		return (moveList[moveNum-1]&0xff000000)>0;
	}
	
	void undoMakeMove() {
		lockHash[(int)(zobristLock&0xffff)]--;		
		moveNum--;
		changeSide();
		undoMovePiece(moveList[moveNum], (moveList[moveNum]>>16)&0xff);		
	}
	
	boolean isNullMoveOK() {
		return (moveNum==0||moveList[moveNum-1]!=NULL_MOVE);
	}
	
	void makeNullMove() {
		changeSide();
		moveList[moveNum++] = NULL_MOVE;
		lockHist[moveNum] = zobristLock;
		lockHash[(int)(zobristLock&0xffff)]++;
	}
	
	void undoMakeNullMove() {
		lockHash[(int)(zobristLock&0xffff)]--;
		moveNum--;
		changeSide();
	}
	
	int material() {
		if (player==0) return redPower-blackPower;
		else return blackPower - redPower;
	}	
	
	boolean legalMove(int move) {
		int src, dst, rank, file;
		int pieceTag, movedPiece, capturedPiece;
		
		src = (move>>8)&0xff;
		dst = move&0xff;
		pieceTag = 16 + (player<<4);
		movedPiece = board[src];
		capturedPiece = board[dst];
		
		if ((movedPiece&pieceTag)==0) return false;
		if ((capturedPiece&pieceTag)!=0) return false;
		
		switch (pieceTypes[movedPiece]) {
			case 0: // KING
				return inPalace[dst] && legalMoveTab[dst-src+256]==1;
			case 1: // BISHOP
				return inPalace[dst] && legalMoveTab[dst-src+256]==2;
			case 2: // ELEPHANT
				return ((src^dst)&0x80)==0 && legalMoveTab[dst-src+256]==3 && board[(src+dst)>>1]==0;
			case 3: // ROOK
				rank = src >> 4;
				file = src & 0xf;				
				if (file==(dst&0xf)) {					
					if (capturedPiece==0) {						
						return (rookFileNoCapMask[rank-3][bitFiles[file]>>3] & bitFileMask[dst]) != 0;
					} else {
						return (rookFileCapMask[rank-3][bitFiles[file]>>3] & bitFileMask[dst]) != 0;
					}
				} else
				if (rank==(dst>>4)) {
					if (capturedPiece==0) {
						return (rookRankNoCapMask[file-3][bitRanks[rank]>>3] & bitRankMask[dst]) != 0;
					} else {
						return (rookRankCapMask[file-3][bitRanks[rank]>>3] & bitRankMask[dst]) != 0;
					}
				}
				return false;
			case 4: // CANNON
				rank = src >> 4;
				file = src & 0xf;
				if (file==(dst&0xf)) {
					if (capturedPiece==0) {
						return (rookFileNoCapMask[rank-3][bitFiles[file]>>3] & bitFileMask[dst]) != 0;
					} else {
						return (cannonFileCapMask[rank-3][bitFiles[file]>>3] & bitFileMask[dst]) != 0;
					}
				} else
				if (rank==(dst>>4)) {
					if (capturedPiece==0) {
						return (rookRankNoCapMask[file-3][bitRanks[rank]>>3] & bitRankMask[dst]) != 0;
					} else {
						return (cannonRankCapMask[file-3][bitRanks[rank]>>3] & bitRankMask[dst]) != 0;
					}
				}
				return false;
			case 5: // KNIGHT
				int hleg = horseLegTab[dst-src+256];
				return hleg!=0 && board[src+hleg]==0;
			default: // PAWN
				if (pieceTag==16)
					return dst==src-16 || ((dst&0x80)==0&&Math.abs(dst-src)==1);
				else
					return dst==src+16 || ((dst&0x80)!=0&&Math.abs(dst-src)==1);
		}
	}
	
	boolean checked(int side) {
		int src, dst, rank, file, pieceTag, x, y;
		
		pieceTag = 32 - (side<<4);
		src = pieces[48-pieceTag];
		
		rank = src >> 4;
		file = src & 0xf;

		// KING kills KING
		dst = pieces[pieceTag];
		if (dst!=0) {
			y = dst & 0xf;
			if (y==file&&(rookFileCapMask[rank-3][bitFiles[file]>>3]&bitFileMask[dst])!=0) return true;
		}
		
		// ROOK kills KING
		for (int i=5; i<=6; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((rookRankCapMask[file-3][bitRanks[rank]>>3]&bitRankMask[dst])!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((rookFileCapMask[rank-3][bitFiles[file]>>3]&bitFileMask[dst])!=0) return true;
				}
			}
		}
		
	
		// CANNON kills KING
		for (int i=7; i<=8; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((cannonRankCapMask[file-3][bitRanks[rank]>>3]&bitRankMask[dst])!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((cannonFileCapMask[rank-3][bitFiles[file]>>3]&bitFileMask[dst])!=0) return true;
				}
			}			
		}
		
	
		// KNIGHT kills KING
		for (int i=9; i<=10; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				int hleg = horseLegTab[src-dst+256];
				if (hleg!=0&&board[dst+hleg]==0) return true;
			}
		}

		// PAWN kills KING
		int ptmp;
		
		ptmp = board[src-1];
		if ((ptmp&pieceTag)!=0&&pieceTypes[ptmp]==6) return true;

		ptmp = board[src+1];
		if ((ptmp&pieceTag)!=0&&pieceTypes[ptmp]==6) return true;
		
		ptmp = board[src-16+(side<<5)];
		if ((ptmp&pieceTag)!=0&&pieceTypes[ptmp]==6) return true;
		
		return false;
	}

	int genAllMoves(int[] mlist) {
		int src, dst, move, num;
		int pieceTag, ind, rank, file, x, y;
		
		num = 0;
		
		pieceTag = 16 + (player<<4);
		
		// KING
		src = pieces[pieceTag];
		if (src!=0) {
			ind = 0;
			dst = kingMoves[src][ind];
			while (dst!=0) {
				if ((board[dst]&pieceTag)==0) {
					move = (src<<8)+dst;
					mlist[num++] = move;
				}
				ind++;
				dst = kingMoves[src][ind];
			}
		}
		
		// BISHOP
		for (int i=1; i<=2; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = bishopMoves[src][ind];
				while (dst!=0) {
					if ((board[dst]&pieceTag)==0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = bishopMoves[src][ind];
				}				
			}
		}
		
		// ELEPHANT
		for (int i=3; i<=4; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = elephantMoves[src][ind];				
				while (dst!=0) {
					int ee = elephantEyes[src][ind];
					if (board[ee]==0&&(board[dst]&pieceTag)==0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = elephantMoves[src][ind];
				}				
			}			
		}
		
		// ROOK		
		for (int i=5; i<=6; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				// HORIZONTAL
				
				y = rookRankNoCapTab[file-3][bitRanks[rank]>>3][0];
				while (y>file) {
					move = src<<8|rank<<4|y;
					mlist[num++] = move;
					y--;
				}

				y = rookRankNoCapTab[file-3][bitRanks[rank]>>3][1];
				while (y<file) {
					move = src<<8|rank<<4|y;
					mlist[num++] = move;
					y++;
				}
				
				y = rookRankCapTab[file-3][bitRanks[rank]>>3][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				y = rookRankCapTab[file-3][bitRanks[rank]>>3][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				// VERTICAL
				
				x = rookFileNoCapTab[rank-3][bitFiles[file]>>3][0];
				while (x>rank) {
					move = src<<8|x<<4|file;
					mlist[num++] = move;
					x--;
				}

				x = rookFileNoCapTab[rank-3][bitFiles[file]>>3][1];
				while (x<rank) {
					move = src<<8|x<<4|file;
					mlist[num++] = move;
					x++;
				}
				
				x = rookFileCapTab[rank-3][bitFiles[file]>>3][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				x = rookFileCapTab[rank-3][bitFiles[file]>>3][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
			}			
		}

		// CANNON
		for (int i=7; i<=8; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				// HORIZONTAL
				
				y = rookRankNoCapTab[file-3][bitRanks[rank]>>3][0];
				while (y>file) {
					move = src<<8|rank<<4|y;
					mlist[num++] = move;
					y--;
				}

				y = rookRankNoCapTab[file-3][bitRanks[rank]>>3][1];
				while (y<file) {
					move = src<<8|rank<<4|y;
					mlist[num++] = move;
					y++;
				}
				
				y = cannonRankCapTab[file-3][bitRanks[rank]>>3][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				y = cannonRankCapTab[file-3][bitRanks[rank]>>3][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				// VERTICAL
				
				x = rookFileNoCapTab[rank-3][bitFiles[file]>>3][0];
				while (x>rank) {
					move = src<<8|x<<4|file;
					mlist[num++] = move;
					x--;
				}

				x = rookFileNoCapTab[rank-3][bitFiles[file]>>3][1];
				while (x<rank) {
					move = src<<8|x<<4|file;
					mlist[num++] = move;
					x++;
				}
				
				x = cannonFileCapTab[rank-3][bitFiles[file]>>3][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				x = cannonFileCapTab[rank-3][bitFiles[file]>>3][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
			}			
		}

		// KNIGHT
		for (int i=9; i<=10; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = knightMoves[src][ind];
				while (dst!=0) {
					int hleg = horseLegs[src][ind];
					if (board[hleg]==0&&(board[dst]&pieceTag)==0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = knightMoves[src][ind];
				}				
			}			
		}
		
		// PAWN
		for (int i=11; i<=15; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = pawnMoves[player][src][ind];
				while (dst!=0) {
					if ((board[dst]&pieceTag)==0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = pawnMoves[player][src][ind];
				}				
			}			
		}
		
		return num;
	}
	
	int genCaptureMoves(int[] mlist) {
		int src, dst, move, num;
		int pieceTag, oppTag, ind, rank, file, x, y;
		
		num = 0;
		
		pieceTag = 16 + (player<<4);
		oppTag = 48 - pieceTag;
		
		// KING
		src = pieces[pieceTag];
		if (src!=0) {
			ind = 0;
			dst = kingMoves[src][ind];
			while (dst!=0) {
				if ((board[dst]&oppTag)!=0) {
					move = (src<<8)+dst;
					mlist[num++] = move;
				}
				ind++;
				dst = kingMoves[src][ind];
			}
		}
		
		// BISHOP
		for (int i=1; i<=2; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = bishopMoves[src][ind];
				while (dst!=0) {
					if ((board[dst]&oppTag)!=0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = bishopMoves[src][ind];
				}				
			}
		}
		
		// ELEPHANT
		for (int i=3; i<=4; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = elephantMoves[src][ind];				
				while (dst!=0) {
					int ee = elephantEyes[src][ind];
					if (board[ee]==0&&(board[dst]&oppTag)!=0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = elephantMoves[src][ind];
				}				
			}			
		}
		
		// ROOK		
		for (int i=5; i<=6; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				// HORIZONTAL
				
				y = rookRankCapTab[file-3][bitRanks[rank]>>3][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				y = rookRankCapTab[file-3][bitRanks[rank]>>3][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				// VERTICAL
				
				x = rookFileCapTab[rank-3][bitFiles[file]>>3][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				x = rookFileCapTab[rank-3][bitFiles[file]>>3][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
			}			
		}

		// CANNON
		for (int i=7; i<=8; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				// HORIZONTAL
				
				y = cannonRankCapTab[file-3][bitRanks[rank]>>3][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				y = cannonRankCapTab[file-3][bitRanks[rank]>>3][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				// VERTICAL
				
				x = cannonFileCapTab[rank-3][bitFiles[file]>>3][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
				x = cannonFileCapTab[rank-3][bitFiles[file]>>3][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					move = src<<8|dst;
					mlist[num++] = move;
				}
				
			}			
		}

		// KNIGHT
		for (int i=9; i<=10; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = knightMoves[src][ind];
				while (dst!=0) {
					int hleg = horseLegs[src][ind];
					if (board[hleg]==0&&(board[dst]&oppTag)!=0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = knightMoves[src][ind];
				}				
			}			
		}
		
		// PAWN
		for (int i=11; i<=15; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				dst = pawnMoves[player][src][ind];
				while (dst!=0) {
					if ((board[dst]&oppTag)!=0) {
						move = (src<<8)+dst;
						mlist[num++] = move;
					}
					ind++;
 					dst = pawnMoves[player][src][ind];
				}				
			}			
		}
		
		return num;
	}
	
	int boardRepeated() {
		if (lockHash[(int)(zobristLock&0xffff)]==0) return 0;		
		boolean opp_check = true;
		boolean me_check = true;
		for (int index=moveNum-1; index>0; index-=2) {
			if (opp_check&&(moveList[index]&0xff000000)==0) opp_check = false;
			if (me_check&&(moveList[index-1]&0xff000000)==0) me_check = false;
			
			if ((moveList[index]&0xffff)==NULL_MOVE||(moveList[index]&0xff0000)!=0
				||(moveList[index-1]&0xffff)==NULL_MOVE||(moveList[index-1]&0xff0000)!=0) break;
			
			if (lockHist[index-1]==zobristLock) 
				return 1 + (me_check? 2:0) + (opp_check? 4:0);
		}
		return 0;
	}
	
}
