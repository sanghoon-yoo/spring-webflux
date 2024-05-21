package com.example.demo.publisherprobe;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

public class SampleCode {
    @Test
    public void publisherProbeTest() {
        /**
         * Sequence의 실행경로를 검증한다.
         */
        PublisherProbe<String> probe = PublisherProbe.of(Mono.just("# use Standby Power"));

        StepVerifier
                .create(Mono.empty()
                        /**
                         * 비어있는 Mono이기때문에 flatMap의 message가 없다.
                         */
                        .flatMap(message -> Mono.just(message))
                        /**
                         * Mono가 비어있기 때문에 switchIfEmpty로 인해 probe.mono()가 실행된다.
                         */
                        .switchIfEmpty(probe.mono())
                        .doOnNext(data -> System.out.println(data)))
                .expectNextCount(1)
                .verifyComplete();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}
