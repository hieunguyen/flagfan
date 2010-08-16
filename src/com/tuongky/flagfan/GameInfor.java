package com.tuongky.flagfan;

import java.util.*;

public class GameInfor {

	final static int KING		= 1;
	final static int BISHOP		= 2;
	final static int ELEPHANT	= 3;
	final static int ROOK		= 4;
	final static int CANNON		= 5;
	final static int KNIGHT		= 6;
	final static int PAWN		= 7;
	
	final static int RED		= 0;
	final static int BLACK		= 1;

	final static int HUMAN		= 0;
	final static int COMPUTER	= 1;

	final static int BOARD_SIZE	= 90;

//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0,-1,-2, 0, 0, 0,
//		 0, 0, 0, 0, 0, 7, 0, 0, 0,		 
//		-3, 0, 0, 0, 0,-2, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 5, 0,		 
//		 0, 0,-3, 0,-7, 0, 0, 0,-7,		 
//		 0, 6, 0,-6,-6, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 7,		 
//		 0, 0, 5, 0, 3, 0, 0, 0, 0,
//		 0, 0, 0, 0, 2, 0, 0, 0, 0,		 
//		 0, 0, 3, 2, 1, 0, 0,-5, 0,
//	};
	
//	final static int[] INIT_STATE = {
//		 0, 0,-3,-2,-1,-2, 0, 0, 0,
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0,-4, 0, 0, 0,		 
//		-7, 6,-7,-6,-4, 0, 0, 0,-7,		 
//		 0, 0, 0, 4, 0, 0, 0, 0, 7,		 
//		 7, 6, 0, 0, 0, 0,-7, 0, 0,		 
//		 0, 0, 7, 5, 0, 0,-6, 0, 0,		 
//		 0, 5, 0, 0, 0, 0, 0, 0, 0,
//		 0, 0, 0, 0, 2, 0, 0, 0, 0,		 
//		 0, 0, 3, 0, 1, 2, 0, 0, 0,
//	};

//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,
//		 0, 0, 0, 0,-1, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 5,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 3, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 3, 0, 0, 0, 0, 2, 0, 0, 0,
//		 0, 0, 0, 1, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,
//	};
	
//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0,-1, 0, 0, 0, 0,
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0,-2, 0,-2, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 3, 0, 5, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 3, 0, 0, 0, 0,
//		 0, 0, 0, 0, 2, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 1, 0, 0, 0, 0,
//	};
	
//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0,-1, 0, 0, 0, 0,
//		 0, 0, 0,-5,-2, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 6, 0, 0, 0, 6, 0, 0, 0,		 
//		 7, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 3, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0,-6, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,
//		 0, 0, 0, 0, 1, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,
//	};

//	final static int[] INIT_STATE = {
//		 0, 0, 0,-2,-1, 0,-3, 6, 0,
//		 0, 0, 0, 0,-2, 0, 0, 0, 0,		 
//		 0, 0, 0, 0,-3, 0, 0, 0, 0,		 
//		 0, 0, 0, 6, 0, 0, 0, 0, 0,		 
//		-7, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 0, 0,-7, 0,-7,		 
//		 0, 0, 0, 0, 0,-6,-5, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,
//		 0, 0, 0, 0, 2, 0, 0, 0, 0,		 
//		 0, 0, 3, 2, 0, 1, 0, 0, 0,
//	};

//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0,-1,-2,-3, 0, 0,
//		 0, 0, 0, 0,-2, 4, 0, 0, 0,		 
//		 0,-5, 0, 0,-3, 0,-6, 0,-4,		 
//		-7, 0, 0, 0,-7, 0, 0, 0,-7,		 
//		 0, 0,-7,-6, 0, 0,-7,-5, 0,		 
//		 0, 0, 0, 0, 7, 0, 0, 0, 0,		 
//		 7, 0,-4, 0, 0, 0, 7, 0, 7,		 
//		 0, 0, 6, 0, 0, 0, 6, 0, 5,		 
//		 0, 5, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 3, 2, 1, 2, 3, 4, 0,
//	};
	
//	final static int[] INIT_STATE = {
//		 0, 4,-3,-4,-1,-2,-3, 0, 0,
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 5, 0, 0, 0, 0,-2, 0,-5, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 7, 0,-7, 0, 0,-6,-7, 0, 0,		 
//		 0, 0, 0, 0, 5, 0, 0, 0, 0,		 
//		 0, 0, 0, 0, 7, 0, 7, 0, 7,		 
//		 0, 0, 0, 0, 0, 0, 6, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 3, 2, 1, 2, 3, 0, 0,
//	};

//	final static int[] INIT_STATE = {
//		 0, 0, 0, 0, 0,-2,-3, 0, 0,
//		 0, 0, 0,-4, 0,-1, 0, 0, 0,		 
//		 5, 4, 0, 0,-3,-2, 0,-5, 0,		 
//		 0, 0, 0, 0, 5, 0, 0, 0, 0,		 
//		 7, 0,-7, 0, 0,-6,-7, 0, 0,		 
//		 0, 0, 0, 0, 0,-6, 0, 0, 0,		 
//		 0, 0, 0, 0, 7, 0, 7, 0, 7,		 
//		 0, 0, 0, 0, 0, 0, 6, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0, 0, 3, 2, 1, 2, 3, 0, 0,
//	};

