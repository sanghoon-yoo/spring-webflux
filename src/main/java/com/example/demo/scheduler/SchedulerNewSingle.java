package com.example.demo.scheduler;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerNewSingle {
    public static void main(String[] args) throws InterruptedException {
        doTask("task1")
                .subscribe(System.out::println);

        doTask("task2")
                .subscribe(System.out::println);

        Thread.sleep(200L);
    }

    private static Flux<Integer> doTask(String taskName) {
        return Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .publishOn(Schedulers.newSingle("new-single", true))
                .filter(data -> data > 3)
                .doOnNext(data -> System.out.println(taskName + Thread.currentThread().getName() + " filter:" + data))
                .map(data -> data + 10)
                .doOnNext(data -> System.out.println(taskName + Thread.currentThread().getName() + " map:" + data));
    }
}
