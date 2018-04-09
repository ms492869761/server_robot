package com.playcrab.robot.robot;

import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.net.encode.IWebSocketDecoder;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.robot.robot.decoder.RobotWebSocketDecoder;

public class Robot extends BaseRobot {
	
	public Robot() {
		this(new RobotWebSocketDecoder());
	}
	
	public Robot(IWebSocketDecoder iWebSocketDecoder) {
		super(iWebSocketDecoder);
	}

	private boolean isConnection=false;
	
	
	
	
	@Override
	public void connectSuccess() {
		this.isConnection=true;
	}
	
	public boolean isConnection() {
		return isConnection;
	}
	
	@Override
	public void onMessage(String arg0) {
		JSONObject parse = (JSONObject)JSONObject.parse(arg0);
		// TODO 消息分发
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

}
