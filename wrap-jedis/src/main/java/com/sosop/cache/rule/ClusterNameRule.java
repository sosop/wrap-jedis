package com.sosop.cache.rule;

import com.sosop.cache.cluster.Cluster;

public abstract class ClusterNameRule extends Rule {
	@Override
	public Cluster getCluster(Object condition) {
		return clusterInfo.getNameMap().get(getName(condition));
	}
	
	public abstract String getName(Object condition);
}
