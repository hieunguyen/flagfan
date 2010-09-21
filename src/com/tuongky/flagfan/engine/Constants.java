package com.tuongky.flagfan.engine;

public class Constants {

	// Info
	final static String ENGINE_NAME 				= "FlagFan 1.1";
	final static String AUTHOR 						= "Hieu Nguyen";
	
	// Protocol
	final static String XBOARD 						= "xboard";
	final static String UCI 						= "uci";
	
	// General
	public static final int oo 						= 1001001001;
	public static final int RANDOM_SEED 			= 1234567;

	// Game
	public static final int MAX_MOVE_NUM 			= 500;
	
	// Board
	public static final int BOARD_SIZE 				= 256;
	public static final int RANKS 					= 10;
	public static final int FILES 					= 9;
	
	// Piece
	public static final int KING					= 0;
	public static final int ADVISOR					= 1;
	public static final int ELEPHANT				= 2;
	public static final int ROOK					= 3;
	public static final int CANNON					= 4;
	public static final int HORSE					= 5;
	public static final int PAWN					= 6;
	public static final String PIECE_LETTERS 		= "KAERCHP";  
	public static final int[] PIECE_TYPES 			= 
	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	 KING,ADVISOR,ADVISOR,ELEPHANT,ELEPHANT,ROOK,ROOK,CANNON,CANNON,HORSE,HORSE,PAWN,PAWN,PAWN,PAWN,PAWN,
	 KING,ADVISOR,ADVISOR,ELEPHANT,ELEPHANT,ROOK,ROOK,CANNON,CANNON,HORSE,HORSE,PAWN,PAWN,PAWN,PAWN,PAWN,};

	// Colour
	public static final int RED						= 0;
	public static final int BLACK					= 1;
	public static final int EMPTY					= 2;
	
	// Move
	public static final int NULL_MOVE 				= 0;
	
	// Search
	public static final int MAX_DEPTH 				= 50;
	public static final int DEEPEST 				= 100;
	public static final int MAX_WIDTH 				= 100;
	public static final int TIME_LIMIT 				= 2*60*1000; // 2 mins
	public static final int TIME_UNLIMITED 			= 24*60*60*1000; // 1 day
	public static final boolean PV_NODE				= true;
	public static final boolean NON_PV				= false;

	// Evaluation
	public static final int WIN_VALUE				= 1000000;
	public static final int DRAW_VALUE				= -3;
	
	// Transposition Table
	public static final int TSIZE 					= 1<<20;
	public static final byte LOWER_BOUND 			= 0;
	public static final byte EXACT_SCORE 			= 1;
	public static final byte UPPER_BOUND 			= 2;

	private Constants() {}
	
}
