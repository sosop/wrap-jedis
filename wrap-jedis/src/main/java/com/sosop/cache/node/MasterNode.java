package com.sosop.cache.node;

import java.util.ArrayList;
import java.util.List;

public class MasterNode extends Node {
	
	private final static int MAX_SLAVE = 8;
	
	private List<SlaveNode> slaves;
	
	public MasterNode() {
		this.slaves = new ArrayList<>(MAX_SLAVE);
	}
	

	public List<SlaveNode> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<SlaveNode> slaves) {
		this.slaves = slaves;
	}
	
	public MasterNode addSlave(SlaveNode slave) {
		return this.slaves.add(slave) ? this : null;
	}
	
	public SlaveNode toSlave(MasterNode node) {
		SlaveNode slave = new SlaveNode();
		this.slaves = null;
		return slave;
	}


	@Override
	public String toString() {
		return super.toString() + " MasterNode [slaves=" + slaves.size() + "]";
	}
	
	
}
