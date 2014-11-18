package com.tcl.aota.cache.redis.cluster;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.tcl.aota.cache.redis.rule.ClusterNameRule;

public class ClusterPropTest {
	private ClusterInfo infoProp;

	@Before
	@SuppressWarnings("all")
	public void init() {
		infoProp = new ClusterProp(new ClusterNameRule() {
			@Override
			public String getName(Object condition) {
				return String.valueOf(condition);
			}
		});
	}

	@Test
	public void testCluster() {
		Cluster c1 = infoProp.cluster("mycluster1");
		assertThat(c1, Matchers.is(infoProp.getClusters().get(0)));
		assertThat(c1.getNodes().get(0).getSlaves().size(), Matchers.greaterThanOrEqualTo(1));
		assertThat(c1.getNodes().get(0).getMaster(), Matchers.nullValue());
		assertThat(c1.getNodes().get(0).getFlag(), Matchers.is(1));
		
		/*Cluster c2 = infoProp.cluster("mycluster2");
		assertThat(c2, Matchers.is(infoProp.getClusters().get(1)));*/
		
		Cluster c3 = infoProp.cluster(1);
		assertThat(c3, Matchers.is(infoProp.getClusters().get(0)));
		
		/*Cluster c4 = infoProp.cluster(2);
		assertThat(c4, Matchers.is(infoProp.getClusters().get(1)));*/
		
		Cluster c5 = infoProp.rule("mycluster1");
		assertThat(c5, Matchers.is(infoProp.getClusters().get(0)));

		/*Cluster c6 = infoProp.rule("mycluster2");
		assertThat(c6, Matchers.is(infoProp.getClusters().get(1)));*/
	}	
	
	
	@Test
	public void testRedisOpt() {
		Cluster c1 = infoProp.cluster(1);
		assertThat(c1.set("c1", "cluster1"), Matchers.is((Object)"OK"));
		assertThat(c1.get("c1"), Matchers.is((Object)"cluster1"));
		/*Cluster c2 = infoProp.cluster(2);
		assertThat(c2.set("c2", "cluster2"), Matchers.is((Object)"OK"));
		assertThat(c2.get("c2"), Matchers.is((Object)"cluster2"));*/
	}
	
}
