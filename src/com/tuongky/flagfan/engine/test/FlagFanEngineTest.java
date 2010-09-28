package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.Evaluator;
import com.tuongky.flagfan.engine.MoveGenerator;
import com.tuongky.flagfan.engine.Position;
import com.tuongky.flagfan.engine.Search;
import com.tuongky.flagfan.engine.Zobrist;
import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.Misc;
import com.tuongky.utils.MyTimer;

public class FlagFanEngineTest {

	public static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
//	public static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1CH4C1/9/R1EAKAEHR b 0 1";
//	public static String fen = "r1eakae2/5r3/h3c1hc1/p1C1p3C/6p2/9/P1P1P1P1P/4E4/R4H3/1HEAKA2R b 0 7";
//	public static String fen = "3akaer1/3h5/4e1h2/pH5Cp/4P4/2p3c2/P8/2C6/4A4/4KA1R1 w 2 22";
//	public static String fen = "2eak3C/4a4/4e2R1/P3r3p/9/1rP3P2/1c6P/4E4/4H4/2EAKA3 b 0 26";
//	public static String fen = "2e2a3/5k3/4e4/5H1H1/2p6/9/9/E3E4/4A3p/3K1A3 w 9 1";
//	public static String fen = "3aka3/9/2hce1h1e/rR2p3p/6p2/2C4R1/4P1P1P/C1r1E1H2/9/2EAKA3 w"; // X8-3 or X8+2
//	public static String fen = "4kae2/4a4/h1H1e4/4p3p/3R5/p5P2/4P4/4E2r1/4A1C2/4KAE1c w"; // X6.9
//	public static String fen = "2cak4/4aP3/4c4/9/9/9/9/4C4/4Ap3/2CAK4 w"; // P7+4
//	public static String fen = "2e2ae2/3R5/4k3h/C8/6p1P/P1P1P4/6P2/4E4/4rr3/3K1AEcR w 7 2";
//	public static String fen = "4ka3/3Pa4/4e4/2h5p/2p6/6E1P/9/2pAE4/4A4/3CK4 w 0 55";
//	public static String fen = "2eak4/4a4/c1r1er3/3P1H3/5R3/R8/6p2/4E4/4A4/3AK1E2 w 0 43";
//	public static String fen = "3aka3/3Ph4/1r1ce4/4R3p/9/6E2/8P/9/4K4/9 b 9 60"; // H5-7
//	public static String fen = "4ka3/3Pa4/1r1c2R2/8p/9/6E2/8P/9/4K4/9 b 0 62";
//	public static String fen = "4k2r1/2R6/5a3/h1p1p3C/8P/2P3C2/2c3P2/1p2E4/4A4/3AK1E2 b 4 37";
//	public static String fen = "2eakaehr/r8/hc5c1/p1p1p1p1p/9/9/P1P1P1P1P/1CH3HC1/8R/R1EAKAE2 b 5 3";
//	public static String fen = "rheakaehr/9/9/p1p1p1p1p/4c4/4c4/P1P3P1P/1C5C1/9/RHEAKAEHR w 0 1"; // mate
//	public static String fen = "2eakaehr/r8/hc5c1/p1p1p1p1p/9/9/P1P1P1P1P/1CH3HC1/8R/R1EAKAE2 b 5 3";
//	public static String fen = "2eakae2/8r/h1c3hc1/p1p1p1p1p/9/2P1C4/Pr2P1P1P/1CH3H2/1R7/2EAKAE1R b 6 7";
//	public static String fen = "2e1kae2/4a3r/h1c3hc1/p1p1p1p1p/9/2P1C4/Pr2P1P1P/1CH3H2/1R7/2EAKAER1 b 8 8";
//	public static String fen = "3k5/4a4/9/2H1c4/9/9/9/9/5p3/4K4 b 7 144";
//	public static String fen = "r1eakhe1r/4a4/4c4/p1p3R1p/4C3c/9/P4R3/4C3H/9/2EAKAE2 w 13 25";
	
	Search search;
	Position p;
	
	public FlagFanEngineTest() {
		System.out.println("Start");
		initAll();
//		String[] moves = {"d8d7", "e7e8", "d7d8", "e8e7", "d8d7", "e7e8", "d7d8"};		
//		for (String wbm: moves) makeMove(wbm);
		MyTimer timer = new MyTimer();
		search.setTimeLimit(-1);
		search.setMaxDepth(20);
		int bestMove = search.findBestMove();
		System.out.println("The best move is:");
		Misc.printMoveForHuman(p, bestMove);
		timer.printElapsedTime();
	}
	
	void makeMove(String wbm) {
		int move = Misc.ffMove(wbm);
		p.makeMove(move);
	}

	void initAll() {
		initSingletons();
		initPosition();
	}
	
	void initSingletons() {
		MoveGenerator.getInstance();
		Evaluator.getInstance();
		Zobrist.getInstance();
	}
	
	void initPosition() {
		FEN f;
		try {
			f = new FEN(fen);
			p = new Position(f.getBoard90(), f.getTurn());
			search = new Search(p);					
		} catch (FENException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new FlagFanEngineTest();
	}
	
}
