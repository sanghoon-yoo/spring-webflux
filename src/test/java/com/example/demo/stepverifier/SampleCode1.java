package com.example.demo.stepverifier;

import com.example.demo.TimeBasedExample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.util.function.Tuples;

import java.sql.Time;
import java.time.Duration;


public class SampleCode1 {
    @Test
    @DisplayName("간단한 문자 emit 데이터 검증")
    public void sayHelloReactorTest() {
        StepVerifier
                /**
                 * 테스트 대상 Sequence 생성
                 */
                .create(Mono.just("Hello Reactor"))
                /**
                 * onNext Signal에 대한 emit 데이터 검증
                 */
                .expectNext("Hello Reactor")
                /**
                 * onComplete Signal 검증
                 */
                .expectComplete()
                /**
                 * 검증 실행
                 */
                .verify();
    }

    @Test
    @DisplayName("Flux emit 데이터 검증")
    public void fluxReactorTest() {
        StepVerifier
                .create(Flux.just("Hello", "Reactor"))
                /**
                 * 구독 발생 이벤트 검증
                 */
                .expectSubscription()
                .expectNext("Hello")
                .expectNext("Reactor")
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("as를 사용해서 실패한 expectXXXX()에게 description을 지정하는 예제")
    public void asReactorTest() {
        StepVerifier
                .create(Flux.just("Hello", "Reactor"))
                .expectSubscription()
                .as("# expect subscription")
                .expectNext("Hi")
                .as("# expect Hi")
                .expectNext("Reactor")
                .as("# expect Reactor")
                .verifyComplete();
    }

    @Test
    @DisplayName("실패를 검증하는 예제")
    public void occurErrorTest() {
        StepVerifier
                .create(Flux.just(2, 4, 6, 8, 10)
                        .zipWith(Flux.just(2, 2, 2, 2, 0), (x, y) -> x / y))
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("한꺼번에 검증하는 예제")
    public void expectNextTest() {
        StepVerifier
                .create(Flux.just(2, 4, 6, 8, 10)
                        .zipWith(Flux.just(2, 2, 2, 2, 2), (x, y) -> x / y))
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("onNext Signal를 통해 emit된 데이터의 개수를 검증")
    public void rangeNumberTest() {
        Flux<Integer> source = Flux.range(0, 1000);
        StepVerifier
                .create(
                        /**
                         * Flux에서 n개까지만 emit하겠다고 정의
                         */
                        source.take(500),
                        /**
                         * 검증에 실패할 경우 StepVerifierOptions에서 지정한 Scenario Name이 표시된다.
                         */
                        StepVerifierOptions.create().scenarioName("Veryfy from 0 to 499"))
                .expectSubscription()
                .expectNext(0)
                .expectNextCount(498)
                .expectNext(499)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("실제 시간을 가상 시간으로 대체하는 테스트 예제")
    public void getCOVID19CountTest() {
        StepVerifier
                .withVirtualTime(() -> TimeBasedExample.getCOVID19Count(
                        /**
                         * 12시간의 Duration을 가진 뒤 1개의 데이터를 emit
                         */
                                Flux.interval(Duration.ofHours(12)).take(1)
                        )
                )
                .expectSubscription()
                /**
                 * VirtualTimeScheduler를 이용해서 12시간 만큼 시간을 미래로 보낸다.
                 * 원래라면 12시간을 기다려야하지만 withVirtualTime를 사용했기 떄문에 바로 테스트가 된다.
                 */
                .then(() -> VirtualTimeScheduler.get().advanceTimeBy(Duration.ofHours(12)))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("실제 시간을 가상 시간으로 대체하는 테스트 예제2")
    public void getCOVID19CountTest2() {
        StepVerifier
                .withVirtualTime(() -> TimeBasedExample.getCOVID19Count(
                                Flux.interval(Duration.ofHours(12)).take(1)
                        )
                )
                .expectSubscription()
                /**
                 * 전자와 차이점은 thenAwait은 실행시점을 현재 시점의 시간으로 당긴다.
                 */
                .thenAwait(Duration.ofHours(12))
                .expectNextCount(11)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("검증에 소요되는 시간을 제한하는 예제")
    public void getCOVIDCountTimeTest() {
        StepVerifier
                .create(TimeBasedExample.getCOVID19Count(
                        Flux.interval(Duration.ofSeconds(2)).take(1)
                ))
                .expectSubscription()
                .expectNextCount(11)
                .expectComplete()
                .verify(Duration.ofSeconds(3));
    }

    @Test
    @DisplayName("expectNoEvent(Duration)으로 지정된 대기 시간동안 이벤트가 없음을 확인하는 예제")
    public void getCOVID19CountTimeTest2() {
        StepVerifier
                .withVirtualTime(() -> TimeBasedExample.getVoteCount(
                        Flux.interval(Duration.ofMinutes(1))
                ))
                .expectSubscription()
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNext(Tuples.of("중구", 15400))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNext(Tuples.of("서초구", 20020))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }
}
