package com.example.demo.backpressure;

import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureStrategyBufferDropLatest {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 데이터가 버퍼에 가득 찰 경우, 버퍼 안에 있는 데이터 중에서 가장 최근에 버퍼로 들어온 데이터부터 Drop시키는 전략
         */
        Flux
                .interval(Duration.ofMillis(300L))
                .doOnNext(data -> System.out.println("# emitted by original Flux: " + data))
                /**
                 * 버퍼 사이즈 2
                 */
                .onBackpressureBuffer(2,
                        dropped -> System.out.println("# Overflow & dropped:" + dropped),
                        BufferOverflowStrategy.DROP_LATEST
                )
                .doOnNext(data -> System.out.println("# emitted by Buffer: " + data))
                /**
                 * prefetch : 스케줄러가 신호를 처리하기 전 미리가져올 데이터의 개수
                 * 버퍼와 같은 역할을 해서, Overflow & dropped이 일어나게 만든다
                 */
//                .publishOn(Schedulers.parallel())
                .publishOn(Schedulers.parallel(), false, 1)
                .subscribe(data -> {
                    try {
                        Thread.sleep(1000L);
                        System.out.println("# onNext:" + data);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
        Thread.sleep(3000L);

    }
}
