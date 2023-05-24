import edu.itmo.blps.service.KafkaConsumerService;
import edu.itmo.blps.service.KafkaProducerService;
import org.apache.kafka.clients.producer.internals.Sender;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import javax.sound.midi.Receiver;

@SpringBootTest(classes = {KafkaProducerService.class, KafkaConsumerService.class})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class SpringKafkaTest {
    @Test
    public void test(){
        Boolean b = Boolean.valueOf("true");
        System.out.print(b.toString());
    }
}
