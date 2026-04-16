import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class YellingApp {

    private static final Logger LOG = LoggerFactory.getLogger(YellingApp.class);

    public static void main(String[] args) throws InterruptedException {

        // set the required properties for running Kafka Streams

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "yelling_app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Serde<String> stringSerde = Serdes.String();

        // the builder is used to construct the topology
        StreamsBuilder builder = new StreamsBuilder();

        // read from the source topic, "src-topic"
        KStream<String, String> originalStream = builder.stream("src-topic", Consumed.with(stringSerde, stringSerde));

        // for each record that appears in the source topic,
        // print the value
        originalStream.foreach(
                (key, value) -> {
                    System.out.println("Original text: " + value);
                });

        // Create the upper cased stream
        KStream<String, String> upperCasedStream = originalStream.mapValues(v -> v.toUpperCase());

        upperCasedStream.to("out-topic", Produced.with(stringSerde, stringSerde));
        upperCasedStream.print(Printed.<String, String>toSysOut().withLabel("Yelling App"));

        // you can also print using the `print` operator
        // stream.print(Printed.<String, String>toSysOut().withLabel("source"));


        // build the topology and start streaming
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
