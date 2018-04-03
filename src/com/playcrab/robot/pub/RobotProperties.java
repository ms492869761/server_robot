package com.playcrab.robot.pub;

import java.io.FileInputStream;
import java.util.Properties;

public class RobotProperties {
	
	private static RobotProperties instance=new RobotProperties();
	
	private RobotProperties() {
		
	}
	
	public static RobotProperties getInstance() {
		return instance;
	}
	
	private String webSocketUrl;
	
	private int webSocketPort;
	
	private String httpUrl;
	
	private int httpPort;
	
	
	public void init() {
		try {
			String userDir = System.getProperty("user.dir");
			Properties properties=new Properties();
			System.out.println(userDir+"/config/robot.properties");
			properties.load(new FileInputStream(userDir+"/config/robot.properties"));
			this.webSocketUrl = properties.getProperty("robot.websocket.url");
			this.webSocketPort=Integer.parseInt(properties.getProperty("robot.websocket.port"));
			this.httpUrl=properties.getProperty("robot.http.url");
			this.httpPort=Integer.parseInt(properties.getProperty("robot.http.port"));
			
			
		} catch (Exception e) {
			System.exit(1);
		}
	}
	
	public int getWebSocketPort() {
		return webSocketPort;
	}
	
	@Deprecated
	public void setWebSocketPort(int webSocketPort) {
		this.webSocketPort = webSocketPort;
	}
	
	public String getWebSocketUrl() {
		return webSocketUrl;
	}
	
	@Deprecated
	public void setWebSocketUrl(String webSocketUrl) {
		this.webSocketUrl = webSocketUrl;
	}
	
	public int getHttpPort() {
		return httpPort;
	}
	
	@Deprecated
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}
	
	public String getHttpUrl() {
		return httpUrl;
	}
	
	@Deprecated
	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	
	
	
}
