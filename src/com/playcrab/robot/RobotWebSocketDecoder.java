package com.playcrab.robot;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.playcrab.core.net.encode.IWebSocketDecoder;
import com.playcrab.net.kakura.msg.KakuraMessage;
import com.playcrab.net.kakura.msg.KakuraPackage;

public class RobotWebSocketDecoder implements IWebSocketDecoder {
	
	private String defaultAesKey="ilo24wEFS*^^*2Ewilo24wEFS*^^*2Ew";
	
	private int requestId=0;
	
	@Override
	public byte[] encodeMsg(String context) {
		try {
			KakuraPackage kp = new KakuraPackage();
	        kp.requestId = this.requestId++;
	        kp.value = context.getBytes("UTF-8");
	        kp.opCode = 1;
	        byte[] createMessage = KakuraMessage.createMessage(1, kp, defaultAesKey);
	        return createMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String decodeMsg(byte[] bytes) {
		KakuraMessage kakuraMessage = new KakuraMessage(bytes, defaultAesKey);
		List<KakuraPackage> arrayPackage = kakuraMessage.getPackages();
		for (KakuraPackage kakuraPackage : arrayPackage) {
			byte[] value = kakuraPackage.value;
			String string=null;
			try {
				string = new String(value,"UTF-8");
			} catch (UnsupportedEncodingException e) {
 				e.printStackTrace();
			}
			return string;
		}
		return null;
	}

}
