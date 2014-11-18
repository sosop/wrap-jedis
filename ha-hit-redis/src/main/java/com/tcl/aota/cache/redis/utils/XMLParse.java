package com.tcl.aota.cache.redis.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import redis.clients.jedis.JedisPoolConfig;

import com.tcl.aota.cache.redis.cluster.Cluster;
import com.tcl.aota.cache.redis.exception.SettingsExcetion;
import com.tcl.aota.cache.redis.factory.ClusterFactory;
import com.tcl.aota.cache.redis.factory.NodeFactory;
import com.tcl.aota.cache.redis.global.settings.Settings;
import com.tcl.aota.cache.redis.node.Node;

public class XMLParse {
	private final static Logger LOG = Logger.getLogger(Properties.class);
	private final static String lastFile = "redis-cluster-final.xml";
	
	private SAXBuilder sax;

	public XMLParse() {
		sax = new SAXBuilder();
	}

	public List<Cluster> load(String filename) {
		String loadfile;
		Path path = Paths.get(FileUtil.getPath(lastFile));
		if(Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			loadfile = lastFile;
		} else {
			loadfile = filename;
		}
		return this.parse(loadfile);
	}

	public List<Cluster> parse(String filename) {
		List<Cluster> clusters = new ArrayList<>();
		try {
			Document doc = sax.build(FileUtil.getConfigFile(filename));
			// root element
			Element root = doc.getRootElement();
			if ("clusters".equalsIgnoreCase(root.getName())) {
				Cluster cluster;
				Node node;
				// get cluster elements
				for (Element e : root.getChildren("cluster")) {
					cluster = ClusterFactory.create();
					clusters.add(cluster);
					// auto set cluster attributes
					for (Attribute attr : e.getAttributes()) {
						autoConfig(cluster, attr.getName(), attr.getValue(), fields(Cluster.class));
					}

					// get nodes
					for (Element nodeE : e.getChildren("node")) {
						node = NodeFactory.create();

						for (Element nInfo : nodeE.getChildren()) {
							if ("flag".equalsIgnoreCase(nInfo.getName()) && Constant.MASTER == Integer.parseInt(nInfo.getValue())) {
								cluster.addNode(node);
							}
							if ("slaveof".equalsIgnoreCase(nInfo.getName()) && StringUtil.notNull(nInfo.getValue())) {
								if (Constant.MASTER == node.getFlag()) {
									throw new SettingsExcetion("master should not have slave of other master");
								}
								Node m = master(cluster, nInfo.getValue());
								if (m == null || m.getFlag() == Constant.SLAVE) {
									throw new SettingsExcetion(StringUtil.append("there is no master named ",
											nInfo.getValue(), " and slaves must be after the master"));
								}
								node.setMaster(m.addSlave(node));
							}
							autoConfig(node, nInfo.getName(), nInfo.getValue(), fields(Node.class));
						}
					}

					// get pool setting
					Element poolE = e.getChild("pool-config");
					if (poolE != null) {
						JedisPoolConfig cfg = new JedisPoolConfig();
						cluster.setConfig(cfg);
						for (Element pInfo : poolE.getChildren()) {
							if ("password".equalsIgnoreCase(pInfo.getName()) && StringUtil.isNull(pInfo.getValue())) {
								continue;
							}
							autoConfig(cfg, pInfo.getName(), pInfo.getValue(),
									fields(GenericObjectPoolConfig.class, BaseObjectPoolConfig.class));
						}
					}
				}
				// get global settings
				Element setE = root.getChild("settings");
				if (null != setE) {
					for (Element sInfo : setE.getChildren()) {
						autoConfig(Settings.getInstance(), sInfo.getName(), sInfo.getValue(), fields(Settings.class));
					}
				}
				// all cluster have same pool configuration
				Element sharePoolE = root.getChild("settings");
				if (null != sharePoolE) {
					JedisPoolConfig config = new JedisPoolConfig();
					for (Element spInfo : setE.getChildren()) {
						if ("password".equalsIgnoreCase(spInfo.getName()) && StringUtil.isNull(spInfo.getValue())) {
							continue;
						}
						autoConfig(config, spInfo.getName(), spInfo.getValue(),
								fields(GenericObjectPoolConfig.class, BaseObjectPoolConfig.class));
					}
					for (Cluster c : clusters) {
						if (c.getConfig() == null) {
							c.setConfig(config);
						}
					}
				}
			}
		} catch (JDOMException | IOException | SettingsExcetion e) {
			LOG.error(e.getMessage(), e);
		}
		return clusters;
	}

