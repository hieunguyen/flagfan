package com.tuongky.flagfan.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class EngineDelegate {
	
	final static String XBOARD 		= "xboard";
	final static String UCI 		= "uci";
	
	XBoard xBoardEngine;
	
    BufferedReader br;	
	PrintStream out;

	void forXboard() throws IOException {
		xBoardEngine = new FlagFanEngine();
		String command;
		while (true) {
			String s = br.readLine().trim();
			String[] ss = s.split(" ");
			command = ss[0];
			if (command.equals("new")) {
				xBoardEngine.init();
				continue;
			}
			if (command.equals("quit")) {
				xBoardEngine.quit();
				continue;
			}
			if (command.equals("force")) {
				xBoardEngine.force();
				continue;
			}			
			if (command.equals("white")) {
				xBoardEngine.white();
				continue;
			}
			if (command.equals("black")) {
				xBoardEngine.black();
				continue;
			}
			if (command.equals("st")) {
				xBoardEngine.setTime(Integer.parseInt(ss[1]));
				continue;
			}
			if (command.equals("sd")) {
				xBoardEngine.setDepth(Integer.parseInt(ss[1]));
				continue;
			}
			if (command.equals("level")) {
				assert ss.length == 4;
				int mps, base, inc;
				mps = Integer.parseInt(ss[1]);
				inc = Integer.parseInt(ss[3]);
				if (ss[2].indexOf(":")>=0) {
					String[] tmp = ss[2].split(":");
					base = Integer.parseInt(tmp[0]) * 60 + Integer.parseInt(tmp[1]);
				} else base = Integer.parseInt(ss[2]);
				xBoardEngine.level(mps, base, inc);
				continue;
			}
			if (command.equals("time")) {
				int n = Integer.parseInt(ss[1]);
				xBoardEngine.time(n);
				continue;
			}
			if (command.equals("otim")) {
				int n = Integer.parseInt(ss[1]);
				xBoardEngine.otim(n);
				continue;
			}
		}
	}
	
	void forUCI() {
		while (true) {
		}
	}
	
	public EngineDelegate() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintStream(System.out, true);
		String protocol = br.readLine();
		if (protocol.equals(XBOARD)) {
			forXboard();
		} else {
			if (protocol.equals(UCI)) {
				forUCI();
			} else {
				out.println("Cannot recognize the procotol "+protocol);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		new EngineDelegate();
	}
	
}
