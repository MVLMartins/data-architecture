package com.github.tksburdo.kafka.tutorial1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerDemoKeys {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String bootstrapServer = "localhost:9092";
        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);

        // Create the Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create a producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i<= 10; i++) {
            //create a producer record

            String topic = "first_topic";
            String value = "helo world " + Integer.toString(i);
            String key   = "id_" + Integer.toString(i);


            ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(topic, key, value);

            logger.info("Key: " + key);

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
            }).get(); // block the .send to make ir synchronous - don't do this in production!

        }

        // flush data
        producer.flush();

        // flush and close producer
        producer.close();
    }
}
