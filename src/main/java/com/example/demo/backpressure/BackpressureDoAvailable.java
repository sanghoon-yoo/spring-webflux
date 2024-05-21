package com.example.demo.backpressure;

import lombok.SneakyThrows;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureDoAvailable {
    public static void main(String[] args) {
        /**
         * Subscriber가 처리 가능한 만큼의 request 개수를 조절하는 Backpressure 예제
         * BackpressureDoAvailableMore과 함께 비교해볼 것
         */
        Flux.range(1, 5)
                .doOnNext(data -> System.out.println("# doOnNext: " + data))
                .doOnRequest(data -> System.out.println("# doOnRequest: " + data))
                .subscribe(new BaseSubscriber<Integer>() {
                    //구독 시점의 hook
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @SneakyThrows
                    @Override
                    protected void hookOnNext(Integer value) {
                        Thread.sleep(2000L);
                        System.out.println("# hookOnNext: " + value);
                        request(1);
                    }
                });
    }
}
