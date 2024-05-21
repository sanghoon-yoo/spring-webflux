package com.example.demo.sink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

public class UnicastOnMultithread {
    public static void main(String[] args) throws InterruptedException {
        int tasks = 6;
        //Sinks unicast는 단일 구독자에게만 emit하므로 멀티스레드 환경에서도 thread-safe하다.
        Sinks.Many<String> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux<String> fluxView = unicastSink.asFlux();
        IntStream
                .range(1, tasks)
                .forEach(n -> {
                    try {
                        new Thread(() -> {
                            unicastSink.emitNext(doTask(n), Sinks.EmitFailureHandler.FAIL_FAST);
                            System.out.println("# emitted: " + n);
                        }).start();
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                    }
                });

        fluxView
                .publishOn(Schedulers.parallel())
                .map(result -> result + " success!")
                .doOnNext(n -> System.out.println("# map(): " + n))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> System.out.println("# onNext: " + data));
    }

    private static String doTask(int n) {
        return "task " + n + " result";
    }
}
