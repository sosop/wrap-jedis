package com.sosop.cache.redis.command;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.sosop.cache.redis.global.settings.Settings;
import com.sosop.cache.redis.statistics.Hits;

public abstract class Command {
	private final static Logger LOG = Logger.getLogger(Command.class);

	private final static MethodAccess access = MethodAccess.get(ShardedJedis.class);

	protected ShardedJedisPool pool;

	public String set(String key, String value) {
		return String.valueOf(this.invoke("set", new String[] { key, value }, new Class[] {
				String.class, String.class }));
	}

	public String get(String key) {
		return String.valueOf(this.invoke("get", new String[] { key }, String.class));
	}

	public long del(String key) {
		return (long) this.invoke("del", new String[] { key }, String.class);
	}

	public String lpopList(String key) {
		return String.valueOf(this.invoke("lpop", new String[] { key }, String.class));
	}

	public long rpushList(String key, String... values) {
		return (long) this.invoke("rpush", new Object[] { key, values }, new Class[] {
				String.class, String[].class });
	}

	public long expire(String key, int time) {
		return (long) this.invoke("expire", new Object[] { key, time }, new Class[] { String.class,
				int.class });
	}

	public long hsetnx(String key, String field, String value) {
		return (long) this.invoke("hsetnx", new String[] { key, field, value }, new Class[] {
				String.class, String.class, String.class });
	}

	public boolean exist(String key) {
		return (boolean) this.invoke("exists", new String[] { key }, String.class);
	}

	public boolean existInSet(String key, String member) {
		return (boolean) this.invoke("sismember", new String[] { key, member }, new Class[] {
				String.class, String.class });
	}

	public long saddSet(String key, String... members) {
		return (long) this.invoke("sadd", new Object[] { key, members }, new Class[] {
				String.class, String[].class });
	}

	public long sremSet(String key, String... members) {
		return (long) this.invoke("srem", new Object[] { key, members }, new Class[] {
				String.class, String[].class });
	}

	public String spopSet(String key) {
		return String.valueOf(this.invoke("spop", new Object[] { key },
				new Class[] { String.class }));
	}

	public long hSet(byte[] key, byte[] field, byte[] value) {
		return (long) this.invoke("hset", new Object[] { key, field, value }, new Class[] {
				byte[].class, byte[].class, byte[].class });
	}

	@SuppressWarnings("unchecked")
	public Map<byte[], byte[]> hGetAll(byte[] key) {
		return (Map<byte[], byte[]>) this.invoke("hgetAll", new Object[] { key },
				new Class[] { byte[].class });
	}

	public byte[] hGet(byte[] key, byte[] field) {
		return (byte[]) this.invoke("hget", new Object[] { key, field }, new Class[] {
				byte[].class, byte[].class });
	}

	public long del(byte[] key) {
		return (long) this.invoke("del", new Object[] { key }, byte[].class);
	}

	protected Object invoke(String methodName, Object[] args, Class<?>... parameterTypes) {
		Object ret = null;

		ShardedJedis jedis = pool.getResource();
		try {
			/*
			 * Method method = clazz.getMethod(methodName, parameterTypes); ret
			 * = method.invoke(jedis, args);
			 */
			ret = access.invoke(jedis, methodName, parameterTypes, args);
			if (Settings.getInstance().isHitCount()) {
				Hits.getInstance().log(args[0], methodName, ret);
			}
		} catch (NoSuchElementException | JedisConnectionException e) {
			LOG.error("one redis server is downtime, please checked it");
			if (null != jedis) {
				pool.returnBrokenResource(jedis);
			}
			if (Settings.getInstance().isReplication() && Settings.getInstance().isAutoSwitch()) {
				jedis = pool.getResource();
				ret = access.invoke(jedis, methodName, parameterTypes, args);
			}
		} catch (SecurityException | IllegalArgumentException e) {
			LOG.error(e.getMessage(), e);
			if (null != jedis) {
				pool.returnBrokenResource(jedis);
			}
		} finally {
			if (null != jedis) {
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
}