package com.example.demo.operator.createsequence;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class And {
    public static void main(String[] args) throws InterruptedException {
        /**
         * and 기본 개념 예제
         * Mono와 파라미터로 입력된 Publisher가 종료할때까지 대기한 후, Mono<Void>를 반환한다.
         */
        Mono
                .just("Okay")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .and(
                        Flux
                                .just("Hi", "Tom")
                                .delayElements(Duration.ofSeconds(2))
                                .doOnNext(System.out::println)
                )
                .subscribe(
                        data -> System.out.println("#onNext:" + data),
                        error -> System.out.println("#onError:" + error),
                        () -> System.out.println("#onComplete")
                );

        Thread.sleep(5000);

        /**
         * and 활용 예제
         * 2개의 task가 모두 끝났을 때, Complete Signal을 전달해서 추가 task를 수행하는 예제
         */
        restartApplicationServer()
                .and(restartDBServer())
                .subscribe(
                        data -> System.out.println("#onNext : " + data),
                        error -> System.out.println("#onError : " + error),
                        () -> System.out.println("All servers are restarted successfully.")
                );
        Thread.sleep(6000L);
    }

    private static Mono<String> restartApplicationServer() {
        return Mono
                .just("Application Server was restarted seccessfully.")
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(System.out::println);
    }

    private static Publisher<String> restartDBServer() {
        return Mono
                .just("DB Server was restarted successfully.")
                .delayElement(Duration.ofSeconds(4))
                .doOnNext(System.out::println);
    }
}
