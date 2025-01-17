import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import javax.swing.*;
import java.util.Collections;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1)
                .kafkaPorts(9092);
        embeddedKafkaBroker.afterPropertiesSet();

        SwingUtilities.invokeLater(Login::new);
    }
}



class MessageProducer {
    private static final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(
            Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()
            )
    );

    public static void send(ProducerRecord<String, String> producerRecord) {
        kafkaProducer.send(producerRecord);
    }
}


class MessageConsumer {
    KafkaConsumer<String, String> kafkaConsumer;

    public MessageConsumer(String topic, String id) {
        this.kafkaConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        ConsumerConfig.GROUP_ID_CONFIG, id + "-" + System.currentTimeMillis(),
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"
                )
        );

        kafkaConsumer.subscribe(Collections.singletonList(topic));
    }
}