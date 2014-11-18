package com.sosop.cache.redis.thrift;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.thrift.TException;

import com.sosop.cache.redis.cluster.ClusterInfo;
import com.sosop.cache.redis.cluster.ClusterXML;


public class RemoteImp implements Remote.Iface {

	private final static ClusterInfo info = new ClusterXML();
	
	@Override
	public String setS(ByteBuffer cluster, String key, String value) throws TException {
		return info.cluster(cluster).set(key, value);
	}

	@Override
	public String get(ByteBuffer cluster, String key) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long delS(ByteBuffer cluster, String key) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String lpopList(ByteBuffer cluster, String key) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long rpushList(ByteBuffer cluster, String key, String values) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long expire(ByteBuffer cluster, String key, int time) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long hsetnx(ByteBuffer cluster, String key, String field, String value)
			throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exist(ByteBuffer cluster, String key) throws TException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existInSet(ByteBuffer cluster, String key, String member) throws TException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long saddSet(ByteBuffer cluster, String key, ByteBuffer members) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long sremSet(ByteBuffer cluster, String key, ByteBuffer members) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String spopSet(ByteBuffer cluster, String key) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long hSet(ByteBuffer cluster, ByteBuffer key, ByteBuffer field, ByteBuffer value)
			throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<ByteBuffer, ByteBuffer> hGetAll(ByteBuffer cluster, ByteBuffer key)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ByteBuffer hGet(ByteBuffer cluster, ByteBuffer key, ByteBuffer field) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long delB(ByteBuffer cluster, ByteBuffer key) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}
	
}