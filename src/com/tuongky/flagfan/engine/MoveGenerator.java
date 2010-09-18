package com.tuongky.flagfan.engine;

import java.util.Arrays;

import static com.tuongky.flagfan.engine.Constants.*;

public class MoveGenerator {

	public static final int[] KING_MOVE_TAB		= {-0x10, -0x01, +0x01, +0x10};
	public static final int[] ADVISOR_MOVE_TAB	= {-0x11, -0x0f, +0x0f, +0x11};
	public static final int[] ELEPHANT_MOVE_TAB	= {-0x22, -0x1e, +0x1e, +0x22};
	public static final int[] HORSE_MOVE_TAB	= {-0x21, -0x1f, -0x12, -0x0e, +0x0e, +0x12, +0x1f, +0x21};
	
	public static final int[] HORSE_LEG_TAB = 
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

	public static final int[] REVERSE_HORSE_LEG_TAB = 
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
	   0,  0,  0,  0,  0,  0,-17,  0,-15,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0, -17, 0,  0,  0,-15,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0, 15,  0,  0,  0, 17,  0,  0,  0,  0,  0,  0,
	   0,  0,  0,  0,  0,  0, 15,  0, 17,  0,  0,  0,  0,  0,  0,  0,
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

	public static final int[] LEGAL_MOVE_TAB =
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

	public static final int ROOK_RANK_NO_CAP_TAB[][][] 	= new int[FILES][1<<FILES][2];
	public static final int ROOK_RANK_CAP_TAB[][][]		= new int[FILES][1<<FILES][2];
	public static final int CANNON_RANK_CAP_TAB[][][]	= new int[FILES][1<<FILES][2];

	public static final int ROOK_FILE_NO_CAP_TAB[][][] 	= new int[RANKS][1<<RANKS][2];
	public static final int ROOK_FILE_CAP_TAB[][][]		= new int[RANKS][1<<RANKS][2];
	public static final int CANNON_FILE_CAP_TAB[][][]	= new int[RANKS][1<<RANKS][2];
	
	public static final int ROOK_RANK_NO_CAP_MASK[][]	= new int[FILES][1<<FILES];
	public static final int ROOK_RANK_CAP_MASK[][]		= new int[FILES][1<<FILES];
	public static final int CANNON_RANK_CAP_MASK[][]	= new int[FILES][1<<FILES];

	public static final int ROOK_FILE_NO_CAP_MASK[][]	= new int[RANKS][1<<RANKS];
	public static final int ROOK_FILE_CAP_MASK[][]		= new int[RANKS][1<<RANKS];
	public static final int CANNON_FILE_CAP_MASK[][]	= new int[RANKS][1<<RANKS];
	
	
	public static final boolean[] IN_BOARD 				= new boolean[BOARD_SIZE];
	public static final boolean[] IN_PALACE 			= new boolean[BOARD_SIZE];
	public static final boolean[] ADVISOR_REACHABLE		= new boolean[BOARD_SIZE];
	public static final boolean[] ELEPHANT_REACHABLE	= new boolean[BOARD_SIZE];
	
	public static final int[][] ELEPHANT_EYES			= new int[BOARD_SIZE][5];
	public static final int[][] HORSE_LEGS				= new int[BOARD_SIZE][10];
	
	public static final int[][][] PAWN_MOVES 			= new int[BOARD_SIZE][2][5];
	public static final int[][] ADVISOR_MOVES 			= new int[BOARD_SIZE][5];
	public static final int[][] ELEPHANT_MOVES 			= new int[BOARD_SIZE][5];
	public static final int[][] HORSE_MOVES				= new int[BOARD_SIZE][10];
	public static final int[][] KING_MOVES				= new int[BOARD_SIZE][5];
	
	private static MoveGenerator instance = null;
	
	void initInBoard() {
		Arrays.fill(IN_BOARD, false);
		for (int i=3; i<3+RANKS; i++)
		for (int j=3; j<3+FILES; j++) IN_BOARD[(i<<4)+j] = true;
	}
	
