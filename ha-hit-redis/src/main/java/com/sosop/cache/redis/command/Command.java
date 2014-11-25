package com.sosop.cache.redis.command;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Client;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.sosop.cache.redis.global.settings.Settings;
import com.sosop.cache.redis.heartbeat.HeartBeat;
import com.sosop.cache.redis.statistics.Hits;

public abstract class Command {
	private final static Logger LOG = Logger.getLogger(Command.class);

	private final static MethodAccess access = MethodAccess.get(ShardedJedis.class);

	protected ShardedJedisPool pool;

	public String set(String key, String value) {
		return String.valueOf(this.invoke("set", new String[] { key, value }, new Class[] {
				String.class, String.class }));
	}

	public String set(String key, String value, String nxxx, String expx, long time) {
		return String
				.valueOf(this.invoke("set", new Object[] { key, value, nxxx, expx, time },
						new Class[] { String.class, String.class, String.class, String.class,
								long.class }));
	}

	public String get(String key) {
		return String.valueOf(this.invoke("get", new String[] { key }, String.class));
	}

	public boolean exists(String key) {
		return (boolean) this.invoke("exists", new String[] { key }, String.class);
	}

	public Long persist(String key) {
		return (long) this.invoke("persist", new String[] { key }, String.class);
	}

	public String type(String key) {
		return String.valueOf(this.invoke("type", new String[] { key }, String.class));
	}

	public long expire(String key, int time) {
		return (long) this.invoke("expire", new Object[] { key, time }, new Class[] { String.class,
				int.class });
	}

	public Long expireAt(String key, long unixTime) {
		return (long) this.invoke("expireAt", new Object[] { key, unixTime }, new Class[] {
				String.class, long.class });
	}

	public Long ttl(String key) {
		return (long) this.invoke("ttl", new String[] { key }, String.class);
	}

	public Boolean setbit(String key, long offset, boolean value) {
		return (boolean) this.invoke("setbit", new Object[] { key, offset, value }, new Class[] {
				String.class, long.class, boolean.class });
	}

	public Boolean setbit(String key, long offset, String value) {
		return (boolean) this.invoke("setbit", new Object[] { key, offset, value }, new Class[] {
				String.class, long.class, String.class });
	}

	public Boolean getbit(String key, long offset) {
		return (boolean) this.invoke("getbit", new Object[] { key, offset }, new Class[] {
				String.class, long.class });
	}

	public Long setrange(String key, long offset, String value) {
		return (long) this.invoke("setrange", new Object[] { key, offset, value }, new Class[] {
				String.class, long.class, String.class });
	}

	public String getrange(String key, long startOffset, long endOffset) {
		return String.valueOf(this.invoke("getrange", new Object[] { key, startOffset, endOffset },
				new Class[] { String.class, long.class, long.class }));
	}

	public String getSet(String key, String value) {
		return String.valueOf(this.invoke("getSet", new String[] { key, value }, new Class[] {
				String.class, String.class }));
	}

	public Long setnx(String key, String value) {
		return (long) this.invoke("setnx", new String[] { key, value }, new Class[] { String.class,
				String.class });
	}

	public String setex(String key, int seconds, String value) {
		return String.valueOf(this.invoke("setex", new Object[] { key, seconds, value },
				new Class[] { String.class, int.class, String.class }));
	}

	public Long decrBy(String key, long integer) {
		return (long) this.invoke("decrBy", new Object[] { key, integer }, new Class[] {
				String.class, long.class });
	}

	public Long decr(String key) {
		return (long) this.invoke("decr", new String[] { key }, String.class);
	}

	public Long incrBy(String key, long integer) {
		return (long) this.invoke("incrBy", new Object[] { key, integer }, new Class[] {
				String.class, long.class });
	}

	public Long incr(String key) {
		return (long) this.invoke("incr", new String[] { key }, String.class);
	}

	public Long append(String key, String value) {
		return (long) this.invoke("append", new String[] { key, value }, new Class[] {
				String.class, String.class });
	}

