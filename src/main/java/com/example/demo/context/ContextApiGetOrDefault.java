package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextApiGetOrDefault {
    public static void main(String[] args) throws InterruptedException {
        /**
         * getOrDefault는 값이 Context에 값이 없을 때 디폴트 값을 가져올 수 있는 메서드이다.
         */
        String key1 = "id";
        String key2 = "name";

        Mono.deferContextual(ctx ->
                        Mono.just("ID: " + ctx.get(key1) + ", " + "Name: " + ctx.get(key2) + ", " + "Job: " + ctx.getOrDefault("Job", "Software Engineer"))
                )
                .publishOn(Schedulers.parallel())
                .contextWrite(Context.of(key1, "itVillage", key2, "Sanghoon"))
                .subscribe(data -> System.out.println("# onNext: " + data));

        Thread.sleep(100L);
    }
}
