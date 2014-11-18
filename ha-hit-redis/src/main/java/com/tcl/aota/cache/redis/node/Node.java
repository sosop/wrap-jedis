package com.tcl.aota.cache.redis.node;

import java.util.ArrayList;
import java.util.List;

import com.tcl.aota.cache.redis.utils.Constant;
import com.tcl.aota.cache.redis.utils.StringUtil;

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

	public boolean switchRole() {
		boolean isSwitch = false;
		if(this.flag == Constant.SLAVE){ 
			this.master.flag = Constant.SLAVE;
			this.master.slaves.remove(this);
			
			this.flag = Constant.MASTER;
			this.slaves.addAll(this.master.slaves);
			this.slaves.add(this.master);
			
			for (Node slave : slaves) {
				slave.master = this;
			}
			isSwitch = true;
		}
		return isSwitch;
	}
	
	public String toAddr() {
		return StringUtil.append(host, ":", port);
	}
}
