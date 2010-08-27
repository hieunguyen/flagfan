package com.tuongky.flagfan.engine;

public interface XBoard {

	// GUI -> Engine
	
	public void xboard();
	
	public void protover();
	
	public void ping();
	
	public void p();
	
	public void memory();
	
	public  void init(); // new
	
	public void quit();
	
	public void force();
	
	public void white();
	
	public void black();
	
	public void setTime(int time);
	
	public void setDepth(int depth);
	
	public void level(int mps, int base, int inc);
	
	public void time(int n);
	
	public void otim(int n);
	
	public void easy();
	
	public void hard();
	
	public void accepted();
	
	public void rejected();
	
	public void random();
	
	public void go();
	
	public void hint();
	
	public void undo();
	
	public void remove();
	
	public void post();
	
	public void nopost();
	
	public void variant();
	
	public void edit();
	
	// Engine -> GUI
	
	public String resign();
	
	public String move();
	
	public String feature();
	
	public String pong();
	
	public String error();
	
}
