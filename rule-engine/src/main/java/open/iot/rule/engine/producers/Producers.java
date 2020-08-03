package open.iot.rule.engine.producers;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;


public class Producers {

    public static void PulsarClient() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();
        Producer<byte[]> producer = client.newProducer()
                .topic("my-topic")
                .create();
        producer.send("My message".getBytes());
        client.close();
        producer.close();
    }

}
