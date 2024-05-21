package com.example.demo.operator.createsequence;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class When {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 파라미터로 입력된 Publisher들이 종료할 때 까지 대기한 후, Mono<Void>를 반환한다.
         * Mono에서 emit하는 데이터를 DownStream으로 전달되지 않는다.
         */
        Mono
                .when(
                        Flux
                                .just("Hi", "Tom")
                                .delayElements(Duration.ofSeconds(2))
                                .doOnNext(System.out::println),
                        Flux
                                .just("Hello", "David")
                                .delayElements(Duration.ofSeconds(1))
                                .doOnNext(System.out::println)
                )
                .subscribe(
                        data -> System.out.println("#onNext:" + data),
                        error -> System.out.println("#onError:" + error),
                        () -> System.out.println("#onComplete")
                );
        Thread.sleep(5000);

        /**
         * when 활용 예제
         */
        Mono
                .when(restartApplicationServer(), restartDBServer(), restartStorageServer())
                .subscribe(
                        data -> System.out.println("#onNext:" + data),
                        error -> System.out.println("#onError:" + error),
                        () -> System.out.println("All servers are restarted successfully.")
                );
        Thread.sleep(6000);
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

    private static Publisher<String> restartStorageServer() {
        return Mono
                .just("Storage Server was restarted successfully.")
                .delayElement(Duration.ofSeconds(3))
                .doOnNext(System.out::println);
    }
}
