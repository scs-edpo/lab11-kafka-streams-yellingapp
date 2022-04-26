echo "Waiting for Kafka to come online..."

cub kafka-ready -b kafka:9092 1 20

# create the src-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic src-topic \
  --replication-factor 1 \
  --partitions 1 \
  --create

# create the out-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic out-topic \
  --replication-factor 1 \
  --partitions 1 \
  --create

sleep infinity