	/**
	 * @param instance
	 * @param property
	 * @param value
	 * @param fields
	 */
	private void autoConfig(Object instance, String property, Object value, Field[] fields) {
		String name;
		for (Field field : fields) {
			name = field.getName();
			if (name.equals(property)) {
				try {
					BeanUtils.setProperty(instance, name, value);
				} catch (IllegalAccessException | InvocationTargetException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 
	 * @param clazzs
	 * @return
	 */
	@SuppressWarnings("all")
	private static Field[] fields(Class... clazzs) {
		int length = 0;
		Field[][] all = new Field[clazzs.length][];
		for (int i = 0; i < clazzs.length; i++) {
			all[i] = clazzs[i].getDeclaredFields();
			length += all[i].length;
		}
		Field[] fields = new Field[length];
		int offset = 0;
		for (Field[] f : all) {
			System.arraycopy(f, 0, fields, offset, f.length);
			offset += f.length;
		}
		return fields;
	}

	private Node master(Cluster cluster, String name) {
		for (Node node : cluster.getNodes()) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	private static Element nodeToXml(Node node) throws Exception {
		Element n = new Element("node");
		String property;
		for (Field f : fields(Node.class)) {
			property = f.getName();
			if (!(property.equals("master") || property.equals("slaves") || property.equals("MAX_SLAVES"))) {
				Element nf = new Element(property);
				nf.addContent(BeanUtils.getProperty(node, f.getName()));
				n.addContent(nf);
			}
		}
		if (node.getFlag() == Constant.SLAVE) {
			Element nf = new Element("slaveof");
			nf.addContent(node.getMaster().getName());
			n.addContent(nf);
		}
		return n;
	}
	
	private static Element poolToXml(JedisPoolConfig config) throws Exception {
		Element n = new Element("pool-config");
		String property;
		String value;
		for (Field f : fields(GenericObjectPoolConfig.class, BaseObjectPoolConfig.class)) {
			property = f.getName();
			if (!property.startsWith("DEFAULT") && StringUtil.notNull(value = BeanUtils.getProperty(config, f.getName()))) {
				Element nf = new Element(property);
				nf.addContent(value);
				n.addContent(nf);
			}
		}
		return n;
	}

	public static void toXML(List<Cluster> clusters) throws Exception {
		Element root = new Element("clusters");
		// cluster element
		for (Cluster cluster : clusters) {
			Element c = new Element("cluster");
			c.setAttribute("name", cluster.getName());
			c.setAttribute("serial", String.valueOf(cluster.getSerial()));
			c.setAttribute("weight", String.valueOf(cluster.getWeight()));
			// add node
			for (Node master : cluster.getNodes()) {
				c.addContent(nodeToXml(master));
				for (Node slave : master.getSlaves()) {
					c.addContent(nodeToXml(slave));
				}
			}
			c.addContent(poolToXml(cluster.getConfig()));
			root.addContent(c);
		}
		// setting element
		Element settings = new Element("settings");
		Element  replication = new Element("replication");
		replication.addContent(String.valueOf(Settings.getInstance().isReplication()));
		Element  hitCount = new Element("hitCount");
		hitCount.addContent(String.valueOf(Settings.getInstance().isHitCount()));
		settings.addContent(replication);
		settings.addContent(hitCount);
		root.addContent(settings);
		
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		out.output(doc, new FileOutputStream(FileUtil.getPath("redis-cluster-final.xml")));
		
	}
}