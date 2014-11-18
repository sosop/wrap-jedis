package com.tcl.aota.cache.redis.cluster;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import com.tcl.aota.cache.redis.command.Command;
import com.tcl.aota.cache.redis.node.Node;

/**
 * 
 * @author sosop
 * @date 2014.9.18 redis集群类， 包含一个redis池， 集群服务器
 * 
 */
public class Cluster extends Command {

	public final static int DEFAULT_WEIGHT = 16;
	public final static int MAX_NODES      = 10;

	private String name;
	private int serial;
	private int weight;
	private JedisPoolConfig config;
	private List<Node> nodes;
	private List<JedisShardInfo> shards;

	public Cluster() {
		this.weight = DEFAULT_WEIGHT;
		this.nodes = new ArrayList<>(MAX_NODES);
		this.shards = new ArrayList<>(MAX_NODES);
	}

	/**
	 * 配置集群资源
	 */
	public void wire() {
		if (null == pool) {
			synchronized (this) {
				if (null == pool) {
					nodesToShards();
					this.pool = new ShardedJedisPool(config, shards);
				}
			}
		}
	}
	
	public void wireFault() {
		shards.clear();
		nodesToShards();
		this.pool = new ShardedJedisPool(config, shards);
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConfig(JedisPoolConfig config) {
		this.config = config;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public boolean addNode(Node node) {
		return this.nodes.add(node);
	}
	
	public void addNode(int index, Node node) {
		this.nodes.add(index, node);
	}
	
	public void setNode(int index, Node node) {
		this.nodes.set(index, node);
	}
	
	public int getIndex(Node node) {
		return this.nodes.indexOf(node);
	}
	
	public Node remove(int index) {
		return this.nodes.remove(index);
	}
	
	public boolean remove(Node node) {
		return this.nodes.remove(node);
	}

	public JedisPoolConfig getConfig() {
		return config;
	}
	
	private void nodesToShards() {
		shards.clear();
		for (Node master : nodes) {
			JedisShardInfo info = new JedisShardInfo(master.getHost(), master.getPort(), master.getTimeout(), master.getWeight());
			shards.add(info);
		}
	}
}