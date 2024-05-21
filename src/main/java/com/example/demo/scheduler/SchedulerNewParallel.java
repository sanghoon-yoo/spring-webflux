package com.example.demo.scheduler;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerNewParallel {
    public static void main(String[] args) throws InterruptedException {
        Mono<Integer> flux =
                Mono
                        .just(1)
                        .publishOn(Schedulers.newParallel("Parallel Thread", 4, true));

        flux.subscribe(data -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 1: " + data);
        });
        flux.subscribe(data -> {
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 2: " + data);
        });
        flux.subscribe(data -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 3: " + data);
        });
        flux.subscribe(data -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 4: " + data);
        });
        Thread.sleep(6000L);
    }
}
