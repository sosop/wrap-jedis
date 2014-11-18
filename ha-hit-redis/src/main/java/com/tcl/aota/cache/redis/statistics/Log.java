package com.tcl.aota.cache.redis.statistics;

import com.tcl.aota.cache.redis.utils.StringUtil;

public class Log {
	private Object key;
	private String methodName;
	private Object result;

	public Log(Object key, String methodName, Object result) {
		super();
		this.key = key;
		this.methodName = methodName;
		this.result = result;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return StringUtil.append(key, "---", methodName, "---", result);
	}
}
