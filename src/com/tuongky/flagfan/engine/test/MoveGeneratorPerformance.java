package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.MoveGenerator;
import com.tuongky.flagfan.engine.MoveSorter;
import com.tuongky.flagfan.engine.Position;
import com.tuongky.utils.FENException;
import com.tuongky.utils.Misc;
import com.tuongky.utils.MyTimer;

public class MoveGeneratorPerformance {

	final static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1";
//	public static String fen = "r1eakae2/5r3/h3c1hc1/p1C1p3C/6p2/9/P1P1P1P1P/4E4/R4H3/1HEAKA2R b 0 7";
//	public static String fen = "4ka3/3Pa4/4e4/2h5p/2p6/6E1P/9/2pAE4/4A4/3CK4 w 0 55";
//	public static String fen = "2e2ae2/3R5/4k3h/C8/6p1P/P1P1P4/6P2/4E4/4rr3/3K1AEcR w 7 2";
	
	public MoveGeneratorPerformance() throws FENException {
		MyTimer timer = new MyTimer();
		Position p = new Position(fen);
		long[][] historyTable = new long[256][256];;
		for (int i=0; i<1; i++) {
			MoveGenerator mg = MoveGenerator.getInstance();
			int[] moveList = new int[100];
			int num = mg.genAllMoves(p, moveList);
			Misc.debug(num);
//			p.makeMove(moveList[0]);
//			p.undoMakeMove();
//			int num = mg.genCaptureMoves(p, moveList);
			
//			MoveSorter ms = new MoveSorter(p, moveList, num, historyTable);
//			for (int j=0; j<num; j++) Misc.printMoveForHuman(p, ms.nextMove());
			
			for (int j=0; j<num; j++) Misc.printMoveForHuman(p, moveList[j]);
			
//			p.printMoveForHuman(moveList[0]);
		}
		timer.printElapsedTime();
	}
	
	public static void main(String[] args) throws FENException {
		new MoveGeneratorPerformance();
	}
	
}
