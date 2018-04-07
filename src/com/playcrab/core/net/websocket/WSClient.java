package com.playcrab.core.net.websocket;

import java.net.URI;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.playcrab.net.kakura.msg.KakuraMessage;
import com.playcrab.net.kakura.msg.KakuraPackage;
import com.playcrab.robot.pub.RobotProperties;

public class WSClient {
	private WebSocketClient client;
	
	private String defaultAesKey="ilo24wEFS*^^*2Ewilo24wEFS*^^*2Ew";
	
	public void init() throws Exception {
		String webSocketUrl = RobotProperties.getInstance().getWebSocketUrl();
		int webSocketPort = RobotProperties.getInstance().getWebSocketPort();
		URI uri = new URI(webSocketUrl+":"+webSocketPort+"/websocket");
		
		client=new WebSocketClient(uri) {
			
			@Override
			public void onOpen(ServerHandshake arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMessage(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Exception arg0) {
				arg0.printStackTrace();
			}
			
			@Override
			public void onClose(int arg0, String arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
			}
		};
		
		client.connect();
		while(!client.getReadyState().equals(READYSTATE.OPEN)) {
			System.out.println("还没有打开");
		}
	}
	
	public void send(String str) throws Exception {
		sendMessage(1, 1, str, 1);
	}
	
	public boolean sendMessage(int opcode, int reqId, String value, int messageType) throws Exception {
        KakuraPackage kp = new KakuraPackage();
        kp.requestId = reqId;
        kp.value = value.getBytes("UTF-8");
        kp.opCode = opcode;
        byte[] createMessage = KakuraMessage.createMessage(messageType, kp, defaultAesKey);
        client.send(createMessage);
		return true;
	}
	
	public static void main(String[] args) throws Exception{
		RobotProperties.getInstance().init();
		WSClient client=new WSClient();
		client.init();
		client.send("asdfasdfasfd");
	}
	
}
