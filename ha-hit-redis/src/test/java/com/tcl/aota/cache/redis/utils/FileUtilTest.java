package com.tcl.aota.cache.redis.utils;

import static org.junit.Assert.assertThat;


import org.hamcrest.Matchers;
import org.junit.Test;

public class FileUtilTest {
	
	
	@Test
	public void testRead() {
		String setting = FileUtil.read("redis-cluster.properties");
		assertThat(setting, Matchers.notNullValue());
	}
}
