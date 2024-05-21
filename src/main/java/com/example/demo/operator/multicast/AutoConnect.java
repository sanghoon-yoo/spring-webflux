package com.example.demo.operator.multicast;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class AutoConnect {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 다수의 Subscriber와 Flux를 공유한다
         * Cold Sequence를 Hot Sequence로 변환한다
         * 파라미터로 입력한 숫자 만큼의 구독이 발생하는 시점에 connect()가 자동으로 호출된다
         */
        Flux<String> publisher = Flux
                .just("Concert part1", "Concert part2", "Concert part3")
                .delayElements(Duration.ofMillis(300L))
                .publish()
                .autoConnect(2);

        Thread.sleep(500L);
        publisher.subscribe(data -> System.out.println("# audience 1 is watching " + data));

        Thread.sleep(500L);
        publisher.subscribe(data -> System.out.println("# audience 2 is watching " + data));

        Thread.sleep(500L);
        publisher.subscribe(data -> System.out.println("# audience 3 is watching " + data));

        Thread.sleep(1000L);
    }
}
