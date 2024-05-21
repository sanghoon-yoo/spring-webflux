package com.example.demo.operator.multicast;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Replay {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 다수의 Subscriber와 Flux를 공유한다
         * Cold Sequence를 Hot Sequence로 변환한다
         * connect()가 호출되기 전까지는 데이터를 emit하지 않는다
         * 구독 시점 이전에 emit된 데이터를 모두 캐시한다
         */
        ConnectableFlux<Integer> flux = Flux
                .range(1, 5)
                .delayElements(Duration.ofMillis(300L))
                .replay(2);

        Thread.sleep(500L);
        flux.subscribe(data -> System.out.println("subscriber1: " + data));

        Thread.sleep(200L);
        flux.subscribe(data -> System.out.println("subscriber2: " + data));

        flux.connect();

        Thread.sleep(1000L);
        /**
         * 뒤늦게 구독해도 못받았던 데이터까지 받을 수 있다 (파라미터로 지정한 개수만큼, default는 모두)
         */
        flux.subscribe(data -> System.out.println("subscriber3: " + data));

        Thread.sleep(2000L);
    }
}
