package com.tcl.aota.cache.redis.thrift;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;


public class Server {
	
	private final static Logger LOG = Logger.getLogger(Server.class);
	
	public static void main(String[] args) {
		
	}
	
	public void startServer() {
		try {
			TServerTransport serverTransport = new TServerSocket(9090);
			/*Factory proFactory = new TBinaryProtocol.Factory();
			TProcessor processor = new Remote.Processor<Remote.Iface>(
					new RemoteImp());
			TServer.Args tArgs = new TServer.Args(serverTransport);
			tArgs.processor(processor);
			tArgs.protocolFactory(proFactory);
			 TServer server = new TThreadedSelectorServer(args)*/
		} catch (TTransportException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
