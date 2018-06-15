package com.playcrab.core.thread;

import java.util.ArrayList;

import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.service.RobotCoreManager;

/**
 * 机器人轮询线程
 * @author wangzhiyuan@playcrab.com
 *
 */
public class RobotLoopThread extends Thread{
	
	/** 线程ID*/ 
	private int threadId=0;
	/** 是否运行*/
	private volatile boolean run=true;
	/** 
	 * 机器人轮询线程
	 * @param threadId 线程ID
	 */
	public RobotLoopThread(int threadId) {
		this.threadId=threadId;
		setName("robot-loop-thread-"+threadId);
	}
	
	
	@Override
	public void run() {
		while(run) {
			ArrayList<BaseRobot> robotByThreadId = RobotCoreManager.getInstance().getRobotByThreadId(threadId);
			if(robotByThreadId==null||robotByThreadId.size()<=0) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			long start = System.currentTimeMillis();
			robotByThreadId.stream().forEach(robot->{
				try {
					robot.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			long end = System.currentTimeMillis();
			long useTime=end-start;
			if(useTime<1000) {
				try {
					sleep(1000-useTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setRun(boolean run) {
		this.run = run;
	}
	
}
