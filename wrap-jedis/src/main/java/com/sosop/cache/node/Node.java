package com.sosop.cache.node;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private final static int MAX_SLAVES = 8;
	
	private String name;
	private String host;
	private int port;
	private int timeout;
	private int weight;
	private String password;
	private int flag;
	private List<Node> slaves;
	private Node master;

	public Node() {
		this.slaves = new ArrayList<>(MAX_SLAVES);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public List<Node> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<Node> slaves) {
		this.slaves = slaves;
	}
	
	public Node addSlave(Node slave) {
		this.slaves.add(slave);
		return this;
	}

	public Node getMaster() {
		return master;
	}

	public void setMaster(Node master) {
		this.master = master;
	}
}
