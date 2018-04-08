package com.playcrab.core.robot;

public interface IRobot {
	
	public void connectSuccess();
	
	public void onMessage(String arg0);
	
	public void onError(Exception e);
	
	public void onClose();
	
}
