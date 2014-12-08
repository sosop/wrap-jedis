package com.sosop.cache.redis.thrift;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;

import com.sosop.cache.redis.cluster.ClusterInfo;
import com.sosop.cache.redis.utils.ObjectUtil;


public class RemoteImp implements Remote.Iface {

	private ClusterInfo info;
	
	public RemoteImp(ClusterInfo info) {
		this.info = info;
	}

	@Override
	public String sets(String cluster, String key, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).set(key, value);
	}

	@Override
	public String setx(String cluster, String key, String value, String nxxx, String expx, long time)
			throws TException {
		return info.cluster(ObjectUtil.object(cluster)).set(key, value, nxxx, expx, time);
	}

	@Override
	public String get(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).get(key);
	}

	@Override
	public boolean exists(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).exists(key);
	}

	@Override
	public long persist(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).persist(key);
	}

	@Override
	public String type(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).type(key);
	}

	@Override
	public long expire(String cluster, String key, int seconds) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).expire(key, seconds);
	}

	@Override
	public long expireAt(String cluster, String key, long unixTime) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).expireAt(key, unixTime);
	}

	@Override
	public long ttl(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).ttl(key);
	}

	@Override
	public boolean setbit(String cluster, String key, long offset, boolean value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).setbit(key, offset, value);
	}

	@Override
	public boolean setbit0(String cluster, String key, long offset, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).setbit(key, offset, value);
	}

	@Override
	public boolean getbit(String cluster, String key, long offset) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).getbit(key, offset);
	}

	@Override
	public long setrange(String cluster, String key, long offset, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).setrange(key, offset, value);
	}

	@Override
	public String getrange(String cluster, String key, long startOffset, long eOffset)
			throws TException {
		return info.cluster(ObjectUtil.object(cluster)).getrange(key, startOffset, eOffset);
	}

	@Override
	public String getset(String cluster, String key, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).getSet(key, value);
	}

	@Override
	public long setnx(String cluster, String key, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).setnx(key, value);
	}

	@Override
	public String setex(String cluster, String key, int seconds, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).setex(key, seconds, value);
	}

	@Override
	public long decrBy(String cluster, String key, long integer) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).decrBy(key, integer);
	}

	@Override
	public long decr(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).decr(key);
	}

	@Override
	public long incrBy(String cluster, String key, long integer) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).incrBy(key, integer);
	}

	@Override
	public long incr(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).incr(key);
	}

	@Override
	public long appe(String cluster, String key, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).append(key, value);
	}

	@Override
	public String substr(String cluster, String key, int start, int e) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).substr(key, start, e);
	}

	@Override
	public long hset(String cluster, String key, String field, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hset(key, field, value);
	}

	@Override
	public String hget(String cluster, String key, String field) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hget(key, field);
	}

	@Override
	public long hsetnx(String cluster, String key, String field, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hsetnx(key, field, value);
	}

	@Override
	public String hmset(String cluster, String key, Map<String, String> hash) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hmset(key, hash);
	}

	@Override
	public List<String> hmget(String cluster, String key, ByteBuffer fields) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hmget(key, new String(fields.array()).split(","));
	}

	@Override
	public long hincrBy(String cluster, String key, String field, long value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hincrBy(key, field, value);
	}

	@Override
	public boolean hexists(String cluster, String key, String field) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hexists(key, field);
	}

	@Override
	public long hdel(String cluster, String key, ByteBuffer field) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hdel(key, new String(field.array()).split(","));
	}

	@Override
	public long hlen(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hlen(key);
	}

	@Override
	public Set<String> hkeys(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hkeys(key);
	}

	@Override
	public List<String> hvals(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hvals(key);
	}

	@Override
	public Map<String, String> hgetAll(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).hgetAll(key);
	}

	@Override
	public long rpush(String cluster, String key, ByteBuffer str) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).rpush(key, new String(str.array()).split(","));
	}

	@Override
	public long lpush(String cluster, String key, ByteBuffer str) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lpush(key, new String(str.array()).split(","));
	}

	@Override
	public long llen(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).llen(key);
	}

	@Override
	public List<String> lrange(String cluster, String key, long start, long e) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lrange(key, start, e);
	}

	@Override
	public String ltrim(String cluster, String key, long start, long e) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).ltrim(key, start, e);
	}

	@Override
	public String lindex(String cluster, String key, long index) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lindex(key, index);
	}

	@Override
	public String lset(String cluster, String key, long index, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lset(key, index, value);
	}

	@Override
	public long lrem(String cluster, String key, long count, String value) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lrem(key, count, value);
	}

	@Override
	public String lpop(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).lpop(key);
	}

	@Override
	public String rpop(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).rpop(key);
	}

	@Override
	public long sadd(String cluster, String key, ByteBuffer member) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).sadd(key, new String(member.array()).split(","));
	}

	@Override
	public Set<String> smembers(String cluster, String key) throws TException {
		
		return null;
	}

	@Override
	public long srem(String cluster, String key, ByteBuffer member) throws TException {
		
		return 0;
	}

	@Override
	public String spop(String cluster, String key) throws TException {
		
		return null;
	}

	@Override
	public long scard(String cluster, String key) throws TException {
		
		return 0;
	}

	@Override
	public boolean sismember(String cluster, String key, String member) throws TException {
		
		return false;
	}

	@Override
	public String srandmember(String cluster, String key) throws TException {
		
		return null;
	}

	@Override
	public List<String> srandmemberList(String cluster, String key, int count) throws TException {
		
		return null;
	}

	@Override
	public long strlen(String cluster, String key) throws TException {
		
		return 0;
	}

	@Override
	public long zadd(String cluster, String key, double score, String member) throws TException {
		
		return 0;
	}

	@Override
	public long zaddMap(String cluster, String key, Map<String, Double> scoreMembers)
			throws TException {
		
		return 0;
	}

	@Override
	public Set<String> zrange(String cluster, String key, long start, long e) throws TException {
		
		return null;
	}

	@Override
	public long zrem(String cluster, String key, ByteBuffer member) throws TException {
		
		return 0;
	}

	@Override
	public double zincrby(String cluster, String key, double score, String member)
			throws TException {
		
		return 0;
	}

	@Override
	public long zrank(String cluster, String key, String member) throws TException {
		
		return 0;
	}

	@Override
	public long zrevrank(String cluster, String key, String member) throws TException {
		
		return 0;
	}

	@Override
	public Set<String> zrevrange(String cluster, String key, long start, long e) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrangeWithScores(String cluster, String key, long start, long e)
			throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrevrangeWithScores(String cluster, String key, long start, long e)
			throws TException {
		
		return null;
	}

	@Override
	public long zcard(String cluster, String key) throws TException {
		
		return 0;
	}

	@Override
	public double zscore(String cluster, String key, String member) throws TException {
		
		return 0;
	}

	@Override
	public List<String> sort(String cluster, String key) throws TException {
		
		return null;
	}

	@Override
	public List<String> sortParams(String cluster, String key, ByteBuffer sortingParameters)
			throws TException {
		
		return null;
	}

	@Override
	public long zcount(String cluster, String key, double min, double max) throws TException {
		
		return 0;
	}

	@Override
	public long zcounts(String cluster, String key, String min, String max) throws TException {
		
		return 0;
	}

	@Override
	public Set<String> zrangeByScore1(String cluster, String key, double min, double max)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrangeByScore2(String cluster, String key, String min, String max)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore1(String cluster, String key, double max, double min)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrangeByScore3(String cluster, String key, double min, double max,
			int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore2(String cluster, String key, String max, String min)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrangeByScore4(String cluster, String key, String min, String max,
			int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore3(String cluster, String key, double max, double min,
			int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrangeByScoreWithScores1(String cluster, String key, double min,
			double max) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrevrangeByScoreWithScores1(String cluster, String key, double max,
			double min) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrangeByScoreWithScores2(String cluster, String key, double min,
			double max, int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByScore(String cluster, String key, String max, String min,
			int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrangeByScoreWithScores3(String cluster, String key, String min,
			String max) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrevrangeByScoreWithScores2(String cluster, String key, String max,
			String min) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrangeByScoreWithScores4(String cluster, String key, String min,
			String max, int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrevrangeByScoreWithScores3(String cluster, String key, double max,
			double min, int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public Set<ByteBuffer> zrevrangeByScoreWithScores4(String cluster, String key, String max,
			String min, int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public long zremrangeByRank(String cluster, String key, long start, long e) throws TException {
		
		return 0;
	}

	@Override
	public long zremrangeByScore1(String cluster, String key, double start, double e)
			throws TException {
		
		return 0;
	}

	@Override
	public long zremrangeByScore2(String cluster, String key, String start, String e)
			throws TException {
		
		return 0;
	}

	@Override
	public long zlexcount(String cluster, String key, String min, String max) throws TException {
		
		return 0;
	}

	@Override
	public Set<String> zrangeByLex1(String cluster, String key, String min, String max)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrangeByLex2(String cluster, String key, String min, String max, int offset,
			int count) throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByLex1(String cluster, String key, String max, String min)
			throws TException {
		
		return null;
	}

	@Override
	public Set<String> zrevrangeByLex2(String cluster, String key, String max, String min,
			int offset, int count) throws TException {
		
		return null;
	}

	@Override
	public long zremrangeByLex(String cluster, String key, String min, String max)
			throws TException {
		
		return 0;
	}

	@Override
	public long linsert(String cluster, String key, int where, String pivot, String value)
			throws TException {
		
		return 0;
	}

	@Override
	public long lpushx(String cluster, String key, ByteBuffer str) throws TException {
		
		return 0;
	}

	@Override
	public long rpushx(String cluster, String key, ByteBuffer str) throws TException {
		
		return 0;
	}

	@Override
	public List<String> blpop1(String cluster, String arg) throws TException {
		
		return null;
	}

	@Override
	public List<String> blpop2(String cluster, int timeout, String key) throws TException {
		
		return null;
	}

	@Override
	public List<String> brpop1(String cluster, String arg) throws TException {
		
		return null;
	}

	@Override
	public List<String> brpop2(String cluster, int timeout, String key) throws TException {
		
		return null;
	}

	@Override
	public long dels(String cluster, String key) throws TException {
		return info.cluster(ObjectUtil.object(cluster)).del(key);
	}

	@Override
	public String echo(String cluster, String str) throws TException {
		
		return null;
	}

	@Override
	public long move(String cluster, String key, int dbIndex) throws TException {
		
		return 0;
	}

	@Override
	public long bitcount1(String cluster, String key) throws TException {
		
		return 0;
	}

	@Override
	public long bitcount2(String cluster, String key, long start, long e) throws TException {
		
		return 0;
	}

	@Override
	public ByteBuffer hscan(String cluster, String key, String cursor) throws TException {
		
		return null;
	}

	@Override
	public ByteBuffer sscan(String cluster, String key, String cursor) throws TException {
		
		return null;
	}

	@Override
	public ByteBuffer zscan(String cluster, String key, String cursor) throws TException {
		
		return null;
	}

	@Override
	public long pfadd(String cluster, String key, ByteBuffer elements) throws TException {
		
		return 0;
	}

	@Override
	public long pfcount(String cluster, String key) throws TException {
		
		return 0;
	}

	
}