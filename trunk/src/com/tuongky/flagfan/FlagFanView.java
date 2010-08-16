package com.tuongky.flagfan;

import javax.swing.*;

public class FlagFanView extends JFrame {
	
	BoardPanel panel;
	
	public FlagFanView(FlagFanMain xqm) {
		panel = new BoardPanel(xqm);
		setContentPane(panel);
		setTitle("Flag Fan version 2.0");
		setSize(10+310+10, 10+365+10);
		setResizable(false);
		setLocation(200, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
	}

	public void refresh() {
		panel.refreshBoard();
	}
	
}
