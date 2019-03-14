/**
 * @(#)Producer.java, Mar 14, 2019.
 * <p>
 * Copyright 2019 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.coder4.perf;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;

/**
 * @author coder4
 */
public class ProducerUtils {

    private static final String GROUP = "GROUP_TEST";

    private static Message generateMsg(String topic, String tag, int msgLen)
            throws UnsupportedEncodingException {
        return new Message(topic, tag,
                String.format("%1$" + msgLen + "s", "a").getBytes(RemotingHelper.DEFAULT_CHARSET));
    }

    public static void sync(String ns, String topic, String tag, int count, int msgLen) throws MQClientException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer(GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(ns);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < count; i++) {
            try {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = null;

                msg = generateMsg(topic, tag, msgLen);

                //Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
            } catch (Exception e) {
                System.out.println("ERROR: " + e);
            }
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();

    }

    public static void async(String ns, String topic, String tag, int count, int msgLen) throws MQClientException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer(GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(ns);
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < count; i++) {
            try {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = null;

                msg = generateMsg(topic, tag, msgLen);

                //Call send message to deliver message to one of brokers.
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {

                    }

                    @Override
                    public void onException(Throwable e) {
                        System.out.println("ERROR: " + e);
                    }
                });
            } catch (Exception e) {

            }
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();

    }

}