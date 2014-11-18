package com.tcl.aota.cache.redis.cluster;


import com.tcl.aota.cache.redis.rule.Rule;
import com.tcl.aota.cache.redis.utils.XMLParse;

public class ClusterXML extends ClusterInfo {

	private final static String DEFAUL_PATH = "redis-cluster.xml";

	private XMLParse xml;

	
	public ClusterXML() {
		this.xml = new XMLParse();
		clusters = xml.load(DEFAUL_PATH);
		this.config();
	}
	
	public ClusterXML(Rule rule) {
		this.xml = new XMLParse();
		clusters = xml.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}

	public ClusterXML(String path) {
		this.xml = new XMLParse();
		clusters = xml.load(path);
		this.config();
	}
	
	public ClusterXML(String path, Rule rule) {
		this.xml = new XMLParse();
		clusters = xml.load(path);
		this.rule = rule.initial(this);
		this.config();
	}

	public ClusterXML(XMLParse xml) {
		this.xml = xml;
		clusters = xml.load(DEFAUL_PATH);
		this.config();
	}
	
	public ClusterXML(XMLParse xml, Rule rule) {
		this.xml = xml;
		clusters = xml.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
		this.config();
	}
}