	final static int[] INIT_STATE = {
		-4,-6,-3,-2,-1,-2,-3,-6,-4,
		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
		 0,-5, 0, 0, 0, 0, 0,-5, 0,		 
		-7, 0,-7, 0,-7, 0,-7, 0,-7,		 
		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
		 7, 0, 7, 0, 7, 0, 7, 0, 7,		 
		 0, 5, 0, 0, 0, 0, 0, 5, 0,		 
		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
		 4, 6, 3, 2, 1, 2, 3, 6, 4,
	};

//	final static int[] INIT_STATE = {
//		-4, 0,-3,-2,-1,-2,-3, 0,-4,
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 0,-5,-6, 0, 0, 0,-6, 0,-5,		 
//		-7, 0, 0, 0,-7, 0, 0, 0,-7,		 
//		 0, 0,-7, 0, 0, 0,-7, 0, 0,		 
//		 0, 0, 0, 0, 0, 0, 0, 0, 0,		 
//		 7, 0, 7, 0, 7, 0, 7, 0, 7,		 
//		 0, 5, 6, 0, 0, 0, 6, 5, 0,		 
//		 0, 0, 0, 4, 0, 0, 0, 0, 4,		 
//		 0, 0, 3, 2, 1, 2, 3, 0, 0,
//	};

	FlagFanMain xqm;
	Stack <Integer> moveHistory;
	int[] state;
	int side;
	
	public GameInfor(FlagFanMain xqm) {
		this.xqm = xqm;
		moveHistory = new Stack <Integer> ();
		side = RED;
		state = new int[256];
		for (int i=0; i<BOARD_SIZE; i++) {
			int rank, file, sqr;
			rank = i/9; file = i%9;
			sqr = (rank+3)<<4|(file+3);
			state[sqr] = INIT_STATE[i];
		}
	}
	
	public void makeMove(int move) {
		xqm.comp1.p.makeMove(move);
		xqm.comp2.p.makeMove(move);
		side = 1-side;
		int src, dst, captured;
		src = (move>>8)&0xff;
		dst = move&0xff;
		captured = Math.abs(state[dst]);
		move|=captured<<16;
		moveHistory.push(move);
		state[dst] = state[src];
		state[src] = 0;
	}
	
	public void undoMakeMove() {
		xqm.comp1.p.undoMakeMove();
		xqm.comp2.p.undoMakeMove();
		int move = moveHistory.pop();
		int src, dst, captured;
		src = (move>>8)&0xff;
		dst = move&0xff;
		captured = (move>>16)&0xff;
		state[src] = state[dst];
		if (state[src]>0) captured =- captured;
		state[dst] = captured;
	}
	
	public boolean isValidMove(int move) {
		boolean res = false;		
		if (side==RED) {
			if (xqm.comp1.p.legalMove(move)) {
				res = xqm.comp1.p.makeMove(move);
				if (res) xqm.comp1.p.undoMakeMove();
			}
		}
		else {
			if (xqm.comp2.p.legalMove(move)) {
				res = xqm.comp2.p.makeMove(move);
				if (res) xqm.comp2.p.undoMakeMove();
			}
		}
//		System.out.println("Valid = "+res);
		return res;
	}
	
	public static void moveInfo(int move) {
		int src, dst;
		src = (move>>8)&0xff;
		dst = move&0xff;
		int r1, f1, r2, f2;
		r1 = src>>4&0xf; f1 = src&0xf;
		r2 = dst>>4&0xf; f2 = dst&0xf;
		r1-=3; f1-=3; r2-=3; f2-=3;
		System.out.println(move+": "+r1+" "+f1+" -> "+r2+" "+f2);
	}
	
}
