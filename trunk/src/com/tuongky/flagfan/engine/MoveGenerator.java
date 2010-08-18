package com.tuongky.flagfan.engine;

import java.util.Arrays;

import static com.tuongky.flagfan.engine.Position.*;

public class MoveGenerator {

	final static int kingMoveTab[]		= {-0x10, -0x01, +0x01, +0x10};
	final static int bishopMoveTab[]	= {-0x11, -0x0f, +0x0f, +0x11};
	final static int elephantMoveTab[]	= {-0x22, -0x1e, +0x1e, +0x22};
	final static int knightMoveTab[]	= {-0x21, -0x1f, -0x12, -0x0e, +0x0e, +0x12, +0x1f, +0x21};
	
	final static int[] horseLegTab = 
	{
	                               0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,-16,  0,-16,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0, 16,  0, 16,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0
	};	

	final static int[] legalMoveTab =
	{
	                       0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	  0, 0, 0, 0, 0, 0, 0
	};

	int rookRankNoCapTab[][][] 	= new int[FILES][1<<FILES][2];
	int rookRankCapTab[][][]	= new int[FILES][1<<FILES][2];
	int cannonRankCapTab[][][]	= new int[FILES][1<<FILES][2];

	int rookFileNoCapTab[][][] 	= new int[RANKS][1<<RANKS][2];
	int rookFileCapTab[][][]	= new int[RANKS][1<<RANKS][2];
	int cannonFileCapTab[][][]	= new int[RANKS][1<<RANKS][2];
	
	int rookRankNoCapMask[][]	= new int[FILES][1<<FILES];
	int rookRankCapMask[][]		= new int[FILES][1<<FILES];
	int cannonRankCapMask[][]	= new int[FILES][1<<FILES];

	int rookFileNoCapMask[][]	= new int[RANKS][1<<RANKS];
	int rookFileCapMask[][]		= new int[RANKS][1<<RANKS];
	int cannonFileCapMask[][]	= new int[RANKS][1<<RANKS];
	
	
	boolean[] inBoard 			= new boolean[BOARD_SIZE];
	boolean[] inPalace 			= new boolean[BOARD_SIZE];
	
	int[][] elephantEyes		= new int[BOARD_SIZE][5];
	int[][] horseLegs			= new int[BOARD_SIZE][10];
	
	int[][][] pawnMoves 		= new int[BOARD_SIZE][2][5];
	int[][] bishopMoves 		= new int[BOARD_SIZE][5];
	int[][] elephantMoves 		= new int[BOARD_SIZE][5];
	int[][] knightMoves			= new int[BOARD_SIZE][10];
	int[][] kingMoves			= new int[BOARD_SIZE][5];
	
	private static MoveGenerator instance = null;
	
	void initInBoard() {
		Arrays.fill(inBoard, false);
		for (int i=3; i<3+RANKS; i++)
		for (int j=3; j<3+FILES; j++) inBoard[(i<<4)+j] = true;
	}
	
	void initInPalace() {
		Arrays.fill(inPalace, false);		
		for (int j=6; j<=8; j++) {
			for (int i=3; i<=5; i++) inPalace[(i<<4)+j] = true;
			for (int i=10; i<=12; i++) inPalace[(i<<4)+j] = true;
		}		
	}
	
	void initKingBishopMoves() {
		// King & Bishop
		int src, dst, cc;		
		for (src=0; src<BOARD_SIZE; src++) {
			Arrays.fill(kingMoves[src], 0);
			Arrays.fill(bishopMoves[src], 0);
			if (inPalace[src]) {
				cc = 0;
				for (int i=0; i<kingMoveTab.length; i++) {
					dst = src + kingMoveTab[i];
					if (inPalace[dst]) kingMoves[src][cc++] = dst;
				}
				cc = 0;
				for (int i=0; i<bishopMoveTab.length; i++) {
					dst = src + bishopMoveTab[i];
					if (inPalace[dst]) bishopMoves[src][cc++] = dst;
				}
			}
		}
	}
	
