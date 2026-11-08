#  Kafka Producer/Consumer Sample

A tiny sample for a producer and consumer using Kafka to publish and subscribe to events within a topic.

## Structure
- `KafkaConsumerApp` is the consumer
- `KafkaProducerApp` creates events to consume
- `docker-compose.yml` defines the Kafka broker to run inside a container that maps to port 9092 on localhost

docker exec -it kafka-kafka-1 /opt/kafka/bin/kafka-topics.sh --create --topic orders --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

### Kafka Notes
- Kafka uses a bootstrap server to establish the initial connection
- Client discovers available brokers in the cluster
- Message consists of a key (ex. "1"), value (ex. "order created: 1"), metadata (ex. topic, partition, offset, timestamp, etc)
- Messages with the same key typically get routed to the same partition w default partitioning (helps for message ordering)
- Kafka distributes partitions among consumers in a given group. A partition can be assigned to only one consumer within a given group at a time
- If multiple consumer groups are created for one topic, they can also read the same msgs. So that's how multiple apps react to the same event