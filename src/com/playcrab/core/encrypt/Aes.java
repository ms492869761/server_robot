package com.playcrab.core.encrypt;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lijianan on 2017/8/7.
 */
public class Aes {
	public static Logger logger = LoggerFactory.getLogger(Aes.class);

	private String ivParameter = "0000000000000000";

	public byte[] encrypt(byte[] sSrcBytes, String aesKey) throws Exception {
		System.out.println(new String(sSrcBytes,"UTF-8"));
		byte[] raw = aesKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		int paddingSize = 16;
		int mod16 = sSrcBytes.length % 16;
		byte[] afterPaddingData;
		if (mod16 != 0) {
			paddingSize = 16 - mod16;
		}
		byte[] padding = new byte[paddingSize];
		for (int i = 0; i < paddingSize; i++) {
			padding[i] = (byte) paddingSize;
		}
		afterPaddingData = Aes.contactBytes(sSrcBytes, padding);

		byte[] rangIv = ivParameter.getBytes("utf-8");
		byte[] enced = cipher.doFinal(afterPaddingData);
		byte[] finalData = Aes.contactBytes(rangIv, enced);
		byte[] decodeBase64 = Base64.encodeBase64(finalData);
		System.out.println(new String(decodeBase64));
		return finalData;
		
	}

	public byte[] decrypt(byte[] sSrcBytes, String aesKey) {
		try {
			byte[] raw = aesKey.getBytes();
			byte[] randiv = Aes.catBytes(sSrcBytes, 0, 16);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			IvParameterSpec iv = new IvParameterSpec(randiv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			int len = sSrcBytes.length;
			byte[] decrypt = cipher.doFinal(Aes.catBytes(sSrcBytes, 16, len - 16));
			int paddingLen = decrypt[decrypt.length - 1];
			return Aes.catBytes(decrypt, 0, decrypt.length - paddingLen);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("decrypt error:", e);
			return null;
		}
	}

	public static byte[] contactBytes(byte[] buf1, byte[] buf2) {
		int buf1Len = buf1.length;
		int buf2Len = buf2.length;
		if (buf1Len == 0) {
			return buf2;
		}
		if (buf2Len == 0) {
			return buf1;
		}
		byte[] finalData = new byte[buf1Len + buf2Len];
		System.arraycopy(buf1, 0, finalData, 0, buf1Len);
		System.arraycopy(buf2, 0, finalData, buf1Len, buf2Len);
		return finalData;
	}

	public static byte[] catBytes(byte[] buf, int start, int len) {
		byte[] bs = new byte[len];
		for (int i = start; i < start + len; i++) {
			bs[i - start] = buf[i];
		}
		return bs;
	}
	
	public static void main(String[] args) throws Exception {
		String a="MDAwMDAwMDAwMDAwMDAwMCzOkjau2cCR/VHK5Gk4I+5fFAuLptHpm3b0ZzgBlJzT";
		byte[] decodeBase64 = Base64.decodeBase64(a);
		String key="ilo24wEFS*^^*2Ewilo24wEFS*^^*2Ew";
		Aes aes=new Aes();
		byte[] decrypt = aes.decrypt(decodeBase64, key);
		System.out.println(new String(decrypt));
	}
}
