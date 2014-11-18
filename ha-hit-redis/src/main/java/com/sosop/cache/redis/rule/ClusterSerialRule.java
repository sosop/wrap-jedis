package com.sosop.cache.redis.rule;

import com.sosop.cache.redis.cluster.Cluster;

public abstract class ClusterSerialRule extends Rule {
	
	@Override
	public Cluster getCluster(Object condition) {
		return clusterInfo.cluster(getSerial(condition));
	}
	
	public abstract int getSerial(Object condition);
}
