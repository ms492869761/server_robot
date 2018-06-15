package com.playcrab.robot.robot.production;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.robot.IRobotProduction;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.robot.pub.RobotProperties;
import com.playcrab.robot.robot.Robot;

public class RobotProduction implements IRobotProduction {

	private int battleCount;
	
	private int battleIdBegin;
	
	private int battleIdEnd;
	
	private int battleId;
	
	public RobotProduction() {
		this.battleCount=RobotProperties.getInstance().getBattleCount();
		this.battleIdBegin=RobotProperties.getInstance().getBattleIdBegin();
		this.battleIdEnd=RobotProperties.getInstance().getBattleIdEnd();
		this.battleId=RobotProperties.getInstance().getBattleIdBegin();
	}
	
	@Override
	public void productionLine() {
		// 机器人生产线   生产机器人逻辑

		while (true) {
			int robotCount = RobotCoreManager.getInstance().getRobotCount();
			if (robotCount < this.battleCount*2) {
				try {
					String battleId = ""+this.battleId;
					String id1 = "robot_" + this.battleId*2;
					String id2 = "robot_" + (this.battleId*2+1);
					Robot robot1 = new Robot(id1, battleId, id2);
					Robot robot2 = new Robot(id2, battleId, id1);
					Map<String, Object> dataMap = new HashMap<>();
					dataMap.put("battleId", battleId);
					dataMap.put("rid1", robot1.getId());
					dataMap.put("rid2", robot2.getId());
					robot1.setBattleInfo(new JSONObject(dataMap));
					RobotCoreManager.getInstance().addRobot(robot1);
					RobotCoreManager.getInstance().addRobot(robot2);
					this.battleId++;
					if(this.battleId==battleIdEnd) {
						this.battleId=battleIdBegin;
					}
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}