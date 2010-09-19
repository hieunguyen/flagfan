package com.tuongky.flagfan.engine.test;

import static com.tuongky.flagfan.engine.Constants.*;
import static com.tuongky.flagfan.engine.Evaluator.*;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.tuongky.flagfan.engine.Position;
import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.Misc;


public class PositionTest {

	static final String fen = "1reak4/4ah1h1/4e4/p2cp1p1p/9/2E2HPH1/P4c2P/6C1E/3RA1C2/4K4 w 8 1";
	
	private Position p;
	
	@Before
	public void setUp() throws FENException {
		p = new Position(fen);
	}
	
	@Test(timeout = 5)
	public void testSEE() {
		int move = Misc.ffMove("h4g6");
//		int move = Misc.ffMove("f4e6");
		int actual = p.see(move);
		int expected = 100;
		Assert.assertEquals(expected, actual);
	}
	
//	@Test
	public void testAttackers() {
		int[] ps = new int[32];
		int square = Misc.ff2sqr("b9"); 
		int num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(2, num);
		
		square = Misc.ff2sqr("e7"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(2, num);
		
		square = Misc.ff2sqr("e8"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(2, num);
		
		square = Misc.ff2sqr("a7"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(4, num);
		
		square = Misc.ff2sqr("i2"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(4, num);
		
		square = Misc.ff2sqr("e1"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(2, num);
		
		square = Misc.ff2sqr("c2"); 
		num = p.attackers(square, ps);
		for (int i=0; i<num; i++) {
			System.out.println(Misc.sqr2ff(p.getPieces()[ps[i]]));
		}
		System.out.println();
		Assert.assertEquals(1, num);
	}
	
}
