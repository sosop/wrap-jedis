package com.tcl.aota.cache.redis.utils;

import java.nio.ByteBuffer;

public class BufUtil {
	public static String BufToObj(ByteBuffer buf) {
		if (buf.hasArray()) {
			byte[] arr = buf.array();
			return new String(arr);
		}
		return null;
	}
}
