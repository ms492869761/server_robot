package com.playcrab.robot;

import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.robot.pub.RobotProperties;
import com.playcrab.robot.robot.production.RobotProduction;
import com.playcrab.robot.timer.FixTimeTaskService;

public class RobotStart {
	
	public static void main(String[] args) {
		init();
		start();
	}
	
	
	public static void init() {
		RobotProperties.getInstance().init();
		
	}
	
	public static void start() {
		RobotCoreManager.initInstance(new RobotProduction(), RobotProperties.getInstance().getThreadCount());
		RobotCoreManager.getInstance().start();
		FixTimeTaskService.getInstance();
	}
	
	public static void stop() {
		
	}
	
}
