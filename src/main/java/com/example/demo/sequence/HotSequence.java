package com.example.demo.sequence;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class HotSequence {
    public static void main(String[] args) throws InterruptedException {
        Flux<String> concertFlux = Flux.fromStream(Stream.of("Singer A", "Singer B", "Singer C", "Singer D", "Singer E"))
                .delayElements(Duration.ofSeconds(1)).share(); // share() 원본 Flux를 여러 Subscriber가 공유한다.

        concertFlux.subscribe(singer -> System.out.println("# Subscriber1 is watching " + singer + "'s song"));

        Thread.sleep(2500);

        concertFlux.subscribe(singer -> System.out.println("# Subscriber2 is watching " + singer + "'s song"));

        Thread.sleep(3000);
    }
}
