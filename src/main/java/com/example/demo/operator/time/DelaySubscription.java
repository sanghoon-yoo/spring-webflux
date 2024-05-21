package com.example.demo.operator.time;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class DelaySubscription {
    public static void main(String[] args) throws InterruptedException {
        /**
         * delaySubscription()은 구독 자체를 딜레이 시킨다
         * delaySubscription() 기점으로 전파된 stream은 뒤늦게 실행되는 것을 확인
         */
        Flux
                .range(1, 10)
                .doOnSubscribe(subscription -> System.out.println("# doOnSubscription > upstream"))
                .delaySubscription(Duration.ofSeconds(2))
                .doOnSubscribe(subscription -> System.out.println("# doOnSubscription > downstream"))
                .subscribe(data -> System.out.println(data));
        Thread.sleep(2500);
    }
}
