/**
 * @(#)ConsumerUtils.java, Mar 21, 2019.
 * <p>
 * Copyright 2019 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.coder4.perf;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author coder4
 */
public class ConsumerUtils {

    private static final String GROUP = "GROUP_TEST_CONSUMER";

    private static final String TOPIC = "topic0";

    public static void consume(String ns, int threads) {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(GROUP);

        // Threads
        consumer.setConsumeThreadMin(threads);
        consumer.setConsumeThreadMax(threads);

        // Specify name server addresses.
        consumer.setNamesrvAddr(ns);

        // cnt
        AtomicInteger succCnt = new AtomicInteger();

        AtomicLong tsFirst = new AtomicLong();

        AtomicLong tsLast = new AtomicLong();

        // Subscribe one more more topics to consume.
        try {
            consumer.subscribe(TOPIC, "*");

            // Register callback to execute on arrival of messages fetched from brokers.
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                tsFirst.compareAndSet(0, System.currentTimeMillis());
                succCnt.incrementAndGet();
                tsLast.set(System.currentTimeMillis());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });

            //Launch the consumer instance.
            long tsStart = System.currentTimeMillis();
            System.out.printf("Consumer Started.%n");
            consumer.start();

            Thread.sleep(1000 * 10);

            consumer.shutdown();

            System.out.format("TPS=%.2f\n", (double) succCnt.get() * 1000 / (double) (tsLast.get() - tsFirst.get()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}