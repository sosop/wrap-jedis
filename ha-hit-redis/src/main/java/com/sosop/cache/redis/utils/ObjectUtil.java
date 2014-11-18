package com.sosop.cache.redis.utils;

import java.nio.ByteBuffer;
import java.util.Collection;

public class ObjectUtil {
	public static String BufToObj(ByteBuffer buf) {
		if (buf.hasArray()) {
			byte[] arr = buf.array();
			return new String(arr);
		}
		return null;
	}

	public static Object object(String value) {
		Object obj = null;
		try {
			obj = Integer.parseInt(value);
		} catch (Exception e) {
			obj = value;
		}
		return obj;
	}
	
	public static <T> String[] CollectTo(Collection<T> c) {
		String[] arr = new String[c.size()];
		c.toArray(arr);
		return arr;
	}
}
