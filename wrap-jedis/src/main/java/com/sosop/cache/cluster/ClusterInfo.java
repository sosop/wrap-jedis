package com.sosop.cache.cluster;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import com.sosop.cache.rule.DefaultRule;
import com.sosop.cache.rule.Rule;
import com.sosop.cache.utils.Properties;
import com.sosop.cache.utils.StringUtil;

public class ClusterInfo {

	private final static Logger LOG = Logger.getLogger(ClusterInfo.class);

	private final static String DEFAUL_PATH = "redis-cluster.properties";

	private Properties prop;

	private List<Cluster> clusters;
	
	private Map<String, Cluster> nameMap;
	
	private Map<Integer, Cluster> numMap;
	
	private Rule rule;

	public ClusterInfo() {
		this.prop = new Properties();
		prop.load(DEFAUL_PATH);
	}
	
	public ClusterInfo(Rule rule) {
		this.prop = new Properties();
		prop.load(DEFAUL_PATH);
		this.rule = rule.initial(this);
	}

	public ClusterInfo(String path) {
		this.prop = new Properties();
		prop.load(path);
	}
	
	public ClusterInfo(String path, Rule rule) {
		this.prop = new Properties();
		prop.load(path);
		this.rule = rule.initial(this);
	}

	public ClusterInfo(Properties prop) {
		this.prop = prop;
	}
	
	public ClusterInfo(Properties prop, Rule rule) {
		this.prop = prop;
		this.rule = rule.initial(this);
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public Map<String, Cluster> getNameMap() {
		return nameMap;
	}

	public Map<Integer, Cluster> getNumMap() {
		return numMap;
	}

	public void config() {
		/*clusters = new ArrayList<>();
		nameMap  = new HashMap<>();
		numMap   = new HashMap<>();

		// Map<String, String> info = prop.getContainerMap();

		JedisPoolConfig shareConfig = null;

		// 获得集群数
		int clusterCount = 0;

		for (String key : info.keySet()) {
			if ("[cluster]".equalsIgnoreCase(key)) {
				clusterCount++;
			}
			if ("[share-pool]".equalsIgnoreCase(key)) {
				shareConfig = new JedisPoolConfig();
				poolConfig(shareConfig, "c", info);
			}
		}

		// 获得每个集群的配置
		for (int i = 1; i <= clusterCount; i++) {
			// pool info
			Cluster cluster = new Cluster();
			if (shareConfig != null) {
				cluster.setConfig(shareConfig);
			} else {
				JedisPoolConfig config = new JedisPoolConfig();
				poolConfig(config, StringUtil.append("c", i),
						info);
				cluster.setConfig(config);
			}
			// 获取 server 数量
			List<JedisShardInfo> servers = new ArrayList<>();
			int serverCount = Integer.parseInt(info.get(StringUtil.append("c",
					i, ".server.count")));
			for (int j = 1; j <= serverCount; j++) {
				String serverKey = StringUtil.append("c", i,
						".s", j, ".");
				JedisShardInfo server = new JedisShardInfo(
						info.get(StringUtil.append(serverKey, "host")), 
						Integer.parseInt(info.get(StringUtil.append(serverKey, "port"))),
						Integer.parseInt(info.get(StringUtil.append(serverKey,"timeout"))), 
						Integer.parseInt(info.get(StringUtil.append(serverKey, "weight"))));
				String passwd = info.get(StringUtil.append(serverKey, "password"));
				if(!StringUtil.isNull(passwd)) {
					server.setPassword(passwd);
				}
				servers.add(server);
			}
			// cluster.setNodes(servers);
			cluster.wire();
			
			// 获得编号和名称
			cluster.setName(info.get(StringUtil.append("c", i, ".name")));
			cluster.setSerial(Integer.parseInt(info.get(StringUtil.append("c", i, ".num"))));
			
			// 获得虚拟节点
			String weight = info.get(StringUtil.append("c", i, ".weight"));
			if(!StringUtil.isNull(weight)) {
				cluster.setSerial(Integer.parseInt(weight));
			}
			
			*//**
			 * 添加映射信息
			 *//*
			nameMap.put(cluster.getName(), cluster);
			numMap.put(cluster.getSerial(), cluster);
			clusters.add(cluster);
		}*/
		if(null == this.rule) {
			rule = new DefaultRule().initial(this);
		}
	}

	/*public ShardedJedis getJedis(Object key) {
		return rule.getCluster(key).getJedis();
	}
	
	public ShardedJedis getJedis(String clusterName) {
		return nameMap.get(clusterName).getJedis();
	}
	
	public ShardedJedis getJedis(int clusterNum) {
		return numMap.get(clusterNum).getJedis();
	}*/
	
	/**
	 * 配置池
	 * @param config
	 * @param prefix
	 * @param map
	 */
	private void poolConfig(JedisPoolConfig config, String prefix,
			Map<String, String> map) {

		Field[] fields1 = GenericObjectPoolConfig.class.getDeclaredFields();
		Field[] fields2 = BaseObjectPoolConfig.class.getDeclaredFields();

		List<Field> fields = new ArrayList<>();
		fields.addAll(Arrays.asList(fields1));
		fields.addAll(Arrays.asList(fields2));

		for (Field field : fields) {
			String name = field.getName();
			String key = StringUtil.append(prefix, ".pool.", name);
			if (map.containsKey(key)) {
				try {
					BeanUtils.setProperty(config, name, map.get(key));
				} catch (IllegalAccessException | InvocationTargetException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}

	}
}