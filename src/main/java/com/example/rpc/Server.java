package com.example.rpc;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

public class Server {

    private final static String RPC_QUEUE = "rpc_queue";

    public static void main(String[] args) throws Exception {

        // Establish connection to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Channel channel = factory.newConnection().createChannel();

        // Declare queue on which to listen for RPC calls
        channel.queueDeclare(RPC_QUEUE, false, false, false, null);

        // Create RPC server
        MyRpcServer rpcServer = new MyRpcServer(channel, RPC_QUEUE);

        // Start listening for RPC requests
        System.out.println(" [x] Awaiting RPC requests");
        rpcServer.mainloop();
    }

}
