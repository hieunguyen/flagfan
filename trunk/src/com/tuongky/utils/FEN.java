package com.tuongky.utils;

public class FEN {

	final static String RED_PIECES 		= "KAERCHP";
	final static String BLACK_PIECES 	= RED_PIECES.toLowerCase();
	public static String START_FEN		= "rheakaehr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RHEAKAEHR w - - - 1"; 

	int[] board90;
	int turn;
	int halfMoveClock;
	int fullMove;
	
	public FEN(String fen) throws FENException {
		String[] s = fen.split(" ");
		if (s.length<2) throw new FENException("FEN has <2 fields");
		String[] rows = s[0].split("/");
		if (rows.length<10) throw new FENException("There are <10 rows");
		board90 = new int[90];
		int square = 0;
		for (int i=0; i<10; i++) {
			for (int j=0; j<rows[i].length(); j++) {
				char c = rows[i].charAt(j);
				if (Character.isDigit(c)) {
					square += c-'0';
				} else {
					int pieceType = RED_PIECES.indexOf(Character.toUpperCase(c));
					if (pieceType<0) throw new FENException("Cannot recognize piece type "+c);
					board90[square] = pieceType + 1;
					if (Character.isLowerCase(c)) board90[square] = -board90[square];
					square++;
				}
			}
		}
		turn = s[1].toLowerCase().equals("w") ? 0:1;
		if (s.length>=5) {
			try {
				halfMoveClock = Integer.parseInt(s[4]);
			} catch (Exception e) {				
				halfMoveClock = -1;
			}
		}
		if (s.length>=6) {
			try {
				fullMove = Integer.parseInt(s[5]);
			} catch (Exception e) {
				fullMove = -1;
			}
		}
	}

	public int[] getBoard90() {
		return board90;
	}

	public int getTurn() {
		return turn;
	}

	public int getHalfMoveClock() {
		return halfMoveClock;
	}

	public int getFullMove() {
		return fullMove;
	}
	
}
