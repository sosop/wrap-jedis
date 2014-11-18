package com.tcl.aota.cache.redis.utils;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.tcl.aota.cache.redis.cluster.Cluster;

public class PropertiesTest {

	private Properties prop;

	@Before
	public void init() {
		prop = new Properties();
	}

	@Test
	public void testLoad() {
		List<Cluster> clusters = prop.load("redis-cluster.properties");
		assertThat(clusters.size(), Matchers.greaterThanOrEqualTo(1));
	}
}
