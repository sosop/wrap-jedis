package com.tcl.aota.cache.redis.factory;

import com.tcl.aota.cache.redis.cluster.Cluster;

public abstract class ClusterFactory {
	public static Cluster create() {
		return new Cluster();
	}
}
