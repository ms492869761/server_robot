package com.playcrab.robot.robot.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.controller.BaseController;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;
import com.playcrab.core.util.AES;
import com.playcrab.robot.pub.RobotProperties;
import com.playcrab.robot.robot.Robot;

public class ConnectInitController extends BaseController<Robot> {

	{
		registerMethod("");

	}

	public ConnectInitController(BaseRobot baseRobot,long intervalTime) {
		super(baseRobot,intervalTime);
	}

	@Override
	public boolean canUse() {
		Robot robot = getRobot();
		if (robot.isLogin()) {
			return false;
		}
		return true;
	}

	@Override
	public ConnectTypeEnum getConnectType() {
		return ConnectTypeEnum.CONNECT_BY_WEBSOCKET;
	}

	@Override
	public void init() {
		super.init();
		getRobot().setWebsocketAesKey(RobotProperties.getInstance().getDefaultAesKey());
	}

	@Override
	public void onMessage(JSONObject jsonObject) {
		JSONObject object = (JSONObject) jsonObject.get("result");
		String aesKey = object.getString("aesKey");
		getRobot().setWebsocketAesKey(aesKey);
		sendBattleInfo();
		getRobot().setLogin(true);
		getRobot().setBattleStep(1);
		countUtil.addCount("connector.init");

	}

	@Override
	public void run() {
		connect();
		login();
		
	}

	/**
	 * 连接
	 */
	private void connect() {
		boolean connection = getRobot().isConnection();
		if (connection) {
			return;
		}
		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					getRobot().connect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 登录
	 */
	private void login() {
		if (!getRobot().isConnection()) {
			return;
		}
		if (getRobot().isLogin()) {
			return;
		}

		getRobot().asyTask(new Runnable() {
			@Override
			public void run() {
				try {
					if (!countUtil.count("connector.init", 1)) {
						return;
					}
					String id = getRobot().getId();
					String battleId = getRobot().getBattleId();
					long exTime = System.currentTimeMillis() / 1000 + 60 * 60;
					Map<String, Object> dataMap = new HashMap<>();
					dataMap.put("battleId", battleId);
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add(id);
					dataMap.put("rids", arrayList);
					dataMap.put("expireTime", exTime);
					JSONObject jsonObject = new JSONObject(dataMap);
					String string = jsonObject.toString();
					byte[] encrypt = AES.encrypt(string.getBytes("UTF-8"),
							RobotProperties.getInstance().getConnectAesKey());
					byte[] encodeBase64 = Base64.encodeBase64(encrypt);
					Map<String, Object> dataMap2 = new HashMap<>();
					dataMap2.put("battleId", getRobot().getBattleId());
					dataMap2.put("rid", getRobot().getId());
					dataMap2.put("token", new String(encodeBase64));
					JSONObject jsonObject2 = new JSONObject(dataMap2);
					getRobot().sendMessage("connector.init", jsonObject2);
					countUtil.addCount("connector.init");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 发送插入战斗信息
	 */
	private void sendBattleInfo() {
		if (getRobot().isLogin()) {
			return;
		}
		if (getRobot().getBattleInfo() == null) {
			return;
		}
		getRobot().sendMessage("debug.command2", getRobot().getBattleInfo());
	}

}
