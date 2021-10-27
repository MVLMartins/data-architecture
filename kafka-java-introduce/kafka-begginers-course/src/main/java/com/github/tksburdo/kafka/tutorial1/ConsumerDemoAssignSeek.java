package com.github.tksburdo.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoAssignSeek {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class.getName());


        // https://www2.javhdporn.net/video/nhdtb-103/
        String bootstrapServer = "localhost:9092";
        String topic = "first_topic";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // assign and seek are mostly used to replay data pr fetch a specific message

        // Assign
        TopicPartition partitionToReadFrom = new TopicPartition(topic, 0);
        long offsetToReadFrom = 15L;
        consumer.assign(Collections.singletonList(partitionToReadFrom));

        // Seek
        consumer.seek(partitionToReadFrom, offsetToReadFrom);

        int messagesToRead = 5;
        boolean keepOnReading = true;
        int numberOfMessagesReadSoFar = 0;


        // poll for ner data
        while (keepOnReading){

            ConsumerRecords<String,String> records=
                    consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record : records){
                numberOfMessagesReadSoFar += 1;
                logger.info("Key: " + record.key());
                logger.info("Partition: " + record.partition());
                logger.info("Offset: " + record.offset());
                logger.info("Value: " + record.value());
                logger.info("Timestamp: " + record.timestamp());

                logger.info("\n\n\n");

                if ( numberOfMessagesReadSoFar >= messagesToRead ) {
                    keepOnReading = false;
                    break;
                }

            }

        }


    }
}
