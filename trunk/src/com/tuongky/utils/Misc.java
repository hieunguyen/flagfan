package com.tuongky.utils;

import java.util.Arrays;

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
	
}
