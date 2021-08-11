@Bean
public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory(
    ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
    ConsumerFactory<Object, Object> kafkaConsumerFactory,
    KafkaTemplate<Object, Object> template) {
    
  ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
  configurer.configure(factory, kafkaConsumerFactory);
  factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), 3));
  
  return factory;
}

@KafkaListener(id = "fooGroup", topics = "topic1")
public void listen(String in) {
  logger.info("Received: " + in);
  if (in.startsWith("foo")) {
    throw new RuntimeException("failed");
  }
}

@KafkaListener(id = "dltGroup", topics = "topic1.DLT")
public void dltListen(String in) {
  logger.info("Received from DLT: " + in);
}