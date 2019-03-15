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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

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
        send(ns, topic, tag, count, msgLen, false);
    }

    public static void async(String ns, String topic, String tag, int count, int msgLen) throws MQClientException {
        send(ns, topic, tag, count, msgLen, true);
    }

    public static void send(String ns, String topic, String tag, int count, int msgLen,
                            boolean async) throws MQClientException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer(GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(ns);
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        long start = System.currentTimeMillis();
        AtomicInteger asyncSuccCnt = new AtomicInteger(0);
        AtomicInteger asyncFailCnt = new AtomicInteger(0);
        for (int i = 0; i < count; i++) {
            try {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = generateMsg(topic, tag, msgLen);

                if (async) {
                    producer.send(msg, new SendCallback() {
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            asyncSuccCnt.incrementAndGet();
                        }

                        @Override
                        public void onException(Throwable e) {
                            asyncFailCnt.incrementAndGet();
                        }
                    });
                } else {
                    producer.send(msg);
                }

            } catch (Exception e) {

            }
        }
        long end = System.currentTimeMillis();
        System.out.format("TPS=%.2f\n", (double) count * 1000 / (double) (end - start));
        //Shut down once the producer instance is not longer in use.
        if (async) {
            try {
                // 等待都发完
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                System.out.format("succ=%d fail=%d\n", asyncSuccCnt.intValue(), asyncFailCnt.intValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();

    }

}