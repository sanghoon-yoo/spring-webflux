package com.example.demo.operator.createsequence;

import reactor.core.publisher.Mono;

import java.time.Duration;

public class Then {
    public static void main(String[] args) throws InterruptedException {
        /**
         * UpStream Mono의 sequence가 종료되면, Mono<Void>를 DownStream으로 전달한다.
         */
        Mono
                .just("Hi")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .then()
                .subscribe(
                        data -> System.out.println("#onNext:" + data),
                        error -> System.out.println("#onError:" + error),
                        () -> System.out.println("#onComplete")
                );
        Thread.sleep(1500);

        /**
         * 1개의 task가 모두 끝났을 때, Complete Signal을 전달해서 추가 task를 수행하는 예제
         */
        restartApplicationServer()
                .then()
                .subscribe(
                        data -> System.out.println("#onNext:" + data), //then()으로 complete signal만 발생되도록 했기 때문에 실행되지 않음
                        error -> System.out.println("#onError:" + error),
                        () -> System.out.println("All servers are restarted successfully.")
                );
        Thread.sleep(3000);
    }

    private static Mono<Void> restartApplicationServer() {
        return Mono
                .just("Application Server was restarted seccessfully.")
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(System.out::println)
                .flatMap(notUse -> Mono.empty());
    }
}
