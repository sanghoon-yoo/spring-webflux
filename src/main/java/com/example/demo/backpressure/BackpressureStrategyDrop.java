package com.example.demo.backpressure;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyDrop {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 버퍼가 가득차는 시점에 emit되는 데이터는 drop
         */
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureDrop(dropped -> System.out.println("# dropped: " + dropped))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> {
                    try {
                        Thread.sleep(5L);
                        System.out.println("# onNext:" + data);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        Thread.sleep(2000L);
    }
}
