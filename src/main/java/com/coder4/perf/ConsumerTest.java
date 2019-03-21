/**
 * @(#)ConsumerTest.java, Mar 21, 2019.
 * <p>
 * Copyright 2019 fenbi.com. All rights reserved.
 * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.coder4.perf;

/**
 * @author coder4
 */
public class ConsumerTest {

    public static void main(String[] args) {

        String ns = "localhost:9876";

        int threadCnt = 8;
        System.out.format("threadCnt=%d, ", threadCnt);
        ConsumerUtils.consume(ns, 8);
    }

}