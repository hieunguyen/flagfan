package com.tuongky.flagfan.engine.test;

import com.tuongky.flagfan.engine.MoveGenerator;
import com.tuongky.flagfan.engine.Position;
import com.tuongky.utils.FENException;
import com.tuongky.utils.MyTimer;

public class MoveGeneratorPerformance {

	final static String fen = "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR b - - - 1";
	
	public MoveGeneratorPerformance() throws FENException {
		MyTimer timer = new MyTimer();
		Position p = new Position(fen);
		for (int i=0; i<1; i++) {
			MoveGenerator mg = MoveGenerator.getInstance();
			int[] moveList = new int[100];
			int num = mg.genAllMoves(p, moveList);
//			Misc.debug(num);
			for (int j=0; j<num; j++) p.printMoveForHuman(moveList[j]);
//			p.printMoveForHuman(moveList[0]);
		}
		timer.printElapsedTime();
	}
	
	public static void main(String[] args) throws FENException {
		new MoveGeneratorPerformance();
	}
	
}
