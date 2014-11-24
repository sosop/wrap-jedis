package com.sosop.cache.redis.heartbeat;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import redis.clients.jedis.Protocol;

import com.sosop.cache.redis.cluster.Cluster;
import com.sosop.cache.redis.node.Node;
import com.sosop.cache.redis.utils.Constant;
import com.sosop.cache.redis.utils.StringUtil;
import com.sosop.cache.redis.utils.XMLParse;

public class HeartBeat {
	private final Logger LOG = Logger.getLogger(HeartBeat.class);
	private final static HeartBeat beat = new HeartBeat();
	private Map<Node, NodeConnection> nodes;
	private List<Node> fails;
	private List<Cluster> clusters;

	private HeartBeat() {
	}

	public static HeartBeat get() {
		return beat;
	}

	// 初始化
	public HeartBeat init(List<Cluster> clusters) {
		this.clusters = clusters;
		this.nodes = new LinkedHashMap<>();
		this.fails = new LinkedList<>();
		NodeConnection conn;
		for (Cluster cluster : clusters) {
			// 每个集群的master
			for (Node master : cluster.getNodes()) {
				conn = new NodeConnection(master.getHost(), master.getPort());
				conn.connect();
				if (isConnected(conn)) {
					slaveofNoOne(conn);
				}
				nodes.put(master, conn);
				// 每个master的slave
				for (Node slave : master.getSlaves()) {
					conn = new NodeConnection(slave.getHost(), slave.getPort());
					conn.connect();
					if (isConnected(conn)) {
						slaveof(conn, master.getHost(), master.getPort());
					}
					nodes.put(slave, conn);
				}
			}
		}
		conn = null;
		return this;
	}

	public void promotion() {
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
	}

	// 检测是否宕
	private boolean failOver() {
		boolean resetConfig = false;
		Node failMaster;
		for (Entry<Node, NodeConnection> e : nodes.entrySet()) {
			failMaster = e.getKey();
			// it's not a master
			if (failMaster.getFlag() == Constant.SLAVE || fails.contains(failMaster)) {
				continue;
			}
			// master is connneted
			if (!isConnected(e.getValue())) {
				resetConfig = autoPromotionAndRmFail(failMaster, e.getValue());
			}

		}
		failMaster = null;
		return resetConfig;
	}

	/**
	 * auto promotion
	 * 
	 * @param node
	 */

	private boolean autoPromotionAndRmFail(Node failMaster, NodeConnection conn) {
		Node newMaster = null;
		int index = -1;
		try {
			for (Node node : failMaster.getSlaves()) {
				if (isConnected(nodes.get(node))) {
					newMaster = node;
					break;
				}
			}
			index = failMaster.getCluster().getIndex(failMaster);
			// is node a master
			if (null != newMaster && newMaster.switchRole()) {
				slaveofNoOne(nodes.get(newMaster));
				slaveInit(newMaster);
				newMaster.getCluster().setNode(index, newMaster);
			} else {
				// cluster remove the fail node
				failMaster.getCluster().remove(index);
			}
			fails.add(failMaster);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			newMaster = null;
		}
	}

	/**
	 * this who has not slave recovery
	 * 
	 * @return
	 */
	private boolean recoveryFail() {
		Node fail;
		NodeConnection conn;
		Iterator<Node> it = fails.iterator();
		boolean wire = false;
		while (it.hasNext()) {
			fail = it.next();
			conn = nodes.get(fail);
			try {
				conn.connect();
			} catch (Exception e) {
				LOG.error(StringUtil.append(fail.toAddr(), " still disconnected"));
				continue;
			}
			if (isConnected(conn)) {
				if (fail.getFlag() == Constant.MASTER) {
					fail.getCluster().setNode(fail.getCluster().getIndex(fail), fail);
					wire = true;
				} else if (fail.getFlag() == Constant.SLAVE) {
					slaveof(conn, fail.getMaster().getHost(), fail.getMaster().getPort());
				}
				fails.remove(fail);
			}
		}
		return wire;
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
	private void slaveInit(Node master) {
		NodeConnection conn;
		for (Node slave : master.getSlaves()) {
			if ((conn = nodes.get(slave)) != null) {
				if (isConnected(conn)) {
					slaveof(conn, master.getHost(), master.getPort());
				}
			}
		}
	}

	private String slaveof(NodeConnection conn, String host, int port) {
		conn.sendCommand(Protocol.Command.SLAVEOF, host, String.valueOf(port));
		return conn.getStatusCodeReply();
	}

	private String slaveofNoOne(NodeConnection conn) {
		conn.sendCommand(Protocol.Command.SLAVEOF, new byte[][] { Protocol.Keyword.NO.raw,
				Protocol.Keyword.ONE.raw });
		return conn.getStatusCodeReply();
	}
}