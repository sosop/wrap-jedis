package com.tcl.aota.cache.redis.factory;

import com.tcl.aota.cache.redis.node.Node;

public abstract class NodeFactory {
	public static Node create() {
		return new Node();
	}
}
