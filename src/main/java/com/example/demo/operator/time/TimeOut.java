package com.example.demo.operator.time;

import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class TimeOut {
    public static void main(String[] args) throws InterruptedException {
        /**
         * emit되는 데이터가 없다면 TimeOutException을 발생시킨다
         * error signal을 발생시킨다
         */
        Hooks.onOperatorDebug();

        requestToServer()
                .timeout(Duration.ofSeconds(2))
                .subscribe(
                        data -> System.out.println("success : " + data)
                        , error -> System.out.println("error : " + error)
                );
        Thread.sleep(3500);
    }
    private static Mono<String> requestToServer() {
        return Mono.just("complete to process from client successfully")
                .delayElement(Duration.ofSeconds(3));
    }
}
