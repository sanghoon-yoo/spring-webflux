package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class Take {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 파라미터로 입력한 숫자만큼 DownStream으로 emit한다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .doOnNext(data -> System.out.println("[sample1] upstream에서 emit된 데이터 : " + data))
                .take(3)
                .subscribe(System.out::println);
        Thread.sleep(5000L);

        /**
         * 파라미터로 입력한 시간 내에 UpStream에서 emit된 데이터만 DownStream으로 emit한다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .doOnNext(data -> System.out.println("[sample2] upstream에서 emit된 데이터 : " + data))
                .take(Duration.ofSeconds(2))
                .subscribe(System.out::println);
        Thread.sleep(4000L);

        /**
         * emit된 데이터 중에서 파라미터로 입력된 갯수만큼 가장 마지막에 emit된 데이터만 emit한다.
         */
        Flux
                .fromIterable(Arrays.asList("apple", "orange", "grape"))
                .takeLast(2)
                .subscribe(System.out::println);

        /**
         * 파라미터로 입력되는 Predicate가 true가 될때까지 emit한다.
         * emit된 데이터에는 Predicate가 true로 매칭되는 데이터가 포함된다.
         */
        Flux
                .fromIterable(Arrays.asList("apple", "orange", "grape"))
                .takeUntil(data -> data.equals("orange"))
                .subscribe(System.out::println);

        /**
         * 파라미터로 입력된 Publisher가 onNext 또는 onComplete signal을 발생시킬 때까지 Upstream에서 emit된 데이터만 emit한다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .takeUntilOther(
                        /**
                         * 아래 Publisher는 2.5초 뒤 onComplete를 발생시킨다.
                         */
                        Mono.empty()
                                .delay(Duration.ofMillis(2500))
                )
                .subscribe(System.out::println);
        Thread.sleep(4000L);

        /**
         * 파라미터로 입력되는 Predicate가 true인 동안 emit한다.
         */
        Flux
                .fromIterable(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90))
                .takeWhile(data -> data < 50)
                .subscribe(System.out::println);
    }
}
