package com.tcl.aota.cache.redis.rule;

import com.tcl.aota.cache.redis.cluster.Cluster;

public abstract class ClusterSerialRule extends Rule {
	
	@Override
	public Cluster getCluster(Object condition) {
		return clusterInfo.cluster(getSerial(condition));
	}
	
	public abstract int getSerial(Object condition);
}
