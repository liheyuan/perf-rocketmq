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
public class ProducerTest {

    public static void main(String[] args) {
        String ns = "localhost:9876";
        String tag = "tag1";

        int cnt = 100000;

        try {

//            for (int threadCnt = 1; threadCnt < 16; threadCnt++) {
//                int msgLen = 100;
//                int topicCnt = 1;
//                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
//                        msgLen, topicCnt, threadCnt);
//                ProducerUtils.sync(ns, tag, cnt, msgLen, topicCnt, threadCnt);
//            }
//
//            System.out.println();
//
//            for (int msgLen = 1; msgLen <= 10000; ) {
//                int threadCnt = 4;
//                int topicCnt = 1;
//                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
//                        msgLen, topicCnt, threadCnt);
//                ProducerUtils.sync(ns, tag, cnt, msgLen, topicCnt, threadCnt);
//
//                if (msgLen < 1000) {
//                    msgLen *= 10;
//                } else {
//                    msgLen += 1000;
//                }
//            }
//
//            System.out.println();
//            for (int topicCnt = 1; topicCnt < 20; topicCnt++) {
//                int threadCnt = 4;
//                int msgLen = 100;
//                System.out.format("Sync Send Test 10w, len=%d, topicCnt=%d, threadCnt=%d, ",
//                        msgLen, topicCnt, threadCnt);
//                ProducerUtils.sync(ns, tag, cnt, msgLen, topicCnt, threadCnt);
//            }

            System.out.println("Async Test 1w * 10, len=100");
            cnt = 1000000;
            int msgLen = 100;
            for (int i = 0; i < 20; i++) {
                ProducerUtils.async(ns, tag, cnt, msgLen, 1, 8);
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

}