	public String substr(String key, int start, int end) {
		return String.valueOf(this.invoke("substr", new Object[] { key, start, end }, new Class[] {
				String.class, int.class, int.class }));
	}

	public Long hset(String key, String field, String value) {
		return (long) this.invoke("hset", new String[] { key, field, value }, new Class[] {
				String.class, String.class, String.class });
	}

	public String hget(String key, String field) {
		return String.valueOf(this.invoke("hget", new String[] { key, field }, new Class[] {
				String.class, String.class }));
	}

	public long hsetnx(String key, String field, String value) {
		return (long) this.invoke("hsetnx", new String[] { key, field, value }, new Class[] {
				String.class, String.class, String.class });
	}

	public String hmset(String key, Map<String, String> hash) {
		return String.valueOf(this.invoke("hmset", new Object[] { key, hash }, new Class[] {
				String.class, Map.class }));
	}

	@SuppressWarnings("unchecked")
	public List<String> hmget(String key, String... fields) {
		return (List<String>) this.invoke("hmget", new Object[] { key, fields }, new Class[] {
				String.class, String[].class });
	}

	public Long hincrBy(String key, String field, long value) {
		return (long) this.invoke("hincrBy", new Object[] { key, field, value }, new Class[] {
				String.class, String.class, long.class });
	}

	public Boolean hexists(String key, String field) {
		return (boolean) this.invoke("hexists", new String[] { key, field }, new Class[] {
				String.class, String.class });
	}

	public Long hdel(String key, String... field) {
		return (long) this.invoke("hdel", new Object[] { key, field }, new Class[] { String.class,
				String[].class });
	}

