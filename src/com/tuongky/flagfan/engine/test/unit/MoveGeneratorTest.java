package com.tuongky.flagfan.engine.test.unit;

import static com.tuongky.flagfan.engine.Constants.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.tuongky.flagfan.engine.MoveGenerator;
import com.tuongky.flagfan.engine.Position;
import com.tuongky.utils.FENException;

public class MoveGeneratorTest {

	
	private MoveGenerator mg;
	
	@Before
	public void setUp() throws FENException {
		mg = MoveGenerator.getInstance();
	}
	
	@Test
	public void testGenAllMoves() throws FENException {
		String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
		Position p = new Position(fen);
		int[] moveList = new int[MAX_WIDTH];
		int num = mg.genAllMoves(p, moveList);
		Assert.assertEquals(44, num);
	}
	
	@Test
	public void testGenCaptureMoves() throws FENException {
		String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
		Position p = new Position(fen);
		int[] moveList = new int[MAX_WIDTH];
		int num = mg.genCaptureMoves(p, moveList);
		Assert.assertEquals(2, num);
	}
	
	@Test
	public void testGenNonCaptureMoves() throws FENException {
		String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
		Position p = new Position(fen);
		int[] moveList = new int[MAX_WIDTH];
		int num = mg.genNonCaptureMoves(p, moveList);
		Assert.assertEquals(42, num);
	}
	
}
