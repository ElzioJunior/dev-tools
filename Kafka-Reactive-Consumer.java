package foobar;

.....
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverOffset;

import javax.annotation.PostConstruct;

@slf4j
@component
@AllArgsConstructor
public class KafkaConsumer {

    /**
	 * Infelizmente não da pra usar a mesma estratégia de Bean aqui, <br>
	 *  dado que é necessário o topico para instanciar o <br>
	 * 	receiver apontando para o tópico em específico.. <br>
	 * 	detalhes em {@link reactor.kafka.receiver.KafkaReceiver#create}
	 */
	/** 
        public KafkaReceiver<Integer, String> consumer(String topic) {

		Map<String, Object> consumerConfiguration = new HashMap<>();
		consumerConfiguration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		consumerConfiguration.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerName);
		consumerConfiguration.put(ConsumerConfig.GROUP_ID_CONFIG, consumerName);
		consumerConfiguration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfiguration.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		ReceiverOptions receiverOptions = ReceiverOptions.create(consumerConfiguration);

		ReceiverOptions<Integer, String> options = receiverOptions.subscription(Collections.singleton(topic))
				.addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
				.addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

		return KafkaReceiver.create(options);
	} **/

    private KafkaBean kafkaBean;
    private ObjectMapper objectMapper;

    @PostConstruct
    public void receiveEvent() {
        log.info("Creating new PurchaseConsumer");
        kafkaBean.consumer("SeuTopicoAqui")
                .receive()
                .flatMap(record -> {
                    ReceiverOffset offset = record.receiverOffset();
                    return readValue(record.value(), SuaClasseAqui.class)
                            .flatMap(event -> {
                                log.info("Evento recebido.... {}", event);
                                return suaServiceJava.seuMetodo(event);
                            })
                            .doOnError(err -> log.error("Erro ao executar processo de service acima", err))
                            .doOnTerminate(s -> {
                                log.info("Realizando ack na mensagem do offset {}", offset.offset());
                                offset.acknowledge();
                            })
			    // esse é um problema.... precisa retornar um objeto que não seja erro para não 
			    // quebrar a stream, sendo assim não para de processar :/ Tenho que achar uma solução melhor pra isso
			    .onErrorResume(err -> Mono.empty());
                })
                .subscribe();
    }

    private <T> Mono<T> readValue(String object, Class<T> classz) {
        try {
            T t = objectMapper.readValue(object, classz);
            return Mono.just(t);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter evento ao consumir kafka", e);
            return Mono.error(new InternalException("Erro ao converter evento ao consumir kafka " + e.getMessage()));
        }
    }
}
