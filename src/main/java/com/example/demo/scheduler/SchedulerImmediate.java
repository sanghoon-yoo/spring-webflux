package com.example.demo.scheduler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerImmediate {
    public static void main(String[] args) throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> System.out.println(Thread.currentThread().getName() + " filter:" + data))
                .publishOn(Schedulers.immediate())
                .map(data -> data * 10)
                .doOnNext(data -> System.out.println(Thread.currentThread().getName() + " map:" + data))
                .subscribe(data -> System.out.println(Thread.currentThread().getName() + " onNext: " + data));
        Thread.sleep(200L);
    }
}
