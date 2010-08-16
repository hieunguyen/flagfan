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
	
}
