package com.sosop.cache.redis.global.settings;

public class Settings {
	private final static Settings settings = new Settings();

	private boolean hitCount;

	private boolean replication;
	
	private boolean autoSwitch;

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

	public boolean isAutoSwitch() {
		return autoSwitch;
	}

	public void setAutoSwitch(boolean autoSwitch) {
		this.autoSwitch = autoSwitch;
	}
}
