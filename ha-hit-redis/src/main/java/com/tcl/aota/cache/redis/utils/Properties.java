package com.tcl.aota.cache.redis.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPoolConfig;

import com.tcl.aota.cache.redis.cluster.Cluster;
import com.tcl.aota.cache.redis.global.settings.Settings;
import com.tcl.aota.cache.redis.node.Node;

public class Properties {

	private final static Logger LOG = Logger.getLogger(Properties.class);

	public Properties() {
	}

	public List<Cluster> load(String filePath) {
		List<Cluster> clusters = null;
		String kvs = FileUtil.read(filePath);
		try {
			clusters = this.parse(kvs);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOG.error(e.getMessage(), e);
		}
		return clusters;
	}

	private List<Cluster> parse(String kvs) throws IllegalAccessException,
			InvocationTargetException {
		String pattern = System.getProperty("os.name").contains("indow") ? "\r\n" : "\n";
		List<Cluster> clusters = new ArrayList<>();
		Cluster cluster = null;
		List<Field> clusterFields = Arrays.asList(Cluster.class
				.getDeclaredFields());
		List<Field> nodeFields = Arrays.asList(Node.class.getDeclaredFields());
		Field[] fields1 = GenericObjectPoolConfig.class.getDeclaredFields();
		Field[] fields2 = BaseObjectPoolConfig.class.getDeclaredFields();
		List<Field> poolFields = new ArrayList<>();
		poolFields.addAll(Arrays.asList(fields1));
		poolFields.addAll(Arrays.asList(fields2));
		List<Field> settings = Arrays.asList(Settings.class.getDeclaredFields());

		String[] lines = kvs.split(pattern);
		int index = 0;
		int length = lines.length;
		String[] kv = null;
		out: while (index < length) {
			if (skip(lines[index])) {
				index++;
				continue;
			}
			if ("[cluster]".equals(lines[index].trim())) {
				cluster = new Cluster();
				clusters.add(cluster);
				index++;
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						autoConfig(cluster, kv[0], kv[1], clusterFields);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}
			}
			if ("[master]".equals(lines[index].trim())) {
				Node master = new Node();
				master.setFlag(Constant.MASTER);
				index++;
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						autoConfig(master, kv[0], kv[1], nodeFields);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}
				cluster.addNode(master);
			}
			if ("[slave]".equals(lines[index].trim())) {
				Node slave = new Node();
				index++;
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						if ("slaveof".equals(kv[0].trim())) {
							slave.setMaster(master(cluster, kv[1]).addSlave(
									slave));
						}
						autoConfig(slave, kv[0], kv[1], nodeFields);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}

			}
			if ("[pool-config]".equals(lines[index].trim())) {
				JedisPoolConfig cfg = new JedisPoolConfig();
				cluster.setConfig(cfg);
				index++;
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						autoConfig(cfg, kv[0], kv[1], poolFields);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}
			}
			if ("[share-pool-config]".equals(lines[index].trim())) {
				JedisPoolConfig cfg = new JedisPoolConfig();
				index++;
				for (Cluster c : clusters) {
					if (c.getConfig() == null) {
						c.setConfig(cfg);
					}
				}
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						autoConfig(cfg, kv[0], kv[1], poolFields);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}
			}
			if ("[settings]".equals(lines[index].trim())) {
				index++;
				while (!lines[index].startsWith("[")) {
					if (!skip(lines[index])
							&& (kv = lines[index].split("=")).length >= 2) {
						autoConfig(Settings.getInstance(), kv[0], kv[1], settings);
					}
					index++;
					if (index >= length) {
						break out;
					}
				}
			}
		}

		return clusters;
	}

	/**
	 * 配置池
	 * 
	 * @param config
	 * @param prefix
	 * @param map
	 */
	private void autoConfig(Object instance, String property, Object value,
			List<Field> fields) {
		for (Field field : fields) {
			String name = field.getName();
			if (name.equals(property)) {
				try {
					BeanUtils.setProperty(instance, name, value);
				} catch (IllegalAccessException | InvocationTargetException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	private boolean skip(String line) {
		if (line.contains("#") || "".equals(line.trim())) {
			return true;
		}
		return false;
	}

	private Node master(Cluster cluster, String name) {
		for (Node node : cluster.getNodes()) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}
}