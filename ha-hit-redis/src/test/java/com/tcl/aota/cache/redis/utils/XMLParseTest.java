package com.tcl.aota.cache.redis.utils;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.tcl.aota.cache.redis.cluster.Cluster;


public class XMLParseTest {
	private XMLParse xml;

	@Before
	public void init() {
		xml = new XMLParse();
	}

	@Test
	public void testLoad() {
		List<Cluster> clusters = xml.load("redis-cluster.xml");
		assertThat(clusters.size(), Matchers.greaterThanOrEqualTo(1));
		
		Cluster c = clusters.get(0);
		assertThat(c.getName(), Matchers.notNullValue());
		assertThat(c.getSerial(), Matchers.not(0));
		assertThat(c.getWeight(), Matchers.not(0));
		assertThat(c.getConfig(), Matchers.notNullValue());
		assertThat(c.getConfig().getTimeBetweenEvictionRunsMillis(), Matchers.is(60000L));
		assertThat(c.getNodes().get(0).getHost(), Matchers.notNullValue());
		assertThat(c.getNodes().get(0).getPort(), Matchers.not(0));
	}
}
