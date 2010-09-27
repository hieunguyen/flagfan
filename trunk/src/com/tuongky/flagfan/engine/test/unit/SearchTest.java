package com.tuongky.flagfan.engine.test.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.tuongky.flagfan.engine.Position;
import com.tuongky.flagfan.engine.Search;
import com.tuongky.utils.FENException;
import com.tuongky.utils.Misc;

public class SearchTest {

	String findBestMove(String fen) throws FENException {
		Position p = new Position(fen);
		Search search = new Search(p);
		search.setTimeLimit(5*1000);
		int move = search.findBestMove();
		return Misc.moveForHuman(p, move);		
	}
	
	@Test
	public void test1() throws FENException {
		String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
		String expected = "H8+7";
		String actual = findBestMove(fen);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test2() throws FENException {
		String fen = "r1eakae2/5r3/h3c1hc1/p1C1p3C/6p2/9/P1P1P1P1P/4E4/R4H3/1HEAKA2R b 0 7";
		String expected = "C8+6";
		String actual = findBestMove(fen);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test3() throws FENException {
		String fen = "3aka3/9/2hce1h1e/rR2p3p/6p2/2C4R1/4P1P1P/C1r1E1H2/9/2EAKA3 w"; // X8-3 or X8+2
		String expected = "R8+2";
		String actual = findBestMove(fen);
		Assert.assertEquals(expected, actual);
	}

}
