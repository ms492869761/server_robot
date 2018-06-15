package com.playcrab.robot.robot;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.playcrab.core.controller.BaseController;
import com.playcrab.core.net.encode.IWebSocketDecoder;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.service.RobotCoreManager;
import com.playcrab.core.util.HashUtil;
import com.playcrab.core.util.UUIDService;
import com.playcrab.robot.pub.RobotProperties;
import com.playcrab.robot.robot.controller.ConnectInitController;
import com.playcrab.robot.robot.decoder.RobotWebSocketDecoder;
import com.playcrab.robot.status.RobotStatusManager;

public class Robot extends BaseRobot {
	
	
	public Robot(String id,String battleId,String opponent) throws Exception {
		this(new RobotWebSocketDecoder());
		this.id=id;
		this.battleId=battleId;
		this.opponent=opponent;
		this.lastMethodTime=System.currentTimeMillis();
		registerController(new ConnectInitController(this,2000));
		// TODO 此处注册机器人controller 
	}
	
	public Robot(IWebSocketDecoder iWebSocketDecoder) throws Exception {
		super(iWebSocketDecoder);
	}

	private boolean isConnection=false;
	
	private boolean isLogin=false; 
	
	private JSONObject battleInfo;
	
	private String id;
	
	private String battleId;
	
	private int requestId=0;
	
	private int team=0;
	
	private int battleStep=0;
	
	private String opponent="";
	
	private String lastMethod="";
	
	private long lastMethodTime=0;
	
	/**
	 * 设置websocket连接AESkey
	 * @param aesKey
	 */
	public void setWebsocketAesKey(String aesKey) {
		RobotWebSocketDecoder decoder=(RobotWebSocketDecoder)iWebSocketDecoder;
	}
	
	@Override
	public void connectSuccess() {
		this.isConnection=true;
	}
	
	public boolean isConnection() {
		return isConnection;
	}
	
	public boolean isLogin() {
		return isLogin;
	}
	
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public String getBattleId() {
		return battleId;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public int getBattleStep() {
		return battleStep;
	}
	
	public void setBattleStep(int battleStep) {
		this.battleStep = battleStep;
	}
	
	public JSONObject getBattleInfo() {
		return battleInfo;
	}
	
	public void setBattleInfo(JSONObject battleInfo) {
		this.battleInfo = battleInfo;
	}
	
	public String getOpponent() {
		return opponent;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onMessage(String arg0) {
		JSONObject parse = (JSONObject)JSON.parse(arg0);
		super.onMessage(arg0);
		if(!parse.containsKey("method")) {
			asyTask(new Runnable() {
				@Override
				public void run() {
					BaseController baseController = controllerMap.get(ConnectInitController.class);
					baseController.onMessage(parse);
				}
			});
		} else {
			String method = parse.getString("method");
			if(this.lastMethod!=null&&method.equals(this.lastMethod)) {
				long now = System.currentTimeMillis();
				RobotStatusManager.getInstance().countMethodTime(method, now-lastMethodTime);	
			}
		}
		// TODO 消息分发
	}

	@Override
	public void onError(Throwable e) {
		try {
			disconnect();
			e.printStackTrace();	
		} catch (Exception e2) {
			// TODO: handle exception
		}
		

	}

	@Override
	public void onClose() {
		RobotCoreManager.getInstance().removeRobot(this);
	}

	@Override
	public int getThreadId() {
		int threadCount = RobotProperties.getInstance().getThreadCount();
		int hash = HashUtil.hash(battleId);
		return hash%threadCount+1;
	}
	
	@Override
	public void run() {
		super.run();
		if(System.currentTimeMillis()-lastMethodTime>30000) {
			try {
				disconnect();
			} catch (Exception e) {
				RobotCoreManager.getInstance().removeRobot(this);
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void connect() throws Exception {
		setWebsocketAesKey(RobotProperties.getInstance().getDefaultAesKey());
		super.connect();
	}
	
	public void reset() {
		this.isConnection=false;
		this.isLogin=false;
		this.team=0;
		controllerList.stream().forEach(controller->{
			controller.init();
		});
	}
	
	public void sendMessage(String method,JSONObject jsonObject) {
		Map<String, Object> dataMap=new HashMap<>();
		dataMap.put("method", method);
		dataMap.put("id", requestId++);
		dataMap.put("uniqueId", UUIDService.getInstance().getId(1));
		dataMap.put("params", jsonObject);
		JSONObject jsonObject2 = new JSONObject(dataMap);
		this.lastMethod=method;
		this.lastMethodTime=System.currentTimeMillis();
		super.sendMessage(jsonObject2.toJSONString());
	}
	
}
