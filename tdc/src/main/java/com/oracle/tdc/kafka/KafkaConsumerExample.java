package com.oracle.tdc.kafka;

import com.oracle.tdc.msg.Customer;
public class KafkaConsumerExample {

    public static void main(String... args) throws Exception {
        System.out.println("consumer");
        CompatibleConsumer consumer = new CompatibleConsumer();
        consumer.consume();
    }



}
