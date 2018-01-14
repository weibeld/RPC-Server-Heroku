package com.example.rpc;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class Server {

    private final static String QUEUE = "rpc-queue";

    public static void main(String[] args) throws Exception {

        // Establish connection to RabbitMQ server
        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null) uri = "amqp://guest:guest@localhost";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        Channel channel = factory.newConnection().createChannel();

        // Declare queue on which to listen for RPC calls
        channel.queueDeclare(QUEUE, false, false, false, null);

        // Create RPC server
        MyRpcServer server = new MyRpcServer(channel, QUEUE);

        // Start listening for requests
        System.out.println(" [x] Awaiting requests");
        server.mainloop();
    }

}
