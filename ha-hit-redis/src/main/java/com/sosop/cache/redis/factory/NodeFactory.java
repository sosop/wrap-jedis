package com.sosop.cache.redis.factory;

import com.sosop.cache.redis.node.Node;

public abstract class NodeFactory {
	public static Node create() {
		return new Node();
	}
}
