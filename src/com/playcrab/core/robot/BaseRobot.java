package com.playcrab.core.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.playcrab.core.controller.BaseController;
import com.playcrab.core.net.encode.IWebSocketDecoder;
import com.playcrab.core.net.websocket.WSClient;
import com.playcrab.core.type.ConnectTypeEnum;

/**
 * 机器人基本类
 * @author wangzhiyuan@playcrab.com
 *
 */
public abstract class BaseRobot {
	
	/** 控制模块Map集合*/
	@SuppressWarnings("rawtypes")
	protected Map<Class<? extends BaseController>, BaseController> controllerMap=new HashMap<>();
	/** 控制模块List集合*/
	@SuppressWarnings("rawtypes")
	protected List<BaseController> controllerList=new ArrayList<>();
	/** websocket连接客户端*/
	private WSClient wsClient;
	/** 当前机器人的连接类型 */
	private ConnectTypeEnum connectTypeEnum;
	
	/**
	 * 构造函数 必须实例化消息解码器
	 * @param iWebSocketDecoder
	 */
	public BaseRobot(IWebSocketDecoder iWebSocketDecoder) {
		wsClient=new WSClient(iWebSocketDecoder, this);
		connectTypeEnum=ConnectTypeEnum.CONNECT_BY_WEBSOCKET;
	}
	
	/**
	 * 注册控制模块
	 * @param controller
	 */
	@SuppressWarnings("rawtypes")
	protected void registerController(BaseController controller) {
		if(controllerMap.containsKey(controller.getClass())) {
			return ;
		}
		controllerMap.put(controller.getClass(), controller);
		controllerList.add(controller);
	}
	
	/**
	 * 轮询触发事件
	 */
	public void run() {
		
	}
	
	/**
	 * 获取机器人连接类型 
	 * @return
	 */
	public ConnectTypeEnum getConnectTypeEnum() {
		return connectTypeEnum;
	}
	
	/**
	 * 发送信息
	 * @param str
	 */
	public void sendMessage(String str) {
		switch (connectTypeEnum) {
			case CONNECT_BY_WEBSOCKET:
					try {
						wsClient.send(str);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				break;
			default:
				break;
			}
	}
	
	/**
	 * 关闭连接
	 */
	public void disconnect() {
		switch (connectTypeEnum) {
			case CONNECT_BY_WEBSOCKET:
					wsClient.close();
				break;
			default:
				break;
			}
	}
	
	/**
	 * 连接成功回调信息
	 */
	public abstract void connectSuccess();
	
	/**
	 * 消息回调
	 * @param arg0
	 */
	public abstract void onMessage(String arg0);
	
	/**
	 * 错误回调
	 * @param e
	 */
	public abstract void onError(Exception e);
	
	/**
	 * 连接断开回调
	 */
	public abstract void onClose();
	
}
