package com.playcrab.robot.robot.controller;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.robot.robot.Robot;

public class ConnectInitController extends BaseController<Robot> {
	
	{
		registerMethod("");
		
	}
	
	
	public ConnectInitController(BaseRobot baseRobot) {
		super(baseRobot);
	}

	@Override
	public boolean canUse() {
		Robot robot = getRobot();
		if (robot.isConnection()) {
			return false;
		}
		
		return true;
	}

	@Override
	public ConnectTypeEnum getConnectType() {
		return ConnectTypeEnum.CONNECT_BY_WEBSOCKET;
	}

}
