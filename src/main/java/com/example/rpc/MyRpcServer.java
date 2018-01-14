package com.example.rpc;

import com.rabbitmq.client.StringRpcServer;
import com.rabbitmq.client.Channel;

/**
 * Custom RpcServer defining handler for incoming requests.
 *
 * This instance extends StringRpcServer, which is a special case of RpcServer
 * that expects requests to be strings and provides a handleStringCall method
 * to override. In this way, no conversion from byte array to string is needed.
 */
public class MyRpcServer extends StringRpcServer {

    public MyRpcServer(Channel channel, String queueName) throws Exception {
        super(channel, queueName);
    }
 
    @Override
    public String handleStringCall(String req) {
        System.out.println(" [.] Receiving request: \"" + req + "\"");
        String reply = process(req);
        System.out.println(" [ ] Sending reply: \"" + reply + "\"");
        return reply;
    }

    private static String process(String req) {
        return req + " [PROCESSED]";
    }
}
