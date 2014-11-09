package com.sosop.cache.cluster;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sosop.cache.rule.DefaultRule;
import com.sosop.cache.rule.Rule;
import com.sosop.cache.utils.Properties;

public class ClusterInfo {

	private final static String DEFAUL_PATH = "redis-cluster.properties";

	private Properties prop;

	private List<Cluster> clusters;
	
	private Map<Object, Cluster> map;
	
	private Rule rule;

	public ClusterInfo() {
		this.prop = new Properties();
		clusters = prop.load(DEFAUL_PATH);
		this.config();
	}
	
	public ClusterInfo(Rule rule) {
		this.prop = new Properties();
		clusters = prop.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}

	public ClusterInfo(String path) {
		this.prop = new Properties();
		clusters = prop.load(path);
		this.config();
	}
	
	public ClusterInfo(String path, Rule rule) {
		this.prop = new Properties();
		clusters = prop.load(path);
		this.rule = rule.initial(this);
		this.config();
	}

	public ClusterInfo(Properties prop) {
		this.prop = prop;
		clusters = prop.load(DEFAUL_PATH);
		this.config();
	}
	
	public ClusterInfo(Properties prop, Rule rule) {
		this.prop = prop;
		clusters = prop.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}

	public List<Cluster> getClusters() {
		return clusters;
	}
	
	public Cluster cluster(Object obj) {
		return map.get(obj);
	}
	
	public Cluster rule(Object obj) {
		return rule.getCluster(obj);
	}

	private void config() {
		if(null == this.rule) {
			rule = new DefaultRule().initial(this);
		}
		map = new HashMap<>();
		for (Cluster cluster : clusters) {
			map.put(cluster.getName(), cluster);
			map.put(cluster.getSerial(), cluster);
		}
	}
}