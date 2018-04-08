package com.playcrab.robot.robot;

import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.net.websocket.WSClient;
import com.playcrab.core.robot.IRobot;
import com.playcrab.robot.RobotWebSocketDecoder;

public class Robot implements IRobot {
	
	private WSClient wsClient;
	
	private boolean isConnection=false;
	
	public Robot() {
		wsClient=new WSClient(new RobotWebSocketDecoder(), this);
		
		
		
	}
	
	
	@Override
	public void connectSuccess() {
		this.isConnection=true;

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
