import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerApp {
    public static void main(String[] args) {
        Properties props = new Properties();

        props.put( // where the connection is
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        props.put( // configures consumer group
                ConsumerConfig.GROUP_ID_CONFIG,
                "order-consumer-group"
        );

        props.put( // translates bytes from msgs back into Java objects
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName()
        );

        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName()
        );

        props.put( // where kafka starts reading when consumer group has no previously committed offset for a partition
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest" // aka earliest available msgs.
        );


        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe( // consumer subscribes to orders topic
                    Collections.singletonList("orders")
            );

            System.out.println("Listening for orders...");

            while (true) {
                // ask kafka for new msgs
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));

                // extract info from new msgs
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(
                            "Received: " + record.value() +
                            " | Key: " + record.key() +
                            " | Partition: " + record.partition()
                    );
                }
            }
        }
    }
}
