package com.tcl.aota.cache.redis.heartbeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import redis.clients.jedis.Protocol;

import com.tcl.aota.cache.redis.cluster.Cluster;
import com.tcl.aota.cache.redis.node.Node;
import com.tcl.aota.cache.redis.utils.StringUtil;
import com.tcl.aota.cache.redis.utils.XMLParse;

public class HeartBeat extends Thread {
	private final static Logger LOG = Logger.getLogger(HeartBeat.class);
	private final static HeartBeat beat = new HeartBeat();
	private Map<Cluster, Map<Node, NodeConnection>> cNodes;
	private Map<Cluster, Map<Node, NodeConnection>> failNodes;
	private Map<Node, NodeConnection> failSlaveNodes;
	private Map<Node, Integer> failNodeSerial;
	private List<Cluster> clusters;

	private HeartBeat() {
	}

	public static HeartBeat get() {
		return beat;
	}

	public HeartBeat init(List<Cluster> clusters) {
		cNodes = new ConcurrentHashMap<>();
		failNodes = new ConcurrentHashMap<>(6);
		failNodeSerial = new ConcurrentHashMap<>();
		failSlaveNodes = new ConcurrentHashMap<>();
		this.clusters = clusters;
		Map<Node, NodeConnection> nConn;
		NodeConnection conn;
		for (Cluster cluster : clusters) {
			nConn = new HashMap<>(cluster.getNodes().size());
			for (Node master : cluster.getNodes()) {
				conn = new NodeConnection(master.getHost(), master.getPort());
				conn.connect();
				if (isConnected(conn)) {
					slaveofNoOne(conn);
				}
				nConn.put(master, conn);
				List<Node> slaves = master.getSlaves();
				for (Node slave : slaves) {
					NodeConnection sConn = new NodeConnection(slave.getHost(), slave.getPort());
					sConn.connect();
					if (isConnected(sConn)) {
						slaveof(sConn, master.getHost(), master.getPort());
					}
					nConn.put(slave, sConn);
				}
			}
			cNodes.put(cluster, nConn);
		}
		conn = null;
		return this;
	}

	@Override
	public void run() {
		while (true) {
			if (failOver() || recoveryFail()) {
				for (Cluster c : clusters) {
					c.wireFault();
				}
				try {
					XMLParse.toXML(clusters);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
			recoverySlave();
			// every sec
			// LockSupport.parkNanos(3000000L);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private boolean failOver() {
		boolean resetConfig = false;
		for (Entry<Cluster, Map<Node, NodeConnection>> entry : cNodes.entrySet()) {
			Cluster cluster = entry.getKey();
			for (Entry<Node, NodeConnection> e : entry.getValue().entrySet()) {
				Node node = e.getKey();
				int index = cluster.getIndex(node);
				// it's not a master
				if (index < 0) {
					continue;
				}
				// master is connneted
				if (!isConnected(e.getValue())) {
					resetConfig = autoPromotionAndRmFail(cluster, node, index, e.getValue());
				}
			}
		}
		return resetConfig;
	}

	/**
	 * auto promotion
	 * 
	 * @param node
	 */

	private boolean autoPromotionAndRmFail(Cluster cluster, Node failMaster, int index, NodeConnection conn) {
		boolean opt = false;
		Node slave = null;
		try {
			for (int i = 0; i < failMaster.getSlaves().size(); i++) {
				slave = failMaster.getSlaves().get(i);
				if (isConnected(cNodes.get(cluster).get(slave))) {
					opt = true;
					break;
				}
			}
			// is node a master
			if (opt && slave.switchRole()) {
				slaveofNoOne(cNodes.get(cluster).get(slave));
				slaveInit(cluster, slave.getSlaves());
				cluster.setNode(index, slave);
				failSlaveNodes.put(failMaster, conn);
			} else {
				// cluster remove the fail node
				cluster.remove(failMaster);
				if (failNodes.get(cluster) == null) {
					failNodes.put(cluster, new HashMap<Node, NodeConnection>());
				}
				// put it in fail contain
				failNodes.get(cluster).put(failMaster, conn);
				// record the index, and after recovery put it
				// in
				failNodeSerial.put(failMaster, index);
				// don't check it on this method before recovery
				cNodes.get(cluster).remove(failMaster);
				opt = true;
			}
		} catch (Exception e) {
		}
		return opt;
	}

	/**
	 * this who has not slave recovery
	 * 
	 * @return
	 */

	private boolean recoveryFail() {
		boolean resetConfig = false;
		Cluster cluster;
		Node node;
		for (Entry<Cluster, Map<Node, NodeConnection>> fails : failNodes.entrySet()) {
			cluster = fails.getKey();
			for (Entry<Node, NodeConnection> nc : fails.getValue().entrySet()) {
				node = nc.getKey();
				try{
					nc.getValue().connect();
				} catch(Exception e) {
					LOG.error(StringUtil.append(node.toAddr(), " still disconnected"), e);
				}
				if (isConnected(nc.getValue())) {
					cluster.addNode(failNodeSerial.get(node), node);
					cNodes.get(cluster).put(node, nc.getValue());
					failNodes.remove(fails.getKey());
					failNodeSerial.remove(node);
					resetConfig = true;
				}
			}
		}
		node = null;
		cluster = null;
		return resetConfig;
	}

	/**
	 * this is master down to slave and after recovery slave to it's master
	 */

	private void recoverySlave() {
		Node node;
		for (Entry<Node, NodeConnection> entry : failSlaveNodes.entrySet()) {
			node = entry.getKey();
			try {
				entry.getValue().connect();
			} catch(Exception e) {
				LOG.error(StringUtil.append(node.toAddr(), " still disconnected"), e);
			}
			if (isConnected(entry.getValue()) && node.getMaster() != null) {
				slaveof(entry.getValue(), node.getMaster().getHost(), node.getMaster().getPort());
				failSlaveNodes.remove(node);
			}
		}
		node = null;
	}

	/**
	 * this jedis is connected
	 * 
	 * @param jedis
	 * @return
	 */
	private boolean isConnected(NodeConnection conn) {
		boolean result = false;
		try {
			result = (conn.ping().equals("PONG"));
		} catch (Exception e) {
			result = false;
			conn.disconnect();
		}
		return result;
	}

	/**
	 * slaves replicate from master
	 * 
	 * @param c
	 * @param slaves
	 */
	private void slaveInit(Cluster c, List<Node> slaves) {
		Map<Node, NodeConnection> nodes = cNodes.get(c);
		NodeConnection conn;
		for (Node slave : slaves) {
			if ((conn = nodes.get(slave)) != null) {
				if (isConnected(conn)) {
					slaveof(conn, slave.getMaster().getHost(), slave.getMaster().getPort());
				}
			}
		}
	}

	private String slaveof(NodeConnection conn, String host, int port) {
		conn.sendCommand(Protocol.Command.SLAVEOF, host, String.valueOf(port));
		return conn.getStatusCodeReply();
	}

	private String slaveofNoOne(NodeConnection conn) {
		conn.sendCommand(Protocol.Command.SLAVEOF, new byte[][] { Protocol.Keyword.NO.raw, Protocol.Keyword.ONE.raw });
		return conn.getStatusCodeReply();
	}
}