package com.example.rpc;

import com.rabbitmq.client.StringRpcServer;
import com.rabbitmq.client.Channel;
import com.google.gson.Gson;

public class MyRpcServer extends StringRpcServer {

    Gson gson;

    public MyRpcServer(Channel channel, String queueName) throws Exception {
        super(channel, queueName);
        gson = new Gson();
    }
 
    @Override
    public String handleStringCall(String reqJson) {
        System.out.println(" [.] Received JSON: " + reqJson);
        RequestObj reqObj = gson.fromJson(reqJson, RequestObj.class);
        System.out.println(" [ ] Deserialized to object: " + reqObj);
        ReplyObj replyObj = process(reqObj);
        System.out.println(" [ ] Calculated response object: " + replyObj);
        String replyJson = gson.toJson(replyObj);
        System.out.println(" [ ] Serialized to JSON: " + replyJson);
        return replyJson;
    }

    private static ReplyObj process(RequestObj reqObj) {
        return new ReplyObj(reqObj.getOperandA() + reqObj.getOperandB());
    }
}
