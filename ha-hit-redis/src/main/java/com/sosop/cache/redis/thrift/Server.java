package com.sosop.cache.redis.thrift;

import org.apache.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.sosop.cache.redis.cluster.ClusterXML;

public class Server {

	private final static Logger LOG = Logger.getLogger(Server.class);

	public static void main(String[] args) {
		if(args.length != 1) {
			return;
		}
		final String path = args[0];
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Server().startServer(path);
			}
		}).start();
	}

	public void startServer(String path) {
		try {
			TNonblockingServerSocket  serverTransport = new TNonblockingServerSocket(9088); 
			TProcessor processor = new Remote.Processor<Remote.Iface>(
					new RemoteImp(new ClusterXML(path)));
			Args args = new Args(serverTransport);
			args.processor(processor);
			args.transportFactory(new TFramedTransport.Factory());
			args.protocolFactory(new TBinaryProtocol.Factory());
			TServer server = new TThreadedSelectorServer(args);
			LOG.info("server starting ...");
			server.serve();
		} catch (TTransportException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
