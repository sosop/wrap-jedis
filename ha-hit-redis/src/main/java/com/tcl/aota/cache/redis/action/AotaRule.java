package com.tcl.aota.cache.redis.action;

import com.tcl.aota.cache.redis.rule.ClusterNameRule;

public class AotaRule extends ClusterNameRule {
	@Override
	public String getName(Object condition) {
		return String.valueOf(condition);
	}
}
