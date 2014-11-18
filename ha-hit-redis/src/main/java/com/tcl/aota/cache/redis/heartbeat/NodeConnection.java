package com.tcl.aota.cache.redis.heartbeat;

import redis.clients.jedis.Connection;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Protocol.Command;

public class NodeConnection extends Connection {
	public NodeConnection(String host, int port) {
		super(host, port);
	}
	
	public NodeConnection(String host, int port, int timeout) {
		super(host, port);
	}

	@Override
	protected Connection sendCommand(Command cmd, String... args) {
		return super.sendCommand(cmd, args);
	}

	@Override
	protected Connection sendCommand(Command cmd, byte[]... args) {
		return super.sendCommand(cmd, args);
	}

	@Override
	protected String getStatusCodeReply() {
		return super.getStatusCodeReply();
	}
	
	protected String ping() {
		super.sendCommand(Protocol.Command.PING);
		return super.getStatusCodeReply();
	}
}
