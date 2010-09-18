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

	private Position p;
	
	@Before
	public void setUp() throws FENException {
		p = new Position(FEN.START_FEN);
	}
	
	@Test(timeout = 1000)
	public void testCase1() {
		int move = Misc.ffMove("h2h9");
		int actual = p.see(move);
		int expected = PIECE_VALUES[HORSE]-PIECE_VALUES[CANNON];
		Assert.assertEquals(expected, actual);
	}
	
}
