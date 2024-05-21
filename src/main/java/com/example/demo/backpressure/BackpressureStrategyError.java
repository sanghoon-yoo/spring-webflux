package com.example.demo.backpressure;

import ch.qos.logback.core.util.TimeUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyError {
    public static void main(String[] args) throws InterruptedException {
        /**
         * emit보다 subscribe되는 속도가 느려서 버퍼가 가득찰 경우 error출력
         */
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureError()
                .doOnNext(System.out::println)
                .publishOn(Schedulers.parallel())
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("# onNext: " + data);
                },
                        error -> System.out.println(error));
        Thread.sleep(2000L);
    }
}
