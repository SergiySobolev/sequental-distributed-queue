package com.sbk.queue.server.market;

import com.hazelcast.core.*;


public class Client implements MessageListener<StockPrice> {

    public Client(String topicName) {
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
        ITopic<StockPrice> topic = hzInstance.getTopic(topicName);
        topic.addMessageListener(this);
    }

    /**
     * @see com.hazelcast.core.MessageListener#onMessage(com.hazelcast.core.Message)
     */
    @Override
    public void onMessage(Message<StockPrice> arg0) {
        System.out.println("Received: " + arg0.getMessageObject().toString());
    }

    public static void main(String[] args) {

        new Client("STOCKS");
    }

}