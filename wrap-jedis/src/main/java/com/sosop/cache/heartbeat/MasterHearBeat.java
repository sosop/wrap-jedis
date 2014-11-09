package com.sosop.cache.heartbeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.LockSupport;

import redis.clients.jedis.Jedis;

import com.sosop.cache.cluster.Cluster;
import com.sosop.cache.node.Node;
import com.sosop.cache.utils.Constant;

public class MasterHearBeat {
	private Map<Cluster, Map<Jedis, Node>> map;
	private Map<Cluster, Map<Jedis, Node>> failovers;
	private Map<Jedis, Node> failNodes;
	private List<Cluster> clusters;

	public MasterHearBeat(List<Cluster> clusters) {
		map = new HashMap<>();
		this.clusters = clusters;
		init();
	}

	public void init() {
		Jedis jedis = null;
		Map<Jedis, Node> jm;
		for (Cluster cluster : clusters) {
			jm = new HashMap<>(cluster.getNodes().size());
			for (Node master : cluster.getNodes()) {
				jedis = new Jedis(master.getHost(), master.getPort());
				jm.put(jedis, master);
			}
			map.put(cluster, jm);
		}

	}

	public void heartCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				failovers = new HashMap<>();
				failNodes = new HashMap<>();
				while (true) {
					for (Entry<Cluster, Map<Jedis, Node>> entry : map
							.entrySet()) {
						for (Entry<Jedis, Node> e : entry.getValue()
								.entrySet()) {
							// when disconnect
							if (!e.getKey().isConnected()) {
								Node m = e.getValue();
								m.setFlag(Constant.SLAVE);
								failNodes.put(e.getKey(), m);
								if (m.getSlaves().size() > 0) {
									Node s = m.getSlaves().get(0);
									s.setFlag(Constant.MASTER);
									s.setMaster(null);
									s.addSlave(m);
									m.getSlaves().remove(s);
									m.setMaster(s);
									for (Node sn : m.getSlaves()) {
										sn.setMaster(s);
										s.addSlave(sn);
									}
								}
							}
						}
						failovers.put(entry.getKey(), failNodes);
					}
					// every sec
					LockSupport.parkNanos(1000000L);
				}
			}
		});
	}
}