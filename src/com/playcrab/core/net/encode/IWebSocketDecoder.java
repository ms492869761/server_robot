package com.playcrab.core.net.encode;

public interface IWebSocketDecoder {
	
	
	public byte[] encodeMsg(String context);
	
	
	public String decodeMsg(byte[] bytes);
	
}