	public Long hlen(String key) {
		return (long) this.invoke("hlen", new String[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public Set<String> hkeys(String key) {
		return (Set<String>) this.invoke("hkeys", new String[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> hvals(String key) {
		return (List<String>) this.invoke("hvals", new String[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> hgetAll(String key) {
		return (Map<String, String>) this.invoke("hgetAll", new String[] { key }, String.class);
	}

	public Long rpush(String key, String... values) {
		return (long) this.invoke("rpush", new Object[] { key, values }, new Class[] {
				String.class, String[].class });
	}

	public Long lpush(String key, String... values) {
		return (long) this.invoke("lpush", new Object[] { key, values }, new Class[] {
				String.class, String[].class });
	}

	public Long llen(String key) {
		return (long) this.invoke("llen", new Object[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> lrange(String key, long start, long end) {
		return (List<String>) this.invoke("lrange", new Object[] { key, start, end }, new Class[] {
				String.class, long.class, long.class });
	}

	public String ltrim(String key, long start, long end) {
		return String.valueOf(this.invoke("ltrim", new Object[] { key, start, end }, new Class[] {
				String.class, long.class, long.class }));
	}

	public String lindex(String key, long index) {
		return String.valueOf(this.invoke("lindex", new Object[] { key, index }, new Class[] {
				String.class, long.class }));
	}

	public String lset(String key, long index, String value) {
		return String.valueOf(this.invoke("lset", new Object[] { key, index, value }, new Class[] {
				String.class, long.class, String.class }));
	}

	public Long lrem(String key, long count, String value) {
		return (long) this.invoke("lrem", new Object[] { key, count, value }, new Class[] {
				String.class, long.class, String.class });
	}

	public String lpop(String key) {
		return String.valueOf(this.invoke("lpop", new Object[] { key }, String.class));
	}

	public String rpop(String key) {
		return String.valueOf(this.invoke("rpop", new String[] { key }, String.class));
	}

	public Long sadd(String key, String... member) {
		return (long) this.invoke("sadd", new Object[] { key, member }, new Class[] { String.class,
				String[].class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> smembers(String key) {
		return (Set<String>) this.invoke("smembers", new String[] { key }, String.class);
	}

	public Long srem(String key, String... member) {
		return (long) this.invoke("srem", new Object[] { key, member }, new Class[] { String.class,
				String[].class });
	}

	public String spop(String key) {
		return String.valueOf(this.invoke("spop", new String[] { key }, String.class));
	}

	public Long scard(String key) {
		return (long) this.invoke("scard", new Object[] { key }, String.class);
	}

	public Boolean sismember(String key, String member) {
		return (boolean) this.invoke("sismember", new Object[] { key, member }, new Class[] {
				String.class, String.class });
	}

	public String srandmember(String key) {
		return String.valueOf(this.invoke("srandmember", new String[] { key }, String.class));
	}

	@SuppressWarnings("unchecked")
	public List<String> srandmember(String key, int count) {
		return (List<String>) this.invoke("srandmember", new Object[] { key, count }, new Class[] {
				String.class, int.class });
	}

	public Long strlen(String key) {
		return (long) this.invoke("strlen", new Object[] { key }, String.class);
	}

	public Long zadd(String key, double score, String member) {
		return (long) this.invoke("zadd", new Object[] { key, score, member }, new Class[] {
				String.class, double.class, String.class });
	}

	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return (long) this.invoke("zadd", new Object[] { key, scoreMembers }, new Class[] {
				String.class, Map.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrange(String key, long start, long end) {
		return (Set<String>) this.invoke("zrange", new Object[] { key, start, end }, new Class[] {
				String.class, long.class, long.class });
	}

	public Long zrem(String key, String... member) {
		return (long) this.invoke("zrem", new Object[] { key, member }, new Class[] { String.class,
				String[].class });
	}

	public Double zincrby(String key, double score, String member) {
		return (double) this.invoke("zincrby", new Object[] { key, score, member }, new Class[] {
				String.class, double.class, String.class });
	}

	public Long zrank(String key, String member) {
		return (long) this.invoke("zrank", new Object[] { key, member }, new Class[] {
				String.class, String.class });
	}

	public Long zrevrank(String key, String member) {
		return (long) this.invoke("zrevrank", new Object[] { key, member }, new Class[] {
				String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrange(String key, long start, long end) {
		return (Set<String>) this.invoke("zrevrange", new Object[] { key, start, end },
				new Class[] { String.class, long.class, long.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return (Set<Tuple>) this.invoke("zrangeWithScores", new Object[] { key, start, end },
				new Class[] { String.class, long.class, long.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return (Set<Tuple>) this.invoke("zrevrangeWithScores", new Object[] { key, start, end },
				new Class[] { String.class, long.class, long.class });
	}

	public Long zcard(String key) {
		return (long) this.invoke("zcard", new Object[] { key }, String.class);
	}

	public Double zscore(String key, String member) {
		return (double) this.invoke("zscore", new Object[] { key, member }, new Class[] {
				String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public List<String> sort(String key) {
		return (List<String>) this.invoke("sort", new Object[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> sort(String key, SortingParams sortingParameters) {
		return (List<String>) this.invoke("sort", new Object[] { key, sortingParameters },
				new Class[] { String.class, SortingParams.class });
	}

	public Long zcount(String key, double min, double max) {
		return (long) this.invoke("zcount", new Object[] { key, min, max }, new Class[] {
				String.class, double.class, double.class });
	}

	public Long zcount(String key, String min, String max) {
		return (long) this.invoke("zcount", new Object[] { key, min, max }, new Class[] {
				String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByScore(String key, double min, double max) {
		return (Set<String>) this.invoke("zrangeByScore", new Object[] { key, min, max },
				new Class[] { String.class, double.class, double.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByScore(String key, String min, String max) {
		return (Set<String>) this.invoke("zrangeByScore", new Object[] { key, min, max },
				new Class[] { String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return (Set<String>) this.invoke("zrevrangeByScore", new Object[] { key, max, min },
				new Class[] { String.class, double.class, double.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
		return (Set<String>) this.invoke("zrangeByScore", new Object[] { key, min, max, offset,
				count }, new Class[] { String.class, double.class, double.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return (Set<String>) this.invoke("zrevrangeByScore", new Object[] { key, min, max },
				new Class[] { String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
		return (Set<String>) this.invoke("zrangeByScore", new Object[] { key, min, max, offset,
				count }, new Class[] { String.class, String.class, String.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		return (Set<String>) this.invoke("zrevrangeByScore", new Object[] { key, max, min, offset,
				count }, new Class[] { String.class, double.class, double.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return (Set<Tuple>) this.invoke("zrangeByScoreWithScores", new Object[] { key, min, max },
				new Class[] { String.class, double.class, double.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
		return (Set<Tuple>) this.invoke("zrevrangeByScoreWithScores",
				new Object[] { key, max, min }, new Class[] { String.class, double.class,
						double.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset,
			int count) {
		return (Set<Tuple>) this.invoke("zrangeByScoreWithScores", new Object[] { key, min, max,
				offset, count }, new Class[] { String.class, double.class, double.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
		return (Set<String>) this.invoke("zrevrangeByScore", new Object[] { key, max, min, offset,
				count }, new Class[] { String.class, String.class, String.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		return (Set<Tuple>) this.invoke("zrangeByScoreWithScores", new Object[] { key, min, max },
				new Class[] { String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
		return (Set<Tuple>) this.invoke("zrevrangeByScoreWithScores",
				new Object[] { key, max, min }, new Class[] { String.class, String.class,
						String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset,
			int count) {
		return (Set<Tuple>) this.invoke("zrangeByScoreWithScores", new Object[] { key, min, max,
				offset, count }, new Class[] { String.class, String.class, String.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset,
			int count) {
		return (Set<Tuple>) this.invoke("zrevrangeByScoreWithScores", new Object[] { key, max, min,
				offset, count }, new Class[] { String.class, double.class, double.class, int.class,
				int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset,
			int count) {
		return (Set<Tuple>) this.invoke("zrevrangeByScoreWithScores", new Object[] { key, max, min,
				offset, count }, new Class[] { String.class, String.class, String.class, int.class,
				int.class });
	}

	public Long zremrangeByRank(String key, long start, long end) {
		return (long) this.invoke("zremrangeByRank", new Object[] { key, start, end }, new Class[] {
				String.class, long.class, long.class });
	}

	public Long zremrangeByScore(String key, double start, double end) {
		return (long) this.invoke("zremrangeByScore", new Object[] { key, start, end },
				new Class[] { String.class, double.class, double.class });
	}

	public Long zremrangeByScore(String key, String start, String end) {
		return (long) this.invoke("zremrangeByScore", new Object[] { key, start, end },
				new Class[] { String.class, String.class, String.class });
	}

	public Long zlexcount(final String key, final String min, final String max) {
		return (long) this.invoke("zlexcount", new Object[] { key, min, max }, new Class[] {
				String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByLex(final String key, final String min, final String max) {
		return (Set<String>) this.invoke("zrangeByLex", new Object[] { key, min, max },
				new Class[] { String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrangeByLex(final String key, final String min, final String max,
			final int offset, final int count) {
		return (Set<String>) this.invoke("zrangeByLex",
				new Object[] { key, min, max, offset, count }, new Class[] { String.class,
						String.class, String.class, int.class, int.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
		return (Set<String>) this.invoke("zrevrangeByLex", new Object[] { key, max, min },
				new Class[] { String.class, String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public Set<String> zrevrangeByLex(final String key, final String max, final String min,
			final int offset, final int count) {
		return (Set<String>) this.invoke("zrevrangeByLex", new Object[] { key, max, min, offset,
				count }, new Class[] { String.class, String.class, String.class, int.class,
				int.class });
	}

	public Long zremrangeByLex(final String key, final String min, final String max) {
		return (long) this.invoke("zremrangeByLex", new Object[] { key, min, max }, new Class[] {
				String.class, String.class, String.class });
	}

	public Long linsert(String key, Client.LIST_POSITION where, String pivot, String value) {
		return (long) this
				.invoke("linsert", new Object[] { key, where, pivot, value }, new Class[] {
						String.class, Client.LIST_POSITION.class, String.class, String.class });
	}

	public Long lpushx(String key, String... string) {
		return (long) this.invoke("lpushx", new Object[] { key, string }, new Class[] {
				String.class, String[].class });
	}

	public Long rpushx(String key, String... string) {
		return (long) this.invoke("rpushx", new Object[] { key, string }, new Class[] {
				String.class, String[].class });
	}

	@SuppressWarnings("unchecked")
	public List<String> blpop(String arg) {
		return (List<String>) this.invoke("blpop", new Object[] { arg }, String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> blpop(int timeout, String key) {
		return (List<String>) this.invoke("blpop", new Object[] { timeout, key }, new Class[] {
				int.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public List<String> brpop(String key) {
		return (List<String>) this.invoke("brpop", new Object[] { key }, String.class);
	}

	@SuppressWarnings("unchecked")
	public List<String> brpop(int timeout, String key) {
		return (List<String>) this.invoke("brpop", new Object[] { timeout, key }, new Class[] {
				int.class, String.class });
	}

	public long del(String key) {
		return (long) this.invoke("del", new String[] { key }, String.class);
	}

	public String echo(String string) {
		return String.valueOf(this.invoke("echo", new String[] { string }, String.class));
	}

	public Long move(String key, int dbIndex) {
		return (long) this.invoke("move", new Object[] { key, dbIndex }, new Class[] {
				String.class, int.class });
	}

	public Long bitcount(final String key) {
		return (long) this.invoke("bitcount", new Object[] { key }, String.class);
	}

	public Long bitcount(final String key, long start, long end) {
		return (long) this.invoke("bitcount", new Object[] { key, start, end }, new Class[] {
				String.class, long.class, long.class });
	}

	@SuppressWarnings("unchecked")
	public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
		return (ScanResult<Map.Entry<String, String>>) this.invoke("hscan", new Object[] { key,
				cursor }, new Class[] { String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public ScanResult<String> sscan(final String key, final String cursor) {
		return (ScanResult<String>) this.invoke("sscan", new Object[] { key, cursor }, new Class[] {
				String.class, String.class });
	}

	@SuppressWarnings("unchecked")
	public ScanResult<Tuple> zscan(final String key, final String cursor) {
		return (ScanResult<Tuple>) this.invoke("zscan", new Object[] { key, cursor }, new Class[] {
				String.class, String.class });
	}

	public Long pfadd(final String key, final String... elements) {
		return (long) this.invoke("pfadd", new Object[] { key, elements }, new Class[] {
				String.class, String[].class });
	}

	public long pfcount(final String key) {
		return (long) this.invoke("pfcount", new String[] { key }, String.class);
	}

	public String lpopList(String key) {
		return String.valueOf(this.invoke("lpop", new String[] { key }, String.class));
	}

	public boolean existInSet(String key, String member) {
		return (boolean) this.invoke("sismember", new String[] { key, member }, new Class[] {
				String.class, String.class });
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
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			ret = access.invoke(jedis, methodName, parameterTypes, args);
			if (Settings.getInstance().isHitCount()) {
				Hits.getInstance().log(args[0], methodName, ret);
			}
		} catch (SecurityException | IllegalArgumentException e) {
			LOG.error(e.getMessage(), e);
			if (null != jedis) {
				pool.returnBrokenResource(jedis);
			}
		} catch (NoSuchElementException | JedisConnectionException e) {
			LOG.error("one redis server is downtime, please checked it");
			if (null != jedis) {
				pool.returnBrokenResource(jedis);
			}
			if (Settings.getInstance().isReplication() && Settings.getInstance().isAutoSwitch()) {
				HeartBeat.get().promotion();
				jedis = pool.getResource();
				ret = access.invoke(jedis, methodName, parameterTypes, args);
			}
		} finally {
			if (null != jedis) {
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
}