	void initReachable() {
		Arrays.fill(IN_PALACE, false);		
		Arrays.fill(ADVISOR_REACHABLE, false);
		for (int j=6; j<=8; j++) {
			for (int i=3; i<=5; i++) {
				IN_PALACE[(i<<4)+j] = true;
				if ((i+j)%2==1) ADVISOR_REACHABLE[(i<<4)+j] = true;
			}
			for (int i=10; i<=12; i++) {
				IN_PALACE[(i<<4)+j] = true;
				if ((i+j)%2==0) ADVISOR_REACHABLE[(i<<4)+j] = true;
			}
		}
		Arrays.fill(ELEPHANT_REACHABLE, false);
		for (int j=3; j<=7; j+=2) {
			for (int i=3; i<=7; i+=2) {
				int u, v;
				u = (i-3)/2;
				v = (j-3)/2;
				if ((u+v)%2==1) ELEPHANT_REACHABLE[(i<<4)+j] = true;
			}
			for (int i=8; i<=12; i+=2) {
				int u, v;
				u = (i-3)/2;
				v = (j-3)/2;
				if ((u+v)%2==1) ELEPHANT_REACHABLE[(i<<4)+j] = true;
			}
		}
	}
	
	void initKingBishopMoves() {
		// King & Bishop
		int src, dst, cc;		
		for (src=0; src<BOARD_SIZE; src++) {
			Arrays.fill(KING_MOVES[src], 0);
			Arrays.fill(ADVISOR_MOVES[src], 0);
			if (IN_PALACE[src]) {
				cc = 0;
				for (int i=0; i<KING_MOVE_TAB.length; i++) {
					dst = src + KING_MOVE_TAB[i];
					if (IN_PALACE[dst]) KING_MOVES[src][cc++] = dst;
				}
				cc = 0;
				for (int i=0; i<ADVISOR_MOVE_TAB.length; i++) {
					dst = src + ADVISOR_MOVE_TAB[i];
					if (IN_PALACE[dst]) ADVISOR_MOVES[src][cc++] = dst;
				}
			}
		}
	}
	
