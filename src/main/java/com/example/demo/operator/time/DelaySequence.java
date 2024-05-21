package com.example.demo.operator.time;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class DelaySequence {
    public static void main(String[] args) throws InterruptedException {
        /**
         * delaySequence()는 구독은 바로 되지만 emit을 지연시킨다
         * stream자체는 바로 실행되지만(구독되지만) 데이터만 뒤늦게 emit되는 것을 확인
         */
        Flux
                .range(1, 10)
                .doOnSubscribe(subscription -> System.out.println("# doOnSubscription > upstream"))
                .delaySequence(Duration.ofSeconds(2))
                .doOnSubscribe(subscription -> System.out.println("# doOnSubscription > downstream"))
                .subscribe(data -> System.out.println(data));
        Thread.sleep(2500);
    }
}
