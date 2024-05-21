package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Contextfeture {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Context는 각각의 구독을 통해 Reactor Sequence에 연결되며 체인의 각 연산자가 연결된 Context에 접근할 수 있다.
         *  구독이 발생할 때 마다 하나씩 연결된다.
         */
        String key1 = "id";

        Mono<String> mono = Mono.deferContextual(ctx ->
                        Mono.just("ID: " + ctx.get(key1))
                )
                .publishOn(Schedulers.parallel());

        mono.contextWrite(context -> context.put(key1, "itVillage"))
                .subscribe(data -> System.out.println("# subscriber 1 " + data));

        mono.contextWrite(context -> context.put(key1, "itWorld"))
                .subscribe(data -> System.out.println("# subscriber 2 " + data));

        Thread.sleep(100L);

    }
}
