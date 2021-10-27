package com.github.tksburdo.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {

    public static void main(String[] args) {
        String bootstrapServer = "localhost:9092";
        Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);

        // Create the Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create a producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i<= 100; i++) {
            //create a producer record
            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>("first_topic", "helo world " + Integer.toString(i));

            // send data
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // executes evry time a record is successfully sent or an exceptions is thrown
                    if (e == null) {
                        logger.info("Recieved new metadata." +
                                "\nTopic:\t" + recordMetadata.topic() +
                                "\nPartition:\t" + recordMetadata.partition() +
                                "\nOffset:\t" + recordMetadata.offset() +
                                "\nTimestamp:\t" + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing", e);
                    }
                }
            });

        }

        // flush data
        producer.flush();

        // flush and close producer
        producer.close();
    }
}
