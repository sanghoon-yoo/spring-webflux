package com.example.demo.operator.peek;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class DoOnXxx {
    public static void main(String[] args) {
        /**
         * 실행 순서 확인해보기
         * DownStream에서 UpStream으로 전파되는 Operator는 무엇일까
         * 전파 방향 (구독자 > 퍼블리셔 방향 / 퍼블리셔 > 구독자 방향)을 주의해서 살펴볼 것
         */
        Flux
                .range(1, 5)
                .doFinally(signalType -> System.out.println("doFinally() > range"))
                .doOnNext(data -> System.out.println("range: " + data))
                .doOnRequest(n -> System.out.println("doOnRequest > range: " + n))
                .doOnSubscribe(subscription -> System.out.println("doOnSubscribe() > range"))
                .doFirst(() -> System.out.println("doFirst() > range"))
                .doOnComplete(() -> System.out.println("doOnComplete() > range"))
                .filter(num -> num % 2 == 1)
                .doOnNext(data -> System.out.println("filter: " + data))
                .doOnRequest(n -> System.out.println("doOnRequest > filter: " + n))
                .doOnSubscribe(subscription -> System.out.println("doOnSubscribe() > filter"))
                .doFinally(signalType -> System.out.println("doFinally() > filter"))
                .doOnComplete(() -> System.out.println("doOnComplete() > filter"))
                .doFirst(() -> System.out.println("doFirst() > filter"))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("# hookOnNext: " + value);
                        request(1);
                    }
                });
    }
}
