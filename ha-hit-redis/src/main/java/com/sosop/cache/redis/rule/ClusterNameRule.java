package com.sosop.cache.redis.rule;

import com.sosop.cache.redis.cluster.Cluster;

public abstract class ClusterNameRule extends Rule {
	@Override
	public Cluster getCluster(Object condition) {
		return clusterInfo.cluster(getName(condition));
	}
	
	public abstract String getName(Object condition);
}
