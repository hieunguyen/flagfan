package com.tuongky.utils;

import static com.tuongky.flagfan.engine.Constants.PIECE_LETTERS;
import static com.tuongky.flagfan.engine.Constants.PIECE_TYPES;
import static com.tuongky.flagfan.engine.Constants.RED;

import java.util.Arrays;

import com.tuongky.flagfan.engine.Position;

public class Misc {

	public static void debug(Object... os) {
		System.out.println(Arrays.deepToString(os));
	}
	
	public static void showMove(int move) {
		int src = move>>8;
		int dst = move&0xff;
		int x1, y1, x2, y2;
		x1 = (src>>4)-3;
		y1 = (src&0xf)-3;
		x2 = (dst>>4)-3;
		y2 = (dst&0xf)-3;
		System.out.println(x1+" "+y1+" "+x2+" "+y2);
	}
	
	public static String wbMove(int move) { // Winboard move style
		int src, dst;
		src = (move>>8)&0xff;
		dst = move&0xff;
		char r1, f1, r2, f2;
		r1 = (char) ('0'+9-((src>>4)-3));
		f1 = (char) ('a'+(src&0xf)-3);
		r2 = (char) ('0'+9-((dst>>4)-3));
		f2 = (char) ('a'+(dst&0xf)-3);
		String ms = ""+f1+r1+f2+r2;
		return ms;
	}
	
	public static int ffMove(String wbm) {
		int r1, f1, r2, f2;
		r1 = (wbm.charAt(1)-'0');
		f1 = (wbm.charAt(0)-'a')+3;
		r2 = (wbm.charAt(3)-'0');
		f2 = (wbm.charAt(2)-'a')+3;
		r1 = 9-r1+3; r2 = 9-r2+3;
		int src, dst;
		src = (r1<<4)+f1;
		dst = (r2<<4)+f2;
		return (src<<8)+dst;		
	}
	
	public static String moveForHuman(Position p, int move, int side) {
		int[] board = p.getBoard();
		if (move <= 0)
			return "No moves found!";
		int src, dst, r1, f1, r2, f2;
		String moveStr, dir;
		src = (move >> 8) & 0xff;
		dst = move & 0xff;
		char pc = PIECE_LETTERS.charAt(PIECE_TYPES[board[src]]);
		r1 = (src >> 4) - 2;
		f1 = (src & 0xf) - 2;
		r2 = (dst >> 4) - 2;
		f2 = (dst & 0xf) - 2;
		if (side == RED) {
			f1 = 10 - f1;
			f2 = 10 - f2;
		}
		if (r1 != r2) {
			if (side == RED ^ r2 < r1)
				dir = "-";
			else
				dir = "+";
			if (f1 == f2) {
				moveStr = "" + pc + f1 + dir + Math.abs(r1 - r2);
			} else {
				moveStr = "" + pc + f1 + dir + f2;
			}
		} else {
			dir = ".";
			moveStr = "" + pc + f1 + dir + f2;
		}
		return moveStr;
	}

	public static String moveForHuman(Position p, int move) {
		return moveForHuman(p, move, p.getTurn());
	}
	
	public static void printMoveForHuman(Position p, int move) {
		String moveStr = moveForHuman(p, move);
		System.out.println(moveStr);
	}

}
