package com.example.demo.operator.multicast;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Publish {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 다수의 Subscriber와 Flux를 공유한다
         * Cold Sequence를 Hot Sequence로 변환한다
         * connect()가 호출 되기 전까지는 데이터를 emit하지 않는다
         */
        ConnectableFlux<Integer> flux = Flux
                .range(1, 5)
                .delayElements(Duration.ofMillis(300L))
                .publish();

        Thread.sleep(500L);
        flux.subscribe(data -> System.out.println("subscriber1: " + data));

        Thread.sleep(200L);
        flux.subscribe(data -> System.out.println("subscriber2: " + data));

        flux.connect();

        Thread.sleep(1000L);
        flux.subscribe(data -> System.out.println("subscriber3: " + data));

        Thread.sleep(2000L);
    }
}
