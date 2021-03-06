package com.oracle.tdc.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;
import com.oracle.tdc.msg.Customer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class CompatibleConsumer {
	
	public String message_cadastroq = null;

    public void consume() {
    	
             
        String authToken = "6H<(r1HWId3w80flTgbP";
        String tenancyName = "oraclemetodista";
        String username = "silvio.da.silva@oracle.com";
        String compartmentId = "oraclemetodista/silvio.da.silva@oracle.com/ocid1.streampool.oc1.phx.amaaaaaaeun4owyaohyjhfxjk3v52p4uhznswvcj4lyfdcfb23kqu3unbhyq";
        String topicName = "HACK-STREAM";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "https://cell-1.streaming.us-phoenix-1.oci.oraclecloud.com");
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-0");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        properties.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                        + tenancyName + "/"
                        + username + "/"
                        + compartmentId + "\" "
                        + "password=\""
                        + authToken + "\";"
        );
        properties.put("max.partition.fetch.bytes", 1024 * 1024); // limit request size to 1MB per partition

        Consumer<Long, String> consumer = new KafkaConsumer<>(properties);

        try {
            consumer.subscribe(Collections.singletonList( topicName ) );

            while(true) {
                Duration duration = Duration.ofMillis(1000);
                ConsumerRecords<Long, String> consumerRecords = consumer.poll(duration);
                consumerRecords.forEach(record -> {
                    System.out.println("Record Key " + record.key());
                    System.out.println("Record value " + record.value());
                    System.out.println("Record partition " + record.partition());
                    System.out.println("Record offset " + record.offset());
                    
                    String json = record.value();
                    Customer config = new Gson().fromJson(json, Customer.class);
                    String name = config.getName();
                    int age = config.getAge();
                    String cpf = config.getCpf();
                    
                    String msgJ = writeJSON(name, age, cpf);
                    
                    CompatibleProducer cp = new CompatibleProducer();
                    cp.produce(msgJ);
      
                });
                
                             
                // commits the offset of record to broker.
                consumer.commitAsync();
            }
        }
        catch(WakeupException e) {
            // do nothing, shutting down...
        }
        finally {
            System.out.println("closing consumer");
            consumer.close();
        }
        
        
    }
    public String writeJSON( String name, int age, String cpf )
    {
    	// --- criar objeto cliente --- //
    			Customer objCliente = new Customer(name, age, cpf);
    			
    			objCliente.setName(name);
    			objCliente.setAge(age);
    			objCliente.setCpf(cpf);
    			objCliente.setStatus("Aprovado");
    			
    			// --- transformando em JSON --- //
    			Gson gson = new Gson(); // conversor
    			String json = gson.toJson( objCliente );
    			
    			// exibindo o JSON //
    			System.out.println( json );
		return json;
    }


}
