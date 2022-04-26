# lab11-kafka-streams-yellingapp
This code partially corresponds with Chapter 2 in the O'Reilly book: [Mastering Kafka Streams and ksqlDB](https://www.kafka-streams-book.com/) by Mitch Seymour mixed
with the Yelling App example from Chapter 3 in the book [Kafka Streams in Action](https://github.com/bbejeck/kafka-streams-in-action).

## Running Locally
You can start the local Kafka cluster using the following command:

```sh
$ docker-compose up
```

Be aware that we are using Kafka and Zookeeper containers provided by Confluent from now on, which are different from the previous labs (see [docker-compose.yml](/docker-compose.yml))!

Once Kafka is up, the script [create-topics.sh](/scripts/create-topics.sh) is executed and the two topics 'src-topic' and 'out-topic' will be created.

This lab is based on the high-level **DSL example** from the 'Mastering Kafka Streams and ksqlDB' book.

## DSL example

You can run the main application [YellingApp](/src/main/java/YellingApp.java) simply via your IDE as we have also provided a Maven
build script (see [pom.xml](/pom.xml)). As defined in the topology, the app will connect to the 'src-topic' topic as originalStream
and for each value arriving at this stream, print its value. It also transforms the event's value and streams it to the 'out-topic', printing it also.


## Testing the App
Once the Kafka Streams application is running, open a new shell tab and produce some data to the source topic (`src-topic`).

```sh
$ docker-compose exec kafka bash

$ kafka-console-producer \
    --bootstrap-server kafka:9092 \
    --topic src-topic
```

This will drop you in a prompt:

```sh
>
```

Now, type a few words, followed by `<ENTER>`.

```sh
>hello world
>hello izzy
```

You will see the following output if running the DSL example:
```sh
HELLO WORLD
HELLO IZZY
```

You can additionally directly listen to the 'src-topic' and/or 'out-topic' in Kafka in a new shell:

```sh
$ docker-compose exec kafka bash

$ kafka-console-consumer \
    --bootstrap-server kafka:9092 \
    --topic out-topic \
    --from-beginning
```
