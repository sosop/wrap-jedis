package com.sosop.cache.client;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Hello world!
 * 
 */
public class ClientFactory {
	
	private static final Logger LOG = Logger.getLogger(ClientFactory.class); 
	
	private static Remote.Client client;
	
	
	public static Remote.Client create() {
		if(client == null) {
			try {
				TFramedTransport transport = new TFramedTransport(new TSocket("localhost", 9088));
				transport.open();
				// 设置传输协议为TBinaryProtocol
		        TProtocol protocol = new TBinaryProtocol(transport);
		        client = new Remote.Client(protocol);
			} catch (TTransportException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return client;
	}
}