	void initElephanKnightPawnMoves() {
		// Elephant, Knight & Pawn		
		int src, dst, cc;		
		for (src=0; src<BOARD_SIZE; src++) {
			Arrays.fill(ELEPHANT_MOVES[src], 0);
			Arrays.fill(HORSE_MOVES[src], 0);
			Arrays.fill(PAWN_MOVES[src][0], 0);
			Arrays.fill(PAWN_MOVES[src][1], 0);
			if (IN_BOARD[src]) {
				cc = 0;
				for (int i=0; i<ELEPHANT_MOVE_TAB.length; i++) {
					dst = src + ELEPHANT_MOVE_TAB[i];
					if (IN_BOARD[dst]&&((src^dst)&0x80)==0) {
						ELEPHANT_MOVES[src][cc] = dst;
						ELEPHANT_EYES[src][cc] = (src+dst) >> 1;
						cc++;
					}
				}
				
				cc = 0;
				for (int i=0; i<HORSE_MOVE_TAB.length; i++) {
					dst = src + HORSE_MOVE_TAB[i];
					if (IN_BOARD[dst]) {
						HORSE_MOVES[src][cc] = dst;
						HORSE_LEGS[src][cc] = src + HORSE_LEG_TAB[dst-src+BOARD_SIZE];
						cc++;
					}
				}
				
				for (int i=0; i<2; i++) {
					cc = 0;
					if (i==0) dst = src - 16; else dst = src + 16;
					if (IN_BOARD[dst]) PAWN_MOVES[src][i][cc++] = dst;
					
					if (i==0 ? (src&0x80)==0 : (src&0x80)!=0) {
						for (int j=-1; j<=1; j+=2) {
							dst = src + j;
							if (IN_BOARD[dst]) PAWN_MOVES[src][i][cc++] = dst;
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
			ROOK_RANK_NO_CAP_TAB[i][j][0] = ROOK_RANK_NO_CAP_TAB[i][j][1] = i+3;
			ROOK_RANK_CAP_TAB[i][j][0] = ROOK_RANK_CAP_TAB[i][j][1] = i+3;
			CANNON_RANK_CAP_TAB[i][j][0] = CANNON_RANK_CAP_TAB[i][j][1] = i+3;
			ROOK_RANK_NO_CAP_MASK[i][j] = ROOK_RANK_CAP_MASK[i][j] = CANNON_RANK_CAP_MASK[i][j] = 0;
			
			int k;

			for (k=i+1; k<FILES; k++) {
				if ((j>>k&1)!=0) {
					ROOK_RANK_CAP_TAB[i][j][0] = k+3;
					ROOK_RANK_CAP_MASK[i][j] |= 1<<(k+3);
					break;
				}
				ROOK_RANK_NO_CAP_TAB[i][j][0] = k+3;
				ROOK_RANK_NO_CAP_MASK[i][j] |= 1<<(k+3);
			}
			
			for (k++; k<FILES; k++)
			if ((j>>k&1)!=0) {
				CANNON_RANK_CAP_TAB[i][j][0] = k+3;
				CANNON_RANK_CAP_MASK[i][j] |= 1<<(k+3);
				break;
			}

			for (k=i-1; k>=0; k--) {
				if ((j>>k&1)!=0) {
					ROOK_RANK_CAP_TAB[i][j][1] = k+3;
					ROOK_RANK_CAP_MASK[i][j] |= 1<<(k+3);
					break;
				}
				ROOK_RANK_NO_CAP_TAB[i][j][1] = k+3;
				ROOK_RANK_NO_CAP_MASK[i][j] |= 1<<(k+3);
			}
			
			for (k--; k>=0; k--)
			if ((j>>k&1)!=0) {
				CANNON_RANK_CAP_TAB[i][j][1] = k+3;
				CANNON_RANK_CAP_MASK[i][j] |= 1<<(k+3);
				break;
			}
			
		}

		// FILE
		for (int i=0; i<RANKS; i++)
		for (int j=0; j<1<<RANKS; j++) {
			ROOK_FILE_NO_CAP_TAB[i][j][0] = ROOK_FILE_NO_CAP_TAB[i][j][1] = i+3;
			ROOK_FILE_CAP_TAB[i][j][0] = ROOK_FILE_CAP_TAB[i][j][1] = i+3;
			CANNON_FILE_CAP_TAB[i][j][0] = CANNON_FILE_CAP_TAB[i][j][1] = i+3;
			ROOK_FILE_NO_CAP_MASK[i][j] = ROOK_FILE_CAP_MASK[i][j] = CANNON_FILE_CAP_MASK[i][j] = 0;
			
			int k;

			for (k=i+1; k<RANKS; k++) {
				if ((j>>k&1)!=0) {
					ROOK_FILE_CAP_TAB[i][j][0] = k+3;
					ROOK_FILE_CAP_MASK[i][j] |= 1<<(k+3);
					break;
				}
				ROOK_FILE_NO_CAP_TAB[i][j][0] = k+3;
				ROOK_FILE_NO_CAP_MASK[i][j] |= 1<<(k+3);
			}
			
			for (k++; k<RANKS; k++)
			if ((j>>k&1)!=0) {
				CANNON_FILE_CAP_TAB[i][j][0] = k+3;
				CANNON_FILE_CAP_MASK[i][j] |= 1<<(k+3);
				break;
			}

			for (k=i-1; k>=0; k--) {
				if ((j>>k&1)!=0) {
					ROOK_FILE_CAP_TAB[i][j][1] = k+3;
					ROOK_FILE_CAP_MASK[i][j] |= 1<<(k+3);
					break;
				}
				ROOK_FILE_NO_CAP_TAB[i][j][1] = k+3;
				ROOK_FILE_NO_CAP_MASK[i][j] |= 1<<(k+3);
			}
			
			for (k--; k>=0; k--)
			if ((j>>k&1)!=0) {
				CANNON_FILE_CAP_TAB[i][j][1] = k+3;
				CANNON_FILE_CAP_MASK[i][j] |= 1<<(k+3);
				break;
			}
			
		}		
	}
	
	void initAll() {
		initInBoard();
		initReachable();
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
		moves = KING_MOVES[src];
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
		
		// ADVISOR
		for (int i=1; i<=2; i++) {
			src = pieces[pieceTag+i];
			moves = ADVISOR_MOVES[src];
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
				moves = ELEPHANT_MOVES[src];
				dst = moves[ind];				
				while (dst!=0) {
					int ee = ELEPHANT_EYES[src][ind];
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
				
				y = ROOK_RANK_NO_CAP_TAB[file-3][bitRank][0];
				while (y>file) {
					moveList[num++] = src<<8|rank<<4|y;
					y--;
				}

				y = ROOK_RANK_NO_CAP_TAB[file-3][bitRank][1];
				while (y<file) {
					moveList[num++] = src<<8|rank<<4|y;
					y++;
				}
				
				y = ROOK_RANK_CAP_TAB[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = ROOK_RANK_CAP_TAB[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = ROOK_FILE_NO_CAP_TAB[rank-3][bitFile][0];
				while (x>rank) {
					moveList[num++] = src<<8|x<<4|file;
					x--;
				}

				x = ROOK_FILE_NO_CAP_TAB[rank-3][bitFile][1];
				while (x<rank) {
					moveList[num++] = src<<8|x<<4|file;
					x++;
				}
				
				x = ROOK_FILE_CAP_TAB[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = ROOK_FILE_CAP_TAB[rank-3][bitFile][1];
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
				
				y = ROOK_RANK_NO_CAP_TAB[file-3][bitRank][0];
				while (y>file) {
					moveList[num++] = src<<8|rank<<4|y;
					y--;
				}

				y = ROOK_RANK_NO_CAP_TAB[file-3][bitRank][1];
				while (y<file) {
					moveList[num++] = src<<8|rank<<4|y;
					y++;
				}
				
				y = CANNON_RANK_CAP_TAB[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = CANNON_RANK_CAP_TAB[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = ROOK_FILE_NO_CAP_TAB[rank-3][bitFile][0];
				while (x>rank) {
					moveList[num++] = src<<8|x<<4|file;
					x--;
				}

				x = ROOK_FILE_NO_CAP_TAB[rank-3][bitFile][1];
				while (x<rank) {
					moveList[num++] = src<<8|x<<4|file;
					x++;
				}
				
				x = CANNON_FILE_CAP_TAB[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&pieceTag)==0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = CANNON_FILE_CAP_TAB[rank-3][bitFile][1];
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
				moves = HORSE_MOVES[src];
				dst = moves[ind];
				while (dst!=0) {
					int hleg = HORSE_LEGS[src][ind];
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
				moves = PAWN_MOVES[src][p.turn]; 
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

	public int genCaptureMoves(Position p, int[] moveList) {
		int[] board = p.board;
		int[] pieces = p.pieces;
		int[] moves;
		int src, dst, num;
		int pieceTag, oppTag, ind, rank, file, x, y;
		
		num = 0;
		
		pieceTag = 16 + (p.turn<<4);
		oppTag = 48 - pieceTag;
		
		// KING
		src = pieces[pieceTag];
		moves = KING_MOVES[src];
		if (src!=0) {
			ind = 0;
			dst = moves[ind];
			while (dst!=0) {
				if ((board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				ind++;
				dst = moves[ind];
			}
		}
		
		// ADVISOR
		for (int i=1; i<=2; i++) {
			src = pieces[pieceTag+i];
			moves = ADVISOR_MOVES[src];
			if (src!=0) {
				ind = 0;
				dst = moves[ind];
				while (dst!=0) {
					if ((board[dst]&oppTag)!=0) {
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
				moves = ELEPHANT_MOVES[src];
				dst = moves[ind];				
				while (dst!=0) {
					int ee = ELEPHANT_EYES[src][ind];
					if (board[ee]==0&&(board[dst]&oppTag)!=0) {
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
				
				y = ROOK_RANK_CAP_TAB[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = ROOK_RANK_CAP_TAB[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = ROOK_FILE_CAP_TAB[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = ROOK_FILE_CAP_TAB[rank-3][bitFile][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
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
				
				y = CANNON_RANK_CAP_TAB[file-3][bitRank][0];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				y = CANNON_RANK_CAP_TAB[file-3][bitRank][1];
				dst = rank<<4|y;
				if (y!=file&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				// VERTICAL
				
				x = CANNON_FILE_CAP_TAB[rank-3][bitFile][0];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
				x = CANNON_FILE_CAP_TAB[rank-3][bitFile][1];
				dst = x<<4|file;
				if (x!=rank&&(board[dst]&oppTag)!=0) {
					moveList[num++] = src<<8|dst;
				}
				
			}			
		}

		// KNIGHT
		for (int i=9; i<=10; i++) {
			src = pieces[pieceTag+i];
			if (src!=0) {
				ind = 0;
				moves = HORSE_MOVES[src];
				dst = moves[ind];
				while (dst!=0) {
					int hleg = HORSE_LEGS[src][ind];
					if (board[hleg]==0&&(board[dst]&oppTag)!=0) {
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
				moves = PAWN_MOVES[src][p.turn]; 
				dst = moves[ind];
				while (dst!=0) {
					if ((board[dst]&oppTag)!=0) {
						moveList[num++] = (src<<8)+dst;
					}
					ind++;
 					dst = moves[ind];
				}				
			}			
		}
		
		return num;
	}

}
