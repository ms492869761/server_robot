package com.playcrab.core.compress;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

/**
 * Created by lijianan on 2017/8/7.
 */
public class SnappyCompress {
	
	private static Logger logger = LoggerFactory.getLogger("debug");
	
	public static byte[] compress(byte[] msg) throws IOException {
		return Snappy.compress(msg);
	}

	public static byte[] uncompress(byte[] srcBytes) {
		try {
			return Snappy.uncompress(srcBytes);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
			return null;
		}
	}
}
