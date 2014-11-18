package com.sosop.cache.redis.action;

import com.sosop.cache.redis.rule.ClusterNameRule;

public class AotaRule extends ClusterNameRule {
	@Override
	public String getName(Object condition) {
		return String.valueOf(condition);
	}
}
