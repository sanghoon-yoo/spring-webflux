package com.example.demo.scheduler;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class SchedulerNewBounded {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 스레드가 가득 차고, 대기할 큐도 없는 경우 Exception이 발생한다.
         * newBoundedElastic는 데몬 스레드가 아닌 유저 스레드로 실행되기 때문에 scheduler.dispose()로 종료한다.
         */
        Scheduler scheduler = Schedulers.newBoundedElastic(2, 2, "I/O-Thread");
        Mono<Integer> mono =
                Mono
                        .just(1)
                        .subscribeOn(scheduler);
        System.out.println("# Start");

        mono.subscribe(data -> {
            System.out.println("subscribe 1 doing: " + data);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 1 done: " + data);
        });

        mono.subscribe(data -> {
            System.out.println("subscribe 2 doing: " + data);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("subscribe 2 done: " + data);
        });

        mono.subscribe(data -> {
            System.out.println("subscribe 3 doing: " + data);
        });

        mono.subscribe(data -> {
            System.out.println("subscribe 4 doing: " + data);
        });

        mono.subscribe(data -> {
            System.out.println("subscribe 5 doing: " + data);
        });

        mono.subscribe(data -> {
            System.out.println("subscribe 6 doing: " + data);
        });

        Thread.sleep(4000L);
        scheduler.dispose();
    }
}
