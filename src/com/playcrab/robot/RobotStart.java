package com.playcrab.robot;

import com.playcrab.robot.pub.RobotProperties;

public class RobotStart {
	
	public static void main(String[] args) {
		init();
		
		start();
	}
	
	
	public static void init() {
		RobotProperties.getInstance().init();
	}
	
	public static void start() {
		
	}
	
	public static void stop() {
		
	}
	
}
