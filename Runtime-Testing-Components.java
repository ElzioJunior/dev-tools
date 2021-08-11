package br.com.***REMOVED***.cardsrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Test {

    @Autowired
    TeuComponente

    // roda a cada 1 segundo
    @Scheduled(fixedDelay = 1000L)
    public void test() {
        // teu teste injetando teu componente
    }
}
