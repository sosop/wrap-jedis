package com.sosop.cache.node;

public class SlaveNode extends Node {
	private MasterNode master;

	public MasterNode getMaster() {
		return master;
	}

	public void setMaster(MasterNode master) {
		this.master = master;
	}
	
	public boolean toMaster() {
		return false;
	}
	
	public MasterNode toSlave(MasterNode node) {
		MasterNode master = new MasterNode();
		this.master = null;
		return master;
	}
}
