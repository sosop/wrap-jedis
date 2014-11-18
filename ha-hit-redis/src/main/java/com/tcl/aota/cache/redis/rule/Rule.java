package com.tcl.aota.cache.redis.rule;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.TreeMap;

import com.tcl.aota.cache.redis.cluster.Cluster;
import com.tcl.aota.cache.redis.cluster.ClusterInfo;
import com.tcl.aota.cache.redis.utils.StringUtil;


public abstract class Rule {
	
	protected ClusterInfo clusterInfo;
	
	protected TreeMap<Long, Cluster> virNodes;
	
	public abstract Cluster getCluster(Object condition);
	
	@SuppressWarnings("all")
	public Rule initial(ClusterInfo clusterInfo) {
		this.clusterInfo = clusterInfo;
		virNodes = new TreeMap<>();
		List<Cluster> clusters = clusterInfo.getClusters();
		Cluster cluster = null;
		String key = "";
		for (int i = 0; i < clusters.size(); i++) {
			cluster = clusters.get(i);
			for (int j = 0; j < cluster.getWeight(); j++) {
				if(StringUtil.isNull(cluster.getName())) {
					key = StringUtil.append("SHARD-", i, "-NODE-", j);
				} else {
					key = StringUtil.append(cluster.getName(), "-", i, "-NODE-", j);
				}
				virNodes.put(hash(key), cluster);
			}
		}
		return this;
	}
	
	/** 
     *  MurMurHash算法，是非加密HASH算法，性能很高， 
     *  比传统的CRC32,MD5，SHA-1等HASH算法要快很多，而且据说这个算法的碰撞率很低. 
     *  http://murmurhash.googlepages.com/ 
     */  
    protected Long hash(String key) {  
      
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());  
        int seed = 0x1234ABCD;  
          
        ByteOrder byteOrder = buf.order();  
        buf.order(ByteOrder.LITTLE_ENDIAN);  
  
        long m = 0xc6a4a7935bd1e995L;  
        int r = 47;  
  
        long h = seed ^ (buf.remaining() * m);  
  
        long k;  
        while (buf.remaining() >= 8) {  
            k = buf.getLong();  
  
            k *= m;  
            k ^= k >>> r;  
            k *= m;  
  
            h ^= k;  
            h *= m;  
        }  
  
        if (buf.remaining() > 0) {  
            ByteBuffer finish = ByteBuffer.allocate(8).order(  
                    ByteOrder.LITTLE_ENDIAN);  
            // for big-endian version, do this first:   
            // finish.position(8-buf.remaining());   
            finish.put(buf).rewind();  
            h ^= finish.getLong();  
            h *= m;  
        }  
  
        h ^= h >>> r;  
        h *= m;  
        h ^= h >>> r;  
  
        buf.order(byteOrder);  
        return h;  
    }
}
