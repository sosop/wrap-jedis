package com.sosop.cache.redis.thrift;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

import com.sosop.cache.redis.cluster.ClusterInfo;
import com.sosop.cache.redis.cluster.ClusterXML;
import com.sosop.cache.redis.utils.ObjectUtil;


public class RemoteImp implements Remote.Iface {

	private final static ClusterInfo info = new ClusterXML();

	@Override
	public String setS(String cluster, String key, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).set(key, value);
	}

	@Override
	public String get(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).get(key);
	}

	@Override
	public long delS(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).del(key);
	}

	@Override
	public String lpopList(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lpopList(key);
	}

	@Override
	public long rpushList(String cluster, String key, String values) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).rpushList(key, values);
	}

	@Override
	public long expire(String cluster, String key, int time) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).expire(key, time);
	}

	@Override
	public long hsetnx(String cluster, String key, String field, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hsetnx(key, field, value);
	}

	@Override
	public boolean exist(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).exist(key);
	}

	@Override
	public boolean existInSet(String cluster, String key, String member) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).existInSet(key, member);
	}

	@Override
	public long saddSet(String cluster, String key, List<String> members) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).saddSet(key, ObjectUtil.CollectTo(members));
	}

	@Override
	public long sremSet(String cluster, String key, List<String> members) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).sremSet(key, ObjectUtil.CollectTo(members));
	}

	@Override
	public String spopSet(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).spopSet(key);
	}

	@Override
	public long hSet(String cluster, ByteBuffer key, ByteBuffer field, ByteBuffer value)
			throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hSet(key.array(), field.array(), value.array());
	}

	@Override
	@SuppressWarnings("all")
	public Map hGetAll(String cluster, ByteBuffer key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hGetAll(key.array());
	}

	@Override
	public ByteBuffer hGet(String cluster, ByteBuffer key, ByteBuffer field) throws TException {
		return ByteBuffer.wrap(info.cluster(ObjectUtil.object(cluster)).hGet(key.array(), field.array()));
	}

	@Override
	public long delB(String cluster, ByteBuffer key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).del(key.array());
	}
}