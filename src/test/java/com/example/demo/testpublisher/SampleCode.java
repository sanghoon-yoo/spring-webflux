package com.example.demo.testpublisher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class SampleCode {
    @Test
    @DisplayName("TestPublisher를 사용해서 서비스 로직의 메서드에 대한 Unit Test를 실시하는 예제")
    public void divideByTwoTest() {
        TestPublisher<Integer> source = TestPublisher.create();

        StepVerifier
                .create(source.flux()
                        .zipWith(Flux.just(2, 2, 2, 2, 2), (x, y) -> x / y))
                .expectSubscription()
                .then(() -> source.next(2, 4, 6, 8, 10))
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("TestPublisher를 사용해서 서비스 로직의 메서드에 대한 Unit Test를 실시하는 예제2")
    public void divideByTwoTest2() {
        TestPublisher<Integer> source = TestPublisher.create();

        StepVerifier
                .create(source.flux()
                        .zipWith(Flux.just(2, 2, 2, 2, 0), (x, y) -> x / y))
                .expectSubscription()
                .then(() -> {
                    source.next(2, 4, 6, 8);
                    source.error(new ArithmeticException());
                })
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Reactive Streams의 사양을 위반해도 Publisher가 정상 동작하게 함으로써 서비스 로직을 검증하는 예제")
    public void divideByTwoTest3() {
        /**
         * 데이터가 emit되는 시점에서 NPE 발생
         */
        TestPublisher<Integer> source = TestPublisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL);
        /**
         * 데이터를 emit하기도 전에 NPE 발생
         */
//        TestPublisher<Integer> source = TestPublisher.create();

        StepVerifier
                .create(source.flux()
                        .zipWith(Flux.just(2, 2, 2, 2, 2), (x, y) -> x / y))
                .expectSubscription()
                .then(() -> source.next(2, 4, 6, 8, null))
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectComplete()
                .verify();
    }
}