	void initElephanKnightPawnMoves() {
		// Elephant, Knight & Pawn		
		int src, dst, cc;		
		for (src=0; src<BOARD_SIZE; src++) {
			Arrays.fill(elephantMoves[src], 0);
			Arrays.fill(knightMoves[src], 0);
			Arrays.fill(pawnMoves[src][0], 0);
			Arrays.fill(pawnMoves[src][1], 0);
			if (inBoard[src]) {
				cc = 0;
				for (int i=0; i<elephantMoveTab.length; i++) {
					dst = src + elephantMoveTab[i];
					if (inBoard[dst]&&((src^dst)&0x80)==0) {
						elephantMoves[src][cc] = dst;
						elephantEyes[src][cc] = (src+dst) >> 1;
						cc++;
					}
				}
				
				cc = 0;
				for (int i=0; i<knightMoveTab.length; i++) {
					dst = src + knightMoveTab[i];
					if (inBoard[dst]) {
						knightMoves[src][cc] = dst;
						horseLegs[src][cc] = src + horseLegTab[dst-src+BOARD_SIZE];
						cc++;
					}
				}
				
				for (int i=0; i<2; i++) {
					cc = 0;
					if (i==0) dst = src - 16; else dst = src + 16;
					if (inBoard[dst]) pawnMoves[src][i][cc++] = dst;
					
					if (i==0 ? (src&0x80)==0 : (src&0x80)!=0) {
						for (int j=-1; j<=1; j+=2) {
							dst = src + j;
							if (inBoard[dst]) pawnMoves[src][i][cc++] = dst;
						}
					}
				}
			}
		}
	}

	void initRookCannonMoves() {
		// Rook & Cannon
		
		// RANK
		for (int i=0; i<FILES; i++)
		for (int j=0; j<1<<FILES; j++) {
			rookRankNoCapTab[i][j][0] = rookRankNoCapTab[i][j][1] = i+3;
			rookRankCapTab[i][j][0] = rookRankCapTab[i][j][1] = i+3;
			cannonRankCapTab[i][j][0] = cannonRankCapTab[i][j][1] = i+3;
			rookRankNoCapMask[i][j] = rookRankCapMask[i][j] = cannonRankCapMask[i][j] = 0;
			
			int k;

			for (k=i+1; k<FILES; k++) {
				if ((j>>k&1)!=0) {
					rookRankCapTab[i][j][0] = k+3;
					rookRankCapMask[i][j] |= 1<<(k+3);
					break;
				}
				rookRankNoCapTab[i][j][0] = k+3;
				rookRankNoCapMask[i][j] |= 1<<(k+3);
			}
			
			for (k++; k<FILES; k++)
			if ((j>>k&1)!=0) {
				cannonRankCapTab[i][j][0] = k+3;
				cannonRankCapMask[i][j] |= 1<<(k+3);
				break;
			}

			for (k=i-1; k>=0; k--) {
				if ((j>>k&1)!=0) {
					rookRankCapTab[i][j][1] = k+3;
					rookRankCapMask[i][j] |= 1<<(k+3);
					break;
				}
				rookRankNoCapTab[i][j][1] = k+3;
				rookRankNoCapMask[i][j] |= 1<<(k+3);
			}
			
			for (k--; k>=0; k--)
			if ((j>>k&1)!=0) {
				cannonRankCapTab[i][j][1] = k+3;
				cannonRankCapMask[i][j] |= 1<<(k+3);
				break;
			}
			
		}

		// FILE
		for (int i=0; i<RANKS; i++)
		for (int j=0; j<1<<RANKS; j++) {
			rookFileNoCapTab[i][j][0] = rookFileNoCapTab[i][j][1] = i+3;
			rookFileCapTab[i][j][0] = rookFileCapTab[i][j][1] = i+3;
			cannonFileCapTab[i][j][0] = cannonFileCapTab[i][j][1] = i+3;
			rookFileNoCapMask[i][j] = rookFileCapMask[i][j] = cannonFileCapMask[i][j] = 0;
			
			int k;

			for (k=i+1; k<RANKS; k++) {
				if ((j>>k&1)!=0) {
					rookFileCapTab[i][j][0] = k+3;
					rookFileCapMask[i][j] |= 1<<(k+3);
					break;
				}
				rookFileNoCapTab[i][j][0] = k+3;
				rookFileNoCapMask[i][j] |= 1<<(k+3);
			}
			
			for (k++; k<RANKS; k++)
			if ((j>>k&1)!=0) {
				cannonFileCapTab[i][j][0] = k+3;
				cannonFileCapMask[i][j] |= 1<<(k+3);
				break;
			}

			for (k=i-1; k>=0; k--) {
				if ((j>>k&1)!=0) {
					rookFileCapTab[i][j][1] = k+3;
					rookFileCapMask[i][j] |= 1<<(k+3);
					break;
				}
				rookFileNoCapTab[i][j][1] = k+3;
				rookFileNoCapMask[i][j] |= 1<<(k+3);
			}
			
			for (k--; k>=0; k--)
			if ((j>>k&1)!=0) {
				cannonFileCapTab[i][j][1] = k+3;
				cannonFileCapMask[i][j] |= 1<<(k+3);
				break;
			}
			
		}		
	}
	
