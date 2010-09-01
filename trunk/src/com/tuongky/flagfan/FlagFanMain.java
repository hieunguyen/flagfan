package com.tuongky.flagfan;

import static com.tuongky.flagfan.GameInfor.*;
import com.tuongky.flagfan.Initialization;

public class FlagFanMain {
	
	ComputerPlayer comp1, comp2;
	GameInfor ginf;	
	FlagFanView xqview;
	
	int p1 = COMPUTER;
	int p2 = HUMAN;	

	public FlagFanMain() {
		new Initialization();
		ginf = new GameInfor(this);
		xqview = new FlagFanView(this);
		comp1 = new ComputerPlayer(this, p1==COMPUTER);
		comp2 = new ComputerPlayer(this, p2==COMPUTER);
		gameLoop();
	}

	void gameLoop() {
		do {
			
			if (ginf.side==RED) {
					if (p1==COMPUTER) {
						int move = comp1.play();
						ginf.makeMove(move);
						xqview.refresh();
					}
			} else // ginf.side==BLACK
			if (p2==COMPUTER) {
					int move = comp2.play();
					ginf.makeMove(move);
					xqview.refresh();
			}
			
			try {
				Thread.currentThread().sleep(300);
			} catch (Exception e) {}
			
		} while (true);
	}
	
	public static void main(String[] args) {
		new FlagFanMain();
	}
	
}
