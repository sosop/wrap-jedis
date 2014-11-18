package com.tcl.aota.cache.redis.global.settings;

public class Settings {
	private final static Settings settings = new Settings();

	private boolean hitCount;

	private boolean replication;

	private Settings() {
	}

	public static Settings getInstance() {
		return settings;
	}

	public boolean isHitCount() {
		return hitCount;
	}

	public void setHitCount(boolean hitCount) {
		this.hitCount = hitCount;
	}

	public boolean isReplication() {
		return replication;
	}

	public void setReplication(boolean replication) {
		this.replication = replication;
	}

}
