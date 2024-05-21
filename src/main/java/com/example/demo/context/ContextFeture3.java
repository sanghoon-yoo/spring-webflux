package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ContextFeture3 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 동일한 key에 대해 write할 경우, 해당 키에 대한 값을 덮어 쓴다.
         */
        String key1 = "id";

        Mono.deferContextual(ctx ->
                        Mono.just("ID: " + ctx.get(key1))
                )
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.put(key1, "itWorld"))
                .contextWrite(context -> context.put(key1, "itVillage"))
                .subscribe(data -> System.out.println("# onNext: " + data));
        Thread.sleep(100L);
    }
}
