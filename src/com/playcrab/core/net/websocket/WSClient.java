package com.playcrab.core.net.websocket;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.playcrab.core.net.encode.IWebSocketDecoder;
import com.playcrab.core.robot.BaseRobot;
import com.playcrab.robot.pub.RobotProperties;

public class WSClient {
	private WebSocketClient client;
	
	
	
	private IWebSocketDecoder iWebSocketDecoder;
	
	private BaseRobot iRobot;
	
	public WSClient(IWebSocketDecoder decoder,BaseRobot iRobot) throws Exception {
		this.iWebSocketDecoder=decoder;
		this.iRobot=iRobot;
		init();
	}
	
	public void init() throws Exception {
		String webSocketUrl = RobotProperties.getInstance().getWebSocketUrl();
		int webSocketPort = RobotProperties.getInstance().getWebSocketPort();
		URI uri = new URI(webSocketUrl+":"+webSocketPort+"/websocket");
		
		client=new WebSocketClient(uri) {
			
			@Override
			public void onOpen(ServerHandshake arg0) {
				iRobot.connectSuccess();
			}
			
			@Override
			public void onMessage(String arg0) {
				
			}
			
			@Override
			public void onMessage(ByteBuffer bytes) {
				int remaining = bytes.remaining();
				byte[] body=new byte[remaining];
				bytes.get(body);
				String decodeMsg = iWebSocketDecoder.decodeMsg(body);
				iRobot.onMessage(decodeMsg);
			}
			
			@Override
			public void onError(Exception arg0) {
				iRobot.onError(arg0);
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				iRobot.onClose();
			}
		};
		
	}
	
	public void connect() {
		client.connect();
	}
	
	public void send(String str) throws Exception {
		if(iWebSocketDecoder==null) {
			throw new Exception("iWebSocketDecoder can't be null");
		}
		byte[] encodeMsg = iWebSocketDecoder.encodeMsg(str);
		client.send(encodeMsg);
	}
	
	
	public void close() {
		client.close();
	}
	
	
}
