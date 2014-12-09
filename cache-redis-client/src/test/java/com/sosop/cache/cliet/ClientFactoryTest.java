package com.sosop.cache.cliet;

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
		assertThat(client.sets("aota", "my-test", "just a test"), Matchers.is("OK"));
	}

	@Test
	public void testGet() throws TException {
		assertThat(client.get("1", "thrift-jedis"), Matchers.notNullValue());
	}

	@Test
	public void testDel() throws TException {
		assertThat(client.dels("1", "thrift-test"), Matchers.greaterThan(0L));
	}

}