	void initAll() {
		initInBoard();
		initInPalace();
		initKingBishopMoves();
		initElephanKnightPawnMoves();
		initRookCannonMoves();
	}
	
	private MoveGenerator() {
		initAll();
	}
	
	public static MoveGenerator getInstance() {
		if (instance==null) instance = new MoveGenerator();
		return instance;
	}

	public int genAllMoves(Position p, int[] moveList) {
		int[] board = p.board;
		int[] pieces = p.pieces;
		int[] moves;
		int src, dst, num;
		int pieceTag, ind, rank, file, x, y;
		
		num = 0;
		
		pieceTag = 16 + (p.turn<<4);
		
		// KING
		src = pieces[pieceTag];
		moves = kingMoves[src];
		if (src!=0) {
			ind = 0;
			dst = moves[ind];
			while (dst!=0) {
				if ((board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				ind++;
				dst = moves[ind];
			}
		}
		
		// BISHOP
		for (int i=1; i<=2; i++) {
			src = pieces[pieceTag+i];
			moves = bishopMoves[src];
			if (src!=0) {
				ind = 0;
				dst = moves[ind];
				while (dst!=0) {
					if ((board[dst]&pieceTag)==0) {
						moveList[num++] = src<<8|dst;
					}
					ind++;
 					dst = moves[ind];
				}				
			}
		}
		
		// ELEPHANT
		for (int i=3; i<=4; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				moves = elephantMoves[src];
				dst = moves[ind];				
				while (dst!=0) {
					int ee = elephantEyes[src][ind];
					if (board[ee]==0&&(board[dst]&pieceTag)==0) {
						moveList[num++] = src<<8|dst;
					}
					ind++;
 					dst = moves[ind];
				}				
			}			
		}
		
		// ROOK		
		for (int i=5; i<=6; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				int bitRank, bitFile;
				bitRank = p.bitRanks[rank]>>3;
				bitFile = p.bitFiles[file]>>3;
				
				// HORIZONTAL
				
				y = rookRankNoCapTab[file-3][bitRank][0];
				while (y>file) {
					moveList[num++] = src<<8|rank<<4|y;
					y--;
				}

				y = rookRankNoCapTab[file-3][bitRank][1];
				while (y<file) {
					moveList[num++] = src<<8|rank<<4|y;
					y++;
				}
				
				y = rookRankCapTab[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = rookRankCapTab[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = rookFileNoCapTab[rank-3][bitFile][0];
				while (x>rank) {
					moveList[num++] = src<<8|x<<4|file;
					x--;
				}

				x = rookFileNoCapTab[rank-3][bitFile][1];
				while (x<rank) {
					moveList[num++] = src<<8|x<<4|file;
					x++;
				}
				
				x = rookFileCapTab[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = rookFileCapTab[rank-3][bitFile][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
			}			
		}

		// CANNON
		for (int i=7; i<=8; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				
				rank = src >> 4;
				file = src & 0xf;
				
				int bitRank, bitFile;
				bitRank = p.bitRanks[rank]>>3;
				bitFile = p.bitFiles[file]>>3;
				
				// HORIZONTAL
				
				y = rookRankNoCapTab[file-3][bitRank][0];
				while (y>file) {
					moveList[num++] = src<<8|rank<<4|y;
					y--;
				}

				y = rookRankNoCapTab[file-3][bitRank][1];
				while (y<file) {
					moveList[num++] = src<<8|rank<<4|y;
					y++;
				}
				
				y = cannonRankCapTab[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = cannonRankCapTab[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = rookFileNoCapTab[rank-3][bitFile][0];
				while (x>rank) {
					moveList[num++] = src<<8|x<<4|file;
					x--;
				}

				x = rookFileNoCapTab[rank-3][bitFile][1];
				while (x<rank) {
					moveList[num++] = src<<8|x<<4|file;
					x++;
				}
				
				x = cannonFileCapTab[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = cannonFileCapTab[rank-3][bitFile][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
			}			
		}

		// KNIGHT
		for (int i=9; i<=10; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				moves = knightMoves[src];
				dst = moves[ind];
				while (dst!=0) {
					int hleg = horseLegs[src][ind];
					if (board[hleg]==0&&(board[dst]&pieceTag)==0) {
						moveList[num++] = (src<<8)+dst;
					}
					ind++;
 					dst = moves[ind];
				}				
			}			
		}
		
		// PAWN
		for (int i=11; i<=15; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				moves = pawnMoves[src][p.turn]; 
				dst = moves[ind];
				while (dst!=0) {
					if ((board[dst]&pieceTag)==0) {
						moveList[num++] = (src<<8)+dst;
					}
					ind++;
 					dst = moves[ind];
				}				
			}			
		}
		
		return num;
	}

	public boolean isChecked(Position p, int side) {
		int[] board = p.board;
		int[] pieces = p.pieces;
		int[] bitFiles = p.bitFiles;
		int[] bitRanks = p.bitRanks;
		int src, dst, rank, file, pieceTag, y;
		
		pieceTag = 32 - (side<<4);
		src = pieces[48-pieceTag]; // KING
		
		rank = src >> 4;
		file = src & 0xf;

		// KING kills KING
		dst = pieces[pieceTag];
		if (dst!=0) {
			y = dst & 0xf;
			if (y==file&&(rookFileCapMask[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
		}
		
		// ROOK kills KING
		for (int i=5; i<=6; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((rookRankCapMask[file-3][bitRanks[rank]>>3]&(1<<(dst&0xf)))!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((rookFileCapMask[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
				}
			}
		}
		
	
		// CANNON kills KING
		for (int i=7; i<=8; i++) {
			dst = pieces[pieceTag+i];
			if (dst!=0) {
				if ((dst>>4)==rank) {
					if ((cannonRankCapMask[file-3][bitRanks[rank]>>3]&(1<<(dst&0xf)))!=0) return true;
				} else
				if ((dst&0xf)==file) {
					if ((cannonFileCapMask[rank-3][bitFiles[file]>>3]&(1<<(dst>>4)))!=0) return true;
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
		if ((ptmp&pieceTag)!=0&&Position.PIECE_TYPES[ptmp]==6) return true;

		ptmp = board[src+1];
		if ((ptmp&pieceTag)!=0&&Position.PIECE_TYPES[ptmp]==6) return true;
		
		ptmp = board[src-16+(side<<5)];
		if ((ptmp&pieceTag)!=0&&Position.PIECE_TYPES[ptmp]==6) return true;
		
		return false;
	}
	
}
