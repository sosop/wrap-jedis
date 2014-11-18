package com.tcl.aota.cache.redis.statistics;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.tcl.aota.cache.redis.utils.ArrayUtil;
import com.tcl.aota.cache.redis.utils.FileUtil;

public class Hits extends Thread {

	private static final Logger LOG = Logger.getLogger(Hits.class);

	private final static Hits hit = new Hits();

	private BlockingQueue<Log> queue = new LinkedBlockingQueue<>();
	
	private String[] tableKeys = { "get", "pop", "len", "exists", "expire",
			"member", "range", "keys", "remove", "card", "rank", "score" };

	private Hits() {
	}

	public static Hits getInstance() {
		return hit;
	}

	public void log(Object key, String methodName, Object ret) {
		Log log = new Log(key, methodName, ret);
		try {
			queue.offer(log, 500, TimeUnit.MICROSECONDS);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	
	
	
	@Override
	public void run() {
		while (true) {
			try {
				Log log = queue.take();
				if (ArrayUtil.isMatch(tableKeys, log.getMethodName())) {
					FileUtil.writeToFile(null, log.toString());
				}
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
}
