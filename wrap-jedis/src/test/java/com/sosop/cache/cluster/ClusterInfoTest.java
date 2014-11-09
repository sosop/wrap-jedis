package com.sosop.cache.cluster;

import static org.junit.Assert.assertThat;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.sosop.cache.rule.ClusterNameRule;

public class ClusterInfoTest {
	private ClusterInfo info;

	@Before
	public void init() {
		info = new ClusterInfo(new ClusterNameRule() {
			@Override
			public String getName(Object condition) {
				return String.valueOf(condition);
			}
		});
	}

	@Test
	public void testCluster() {
		Cluster c1 = info.cluster("mycluster1");
		assertThat(c1, Matchers.is(info.getClusters().get(0)));
		assertThat(c1.getNodes().get(0).getSlaves().size(), Matchers.greaterThanOrEqualTo(1));
		assertThat(c1.getNodes().get(0).getMaster(), Matchers.nullValue());
		assertThat(c1.getNodes().get(0).getFlag(), Matchers.is(1));
		
		Cluster c2 = info.cluster("mycluster2");
		assertThat(c2, Matchers.is(info.getClusters().get(1)));
		
		Cluster c3 = info.cluster(1);
		assertThat(c3, Matchers.is(info.getClusters().get(0)));
		
		Cluster c4 = info.cluster(2);
		assertThat(c4, Matchers.is(info.getClusters().get(1)));
		
		Cluster c5 = info.rule("mycluster1");
		assertThat(c5, Matchers.is(info.getClusters().get(0)));
		
		Cluster c6 = info.rule("mycluster2");
		assertThat(c6, Matchers.is(info.getClusters().get(1)));
	}	
}
