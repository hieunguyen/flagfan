package com.tuongky.flagfan;

public class ComputerPlayer {
	
	FlagFanMain xqm;
	Position p;
	SearchEngine se;
	
	public ComputerPlayer(FlagFanMain xqm, boolean comp) {
		this.xqm = xqm;
		p = new Position(xqm.ginf);
		if (comp) se = new SearchEngine(p); else se = null;
	}
	
	public int play() {
		p.startMove = p.moveNum;
		int bestMove = se.searchMain();
		return bestMove;
	}
	
}
