package com.example.demo.backpressure;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyLatest {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 버퍼가 가득 찰 경우, 버퍼 밖에서 폐기되지 않고 대기하는 가장 최근에 emit된 데이터부터 버퍼에 채우는 전략
         */
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureLatest()
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
