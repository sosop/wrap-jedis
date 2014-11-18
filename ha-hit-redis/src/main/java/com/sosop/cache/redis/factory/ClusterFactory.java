package com.sosop.cache.redis.factory;

import com.sosop.cache.redis.cluster.Cluster;

public abstract class ClusterFactory {
	public static Cluster create() {
		return new Cluster();
	}
}
