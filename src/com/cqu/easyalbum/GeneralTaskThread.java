package com.cqu.easyalbum;

public class GeneralTaskThread {
	
	private Thread thread;
	protected volatile boolean isRunning=false;
	
	private Runnable runnable;
	
	public GeneralTaskThread(Runnable runnable) {
		// TODO Auto-generated constructor stub
		this.runnable=runnable;
	}
	
	public void start()
	{
		thread=new Thread(runnable);
		thread.start();
	}
	
	public void stopRunning()
	{
		isRunning=false;
		thread.interrupt();
	}
	
	public boolean isRunning()
	{
		return this.isRunning;
	}
}
