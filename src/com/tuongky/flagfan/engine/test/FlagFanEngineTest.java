package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.Position;
import com.tuongky.flagfan.engine.Search;
import com.tuongky.utils.FEN;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class FlagFanEngineTest {

	public static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
//	public static String fen = "2eak4/4a4/6hc1/p3p3p/1H4RC1/1r7/4P3P/4E4/4A1H2/c3KAE2 w 1 23";
//	public static String fen = "3akaehr/9/rRh1e4/p1p1p1p1p/9/4P4/c1P5P/2C1C1H2/9/2EAKAER1 w 2 8";
//	public static String fen = "3akaer1/3h5/4e1h2/pH5Cp/4P4/2p3c2/P8/2C6/4A4/4KA1R1 w 2 22";
//	public static String fen = "3r1ke2/4a4/h1c3C2/p3p3p/6p2/9/P1P1P1c1P/2H1E4/4Hr3/1REAKA3 w 0 15";
//	public static String fen = "2eaka3/9/4ec3/p3p3p/3r1h3/2P3P1P/P2hP2r1/E1H1E3C/4A4/2RAK2HR b 2 20"; // X8+2
//	public static String fen = "3akae2/9/2c1e4/p3p4/5h3/6E1P/P1P1P1P2/R4c1rH/4K4/1H1A2E2 b 5 16"; // P3+2
//	public static String fen = "2e1ka3/9/4e2P1/6P2/1r7/9/3p2h2/C1RAEh3/9/2EA1K3 b 14 58";
//	public static String fen = "1reaka1hr/9/2h1e1cc1/p1C1p3p/9/6P2/P1P1P3P/4E2C1/9/RH1AKAEHR b 0 5";
//	public static String fen = "2eak3C/4a4/4e2R1/P3r3p/9/1rP3P2/1c6P/4E4/4H4/2EAKA3 b 0 26";
	
	Search search;
	Position p;
	
	public FlagFanEngineTest() {
		System.out.println("Start");
		init();
		MyTimer timer = new MyTimer();
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
