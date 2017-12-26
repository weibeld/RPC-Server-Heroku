package com.example.rpc;

import com.rabbitmq.client.StringRpcServer;
import com.rabbitmq.client.Channel;

public class MyRpcServer extends StringRpcServer {

    public MyRpcServer(Channel channel, String queueName) throws Exception {
        super(channel, queueName);
    }
 
    @Override
    public String handleStringCall(String request) {
        System.out.println(" [.] Received request: " + request);
        return process(request);
    }

    private static String process(String s) {
        return s + " [PROCESSED]";
    }
}
