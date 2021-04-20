package com.oracle.tdc.kafka;

public class KafkaProducerExample {

    public static void main(String... args) throws Exception {
        System.out.println("producer");
        CompatibleProducer producer = new CompatibleProducer();
        producer.produce("teste");
    }

}
