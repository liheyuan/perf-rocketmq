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

        if (args.length != 2) {
            System.out.println("Usage: Main <ns xxx:9867> <mode test/many/consumer>");
            return;
        }

        String ns = args[0];
        String mode = args[1];

        if (mode.equals("test")) {
            sendTest(ns);
        } else if (mode.equals("many")) {
            sendMany(ns);
        } else if (mode.equals("consumer")) {
            consume(ns);
        } else {
            System.out.println("invalid mode = " + mode);
        }


    }

    private static void sendTest(String ns) {
        try {

            int cnt = 100000;

            for (int threadCnt = 1; threadCnt < 16; threadCnt++) {
                int msgLen = 100;
                int topicCnt = 1;
                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
                        msgLen, topicCnt, threadCnt);
                ProducerUtils.sync(ns, cnt, msgLen, topicCnt, threadCnt);
            }

            System.out.println();

            for (int msgLen = 1; msgLen <= 10000; ) {
                int threadCnt = 4;
                int topicCnt = 1;
                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
                        msgLen, topicCnt, threadCnt);
                ProducerUtils.sync(ns, cnt, msgLen, topicCnt, threadCnt);

                if (msgLen < 1000) {
                    msgLen *= 10;
                } else {
                    msgLen += 1000;
                }
            }

            System.out.println();
            for (int topicCnt = 1; topicCnt < 20; topicCnt++) {
                int threadCnt = 4;
                int msgLen = 100;
                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
                        msgLen, topicCnt, threadCnt);
                ProducerUtils.sync(ns, cnt, msgLen, topicCnt, threadCnt);
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    private static void sendMany(String ns) {
        int cnt = 10000000;
        int msgLen = 100;
        try {
            for (int i = 0; i < 200; i++) {
                ProducerUtils.sync(ns, cnt, msgLen, 1, 8);
            }
        } catch (MQClientException e) {

        }
    }

    private static void consume(String ns) {
        int threads = 16;
        while (true) {
            System.out.format("Consume threadCnt=%d, ", threads);
            ConsumerUtils.consume(ns, threads);
        }
    }

}