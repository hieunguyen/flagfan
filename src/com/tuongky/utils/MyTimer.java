package com.tuongky.utils;

public class MyTimer {

	private long beginTime;

	private long startTime;
	private long totalTime;
	
	public MyTimer() {
		 reset();
		 resetAccumulating();	
	}
	
	public void reset() {
		beginTime = System.currentTimeMillis();
	}

	public void resetAccumulating() {
		totalTime = 0;
		startTime = -1;
	}

	public long elapsedTime() {
		return System.currentTimeMillis() - beginTime;
	}
	
	public void printTime(long t) {
		System.out.println("Time: "+t*0.001+"s");
	}
	
	public void printElapsedTime() {
		printTime(elapsedTime());
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		if (startTime==-1) return ;
		totalTime += System.currentTimeMillis() - startTime;
		startTime = -1;
	}
	
	public void printTotalTime() {
		printTime(totalTime);
	}

	public long getTotalTime() {
		return totalTime;
	}
	
	public boolean expired(long timeLimit) {
		return elapsedTime()>timeLimit;
	}
	
}
