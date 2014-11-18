package com.tcl.aota.cache.redis.cluster;


import java.util.List;

import com.tcl.aota.cache.redis.rule.Rule;
import com.tcl.aota.cache.redis.utils.Properties;

public class ClusterProp extends ClusterInfo {

	private final static String DEFAUL_PATH = "redis-cluster.properties";

	private Properties prop;
	
	@Deprecated
	public ClusterProp() {
		this.prop = new Properties();
		clusters = prop.load(DEFAUL_PATH);
		this.config();
	}
	
	@Deprecated
	public ClusterProp(Rule rule) {
		this.prop = new Properties();
		clusters = prop.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}

	@Deprecated
	public ClusterProp(String path) {
		this.prop = new Properties();
		clusters = prop.load(path);
		this.config();
	}
	
	@Deprecated
	public ClusterProp(String path, Rule rule) {
		this.prop = new Properties();
		clusters = prop.load(path);
		this.rule = rule.initial(this);
		this.config();
	}

	@Deprecated
	public ClusterProp(Properties prop) {
		this.prop = prop;
		clusters = prop.load(DEFAUL_PATH);
		this.config();
	}
	
	@Deprecated
	public ClusterProp(Properties prop, Rule rule) {
		this.prop = prop;
		clusters = prop.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}

	@Deprecated
	public List<Cluster> getClusters() {
		return clusters;
	}
	
	@Deprecated
	public Cluster cluster(Object obj) {
		return map.get(obj);
	}
	
	@Deprecated
	public Cluster rule(Object obj) {
		return rule.getCluster(obj);
	}

	
}