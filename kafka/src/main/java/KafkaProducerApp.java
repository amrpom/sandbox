import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerApp {
    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        props.put( // kafka doesn't understand Java objects (stores msgs as bytes), so you need to define a serializer
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );

        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName()
        );

        try(KafkaProducer<String, String> producer = // configures key and message types for producer
                new KafkaProducer<>(props)) {
            for (int i = 0; i <= 5; i++) {
                String message = "Order created: " + i;

                ProducerRecord<String, String> record = // creates messages
                        new ProducerRecord<>(
                                "orders",
                                String.valueOf(i),
                                message
                        );
                producer.send(record, (metadata, exception) -> { // send is async
                    if (exception == null) {
                        System.out.println(
                                "Sent: " + message +
                                " | Partition: " + metadata.partition()
                        );
                    } else {
                        exception.printStackTrace();
                    }
                });
            }
        }

    }
}
