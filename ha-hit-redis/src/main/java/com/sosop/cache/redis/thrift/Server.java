package com.sosop.cache.redis.thrift;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

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
		String path = "/tmp/redis-cluster-final.xml";
		if (args.length == 0 && Files.notExists(Paths.get(path), LinkOption.NOFOLLOW_LINKS)) {
			LOG.error("please input redis clusters configuration file");
			return;
		}
		if (args.length >= 1 && Files.notExists(Paths.get(path = args[0]), LinkOption.NOFOLLOW_LINKS)) {
			LOG.error("can not find the file, path does not exist!");
			return;
		}
		final String realPath = path;
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Server().startServer(realPath);
			}
		}).start();
	}

	public void startServer(String path) {
		try {
			TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(9088);
			TProcessor processor = new Remote.Processor<Remote.Iface>(new RemoteImp(new ClusterXML(
					path)));
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
