package com.tcl.aota.cache.redis.rule;

import java.util.SortedMap;

import com.tcl.aota.cache.redis.cluster.Cluster;

/**
 * 
 * @author xiaolong.hou 一致性hash
 */
public class DefaultRule extends Rule {

	@Override
	public Cluster getCluster(Object condition) {
		// 从hash值开始顺时针查找节点
		SortedMap<Long, Cluster> tail = virNodes.tailMap(hash(String.valueOf(condition)));
		// 如果hash值超出范围，映射到第一个虚拟节点
		if (tail.size() == 0) {
			return virNodes.get(virNodes.firstKey());
		}
		// 返回找到的第一个节点, 返回真实节点
		return tail.get(tail.firstKey());
	}

}
