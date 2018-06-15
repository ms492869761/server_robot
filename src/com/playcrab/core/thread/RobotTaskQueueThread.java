package com.playcrab.core.thread;

import java.util.concurrent.LinkedBlockingQueue;

public class RobotTaskQueueThread extends Thread{
	
	private int threadId=0;
	
	private volatile boolean run=true;
	
	private LinkedBlockingQueue<Runnable> taskQueue=new LinkedBlockingQueue<>();
	
	public RobotTaskQueueThread(int threadId) {
		this.threadId=threadId;
		setName("robot-task-queue-thread-"+threadId);
	}
	
	
	
	@Override
	public void run() {
		while(run) {
			try {
				Runnable poll = taskQueue.take();
				poll.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addTask(Runnable runnable) {
		taskQueue.add(runnable);
	}
	
	
	public int getThreadId() {
		return threadId;
	}
	
	
	
}
