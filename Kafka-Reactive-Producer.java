package br.com.***REMOVED***.conciliation.kafka.producer;

import br.com.***REMOVED***.conciliation.config.KafkaConfig;
import br.com.***REMOVED***.conciliation.kafka.event.TecBanTransactionKafkaEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@AllArgsConstructor
public class ReactorKafkaProducer  {

    // ---------------- DEPS:
    // implementation "io.projectreactor.kafka:reactor-kafka:1.2.0.RELEASE"
    
    // ---------------- CONFIGS/BEANS:
    //@Configuration
    //public class KafkaBean {
    //
    //    @Value("${kafka.bootstrapServers}")
    //    private String bootstrapServers;
    //
    //    @Bean
    //    public KafkaSender<Integer, String> kafkaSender() {
    //
    //        final Map<String, Object> configProps = new HashMap<>();
    //        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    //        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    //        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    //
    //        SenderOptions<Integer, String> senderOptions = SenderOptions.create(configProps);
    //
    //        return KafkaSender.create(senderOptions);
    //    }
    //
    //}

    private KafkaSender<Integer, String> kafkaSender;
    private KafkaConfig kafkaConfig;

    public void sendKafkaEvent(SeuEvento seuEvento) {
        String message = seuEvento.toString();

        kafkaSender.send(Flux.just(message)
                // está sendo usado a própria mensagem como correlation metadata(no caso o i), mas caso queira fazer uso na resposta
                // pode ser usado um identificado único, como um accountId, etc
                .map(i -> SenderRecord.create(new ProducerRecord<>(kafkaConfig.getConciliationTopic(), message), i)))
                .next()
                .doOnError(e -> log.error("Ocorreu um erro ao tentar enviar a mensagem ao tópico Kafka", e.getStackTrace()))
                .subscribe(record -> {
                    log.info("Mensagem enviada com sucesso ao tópico Kafka. Mensagem={} | Tópico={}",
                            record.correlationMetadata(),
                            record.recordMetadata().topic());
                });
    }

}
