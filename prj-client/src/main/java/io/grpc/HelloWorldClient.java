package io.grpc;


import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;

public class HelloWorldClient {
//	 private static final Logger logger = LoggerFactory.getLogger(HelloWorldClient.class);
	 private static final Logger logger = Logger.getLogger(HelloWorldClient.class);

	  private final ManagedChannel channel;
	  private final GreeterGrpc.GreeterBlockingStub blockingStub;

	  /** Construct client connecting to HelloWorld server at {@code host:port}. */
	  public HelloWorldClient(String host, int port) {
	    channel = ManagedChannelBuilder.forAddress(host, port)
	        .usePlaintext(true)
	        .build();
	    blockingStub = GreeterGrpc.newBlockingStub(channel);
	  }

	  public void shutdown() throws InterruptedException {
	    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	  }

	  /** Say hello to server. */
	  public void greet(String name) {
	    logger.info("Will try to greet " + name + " ...");
	    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
	    HelloReply response;
	    try {
	      response = blockingStub.sayHello(request);
	    } catch (StatusRuntimeException e) {
	    	System.out.println(e);
	    	return;
	    }
	    logger.info("Greeting: " + response.getMessage());
	  }
	  
	  public void greetAgain(String name) {
	    logger.info("Will try to greet " + name + " ...");
	    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
	    HelloReply response;
	    try {
	      response = blockingStub.sayHelloAgain(request);
	    } catch (StatusRuntimeException e) {
	    	System.out.println(e);
	    	return;
	    }
	    logger.info("Greeting: " + response.getMessage());
	  }

	  /**
	   * Greet server. If provided, the first element of {@code args} is the name to use in the
	   * greeting.
	   */
	  public static void main(String[] args) throws Exception {
	    HelloWorldClient client = new HelloWorldClient("localhost", 50051);
	    try {
	      /* Access a service running on the local machine on port 50051 */
	      String user = "world";
	      if (args.length > 0) {
	        user = args[0]; /* Use the arg as the name to greet if provided */
	      }
	      client.greet(user);
	      client.greetAgain(user);
	    } finally {
	      client.shutdown();
	    }
	  }
}
