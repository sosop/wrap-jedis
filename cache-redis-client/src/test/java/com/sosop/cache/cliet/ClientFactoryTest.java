package com.sosop.cache.cliet;

import java.nio.ByteBuffer;

import org.apache.thrift.TException;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

import com.sosop.cache.client.ClientFactory;
import com.sosop.cache.client.Remote.Client;

/**
 * Unit test for simple App.
 */
public class ClientFactoryTest {
	
	private static Client client = ClientFactory.create();
	
	@Test
	public void testSet() throws TException {
		assertThat(client.setS("aota", "thrift-test", "i hope ..."), Matchers.is("OK"));
	}
	
	@Test
	public void testGet() throws TException {
		assertThat(client.get("1", "thrift-test"), Matchers.notNullValue());
	}
	
	@Test
	public void testDel() throws TException {
		assertThat(client.delS("1", "thrift-test"), Matchers.greaterThan(0L));
	}
	
	@Test
	public void testDelB() throws TException {
		ByteBuffer buf = ByteBuffer.wrap("latestPackageApp".getBytes());
		assertThat(client.delB("1", buf), Matchers.greaterThan(0L));
	}
	
}
