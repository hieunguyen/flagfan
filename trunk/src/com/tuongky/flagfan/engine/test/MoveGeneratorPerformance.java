package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.MoveGenerator;
import com.tuongky.flagfan.engine.Position;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class MoveGeneratorPerformance {

//	final static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR b - - - 1";
	public static String fen = "r1eakae2/5r3/h3c1hc1/p1C1p3C/6p2/9/P1P1P1P1P/4E4/R4H3/1HEAKA2R b 0 7";
	
	public MoveGeneratorPerformance() throws FENException {
		MyTimer timer = new MyTimer();
		Position p = new Position(fen);
		for (int i=0; i<1000000; i++) {
			MoveGenerator mg = MoveGenerator.getInstance();
			int[] moveList = new int[100];
			int num = mg.genAllMoves(p, moveList);
			p.makeMove(moveList[0]);
			p.undoMakeMove();
//			int num = mg.genCaptureMoves(p, moveList);
//			Misc.debug(num);
//			for (int j=0; j<num; j++) p.printMoveForHuman(moveList[j]);
//			p.printMoveForHuman(moveList[0]);
		}
		timer.printElapsedTime();
	}
	
	public static void main(String[] args) throws FENException {
		new MoveGeneratorPerformance();
	}
	
}
