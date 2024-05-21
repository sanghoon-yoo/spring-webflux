package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextApiWrite {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Context API 중에서 write API 예제 코드
         * Context.of(...) 사용
         */
        String key1 = "id";
        String key2 = "name";
        Mono<String> mono =
                Mono.deferContextual(ctx ->
                                Mono.just("ID: " + " " + ctx.get(key1) + ", " + "Name: " + ctx.get(key2))
                        )
                        .publishOn(Schedulers.parallel())
                        .contextWrite(Context.of(key1, "itVillage", key2, "Sanghoon"));

        mono.subscribe(data -> System.out.println("# onNext: " + data));
        Thread.sleep(100L);
    }
}
