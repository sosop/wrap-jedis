package com.tcl.aota.cache.redis.cluster;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.tcl.aota.cache.redis.rule.ClusterSerialRule;
import com.tcl.aota.cache.redis.utils.XMLParse;

public class ClusterXmlTest {
	private ClusterInfo infoXml;

	@Before
	@SuppressWarnings("all")
	public void init() {
		infoXml = new ClusterXML(new ClusterSerialRule() {
			@Override
			public int getSerial(Object condition) {
				return (int) condition;
			}
		});
	}

	@Test
	public void testCluster() {
		
		Cluster cx1 = infoXml.cluster("aota");
		
		assertThat(cx1, Matchers.is(infoXml.getClusters().get(0)));
		assertThat(cx1.getNodes().get(0).getSlaves().size(), Matchers.greaterThanOrEqualTo(1));
		assertThat(cx1.getNodes().get(0).getMaster(), Matchers.nullValue());
		assertThat(cx1.getNodes().get(0).getFlag(), Matchers.is(1));
		
		/*Cluster c2 = infoProp.cluster("mycluster2");
		assertThat(c2, Matchers.is(infoProp.getClusters().get(1)));*/
		
		Cluster cx3 = infoXml.cluster(1);
		assertThat(cx3, Matchers.is(infoXml.getClusters().get(0)));
		
		/*Cluster c4 = infoProp.cluster(2);
		assertThat(c4, Matchers.is(infoProp.getClusters().get(1)));*/
		
		Cluster cx5 = infoXml.rule(1);
		assertThat(cx5, Matchers.is(infoXml.getClusters().get(0)));

		/*Cluster c6 = infoProp.rule("mycluster2");
		assertThat(c6, Matchers.is(infoProp.getClusters().get(1)));*/
	}	
		
	@Test
	public void testXMLRedisOpt() {
		Cluster cx1 = infoXml.cluster(1);
		assertThat(cx1.set("c1", "xml-cluster1"), Matchers.is((Object)"OK"));
		assertThat(cx1.get("c1"), Matchers.is((Object)"xml-cluster1"));
		try {
			XMLParse.toXML(infoXml.getClusters());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
