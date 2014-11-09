package com.sosop.cache.rule;

import com.sosop.cache.cluster.Cluster;

public abstract class ClusterNumRule extends Rule {
	
	@Override
	public Cluster getCluster(Object condition) {
		return clusterInfo.getNumMap().get(getNum(condition));
	}
	
	public abstract int getNum(Object condition);
}
