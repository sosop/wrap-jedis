package com.tcl.aota.cache.redis.cluster;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcl.aota.cache.redis.global.settings.Settings;
import com.tcl.aota.cache.redis.heartbeat.HeartBeat;
import com.tcl.aota.cache.redis.rule.DefaultRule;
import com.tcl.aota.cache.redis.rule.Rule;
import com.tcl.aota.cache.redis.statistics.Hits;
import com.tcl.aota.cache.redis.utils.BufUtil;

public abstract class ClusterInfo {

	protected List<Cluster> clusters;

	protected Map<Object, Cluster> map;

	protected Rule rule;
	
	public List<Cluster> getClusters() {
		return clusters;
	}
	
	public Cluster cluster(Object obj) {
		return map.get(obj);
	}
	
	public Cluster cluster(ByteBuffer buff) {
		return map.get(BufUtil.BufToObj(buff));
	}
	
	public Cluster rule(Object obj) {
		return rule.getCluster(obj);
	}
	
	public Cluster rule(ByteBuffer buff) {
		return rule.getCluster(BufUtil.BufToObj(buff));
	}
	
	protected void config() {
		if(null == this.rule) {
			rule = new DefaultRule().initial(this);
		}
		if(Settings.getInstance().isHitCount()) {
			Hits.getInstance().start();
		}
		if(Settings.getInstance().isReplication()){
			HeartBeat.get().init(clusters).start();
		}
		map = new HashMap<>();
		for (Cluster cluster : clusters) {
			cluster.wire();
			map.put(cluster.getName(), cluster);
			map.put(cluster.getSerial(), cluster);
		}
	}
}
