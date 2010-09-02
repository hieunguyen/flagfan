package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.Position;
import com.tuongky.flagfan.engine.Search;
import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class FlagFanEngineTest {

//	public static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
//	public static String fen = "3akaer1/3h5/4e1h2/pH5Cp/4P4/2p3c2/P8/2C6/4A4/4KA1R1 w 2 22";
//	public static String fen = "2eak3C/4a4/4e2R1/P3r3p/9/1rP3P2/1c6P/4E4/4H4/2EAKA3 b 0 26";
	public static String fen = "r1eakae2/5r3/h3c1hc1/p1C1p3C/6p2/9/P1P1P1P1P/4E4/R4H3/1HEAKA2R b 0 7";
	
	Search search;
	Position p;
	
	public FlagFanEngineTest() {
		System.out.println("Start");
		init();
		MyTimer timer = new MyTimer();
		search.setTimeLimit(-1);
		search.setMaxDepth(12);
		int bestMove = search.findBestMove();
		p.printMoveForHuman(bestMove);
		timer.printElapsedTime();
	}

	private void init() {
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
