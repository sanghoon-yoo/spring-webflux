package com.example.demo.sink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.stream.IntStream;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class CreateOperator {
    public static void main(String[] args) throws InterruptedException {
        int tasks = 6;
        //Flux craete operator 샘플. FluxSink.next를 해야 create 및 emit 된다. Schedulers.parallel()로 별도 스레드로 emit할때와 안할때의 차이도 확인해볼 것.
        Flux
                .create((FluxSink<String> sink) -> {
                    IntStream
                            .range(1, tasks)
                            .forEach(n -> sink.next(doTask(n)));
                })
                /**
                 * subscribeOn은 퍼블리셔의 emit부터 downstream까지 모든 과정을 별도의 스레드에서 동작하도록 한다.
                 * publishOn 이전의 upstream까지 boundedElastic에서 실행된다.
                 */
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(n -> System.out.println("# create(): " + n))
                /**
                 * publishOn 이후의 downstream은 퍼블리셔의 downstream과는 다른 별도의 스레드에서 동작한다.
                 */
                .publishOn(Schedulers.parallel())
                .map(result -> result + " success!")
                .doOnNext(n -> System.out.println("# map(): " + n))
                /**
                 * publishOn이 한번 더 나오게 되면 또 다른 별도의 스레드에서 동작한다.
                 */
                .publishOn(Schedulers.parallel())
                .subscribe(data -> System.out.println("# onNext: " + data));
        Thread.sleep(500L);


    }

    private static String doTask(int n) {
        return "task " + n + " result";
    }
}
