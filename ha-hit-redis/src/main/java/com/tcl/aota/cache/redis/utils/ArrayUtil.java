package com.tcl.aota.cache.redis.utils;

public class ArrayUtil {
	public static boolean isMatch(String[] arr, String v) {
		for (String s : arr) {
			if(v.contains(s)) {
				return true;
			}
		}
		return false;
	}
}
