package com.playcrab.core.controller;

import java.util.HashSet;
import java.util.Set;

import com.playcrab.core.robot.BaseRobot;
import com.playcrab.core.type.ConnectTypeEnum;

/**
 * 机器人控制模块基本类
 * @author wangzhiyuan@playcrab.com
 *
 * @param <T>
 */
public abstract class BaseController<T extends BaseRobot> {
	/** 机器人*/
	private BaseRobot baseRobot;
	/** method处理集合*/
	private Set<String> methodSet=new HashSet<>();
	/** */
	public BaseController(BaseRobot baseRobot) {
		this.baseRobot=baseRobot;
	}
	
	public boolean hasMethod(String method) {
		return methodSet.contains(method);
	}
	
	protected void registerMethod(String method) {
		methodSet.add(method);
	}
	
	public abstract boolean canUse();
	
	public abstract ConnectTypeEnum getConnectType();
	
	@SuppressWarnings("unchecked")
	protected T getRobot() {
		return (T)baseRobot;
	}
	
	

	
	
}
