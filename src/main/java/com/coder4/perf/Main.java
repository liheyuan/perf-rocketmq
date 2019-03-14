/**
 * @(#)Main.java, Mar 14, 2019.
 * <p>
 * Copyright 2019 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.coder4.perf;

import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author coder4
 */
public class Main {

    public static void main(String[] args) {
        String ns = "localhost:9876";
        String topic = "topic1";
        String tag = "tag1";

        try {

            System.out.println("Sync Test 1w * 10, len=100");
            int cnt = 10000;
            int msgLen = 100;
            int n = 10;
            for (int i = 0; i < n; i++) {
                long start = System.currentTimeMillis();
                ProducerUtils.sync(ns, topic, tag, cnt, msgLen);
                long end = System.currentTimeMillis();
                System.out.format("TPS: %.2f\n", (double) cnt * 1000 / (double) (end - start));
            }

//            System.out.println("Async Test 10 * 10, len=100");
//            cnt = 1000;
//            msgLen = 100;
//            n = 10;
//            for (int i = 0; i < n; i++) {
//                long start = System.currentTimeMillis();
//                ProducerUtils.async(ns, topic, tag, cnt, msgLen);
//                long end = System.currentTimeMillis();
//                System.out.format("TPS: %.2f\n", (double) cnt * 1000 / (double) (end - start));
//            }


        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

}