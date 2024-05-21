package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class Skip {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 파라미터로 입력한 숫자만큼 Upstream에서 emit되는 데이터를 건너 뛴 후, 건넌 뛴 다음 데이터부터 DownStream으로 emit한다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .doOnNext(data -> System.out.println("upstream에서 emit된 데이터 : " + data))
                .take(5)
                .skip(3)
                .subscribe(data -> System.out.println("downstream에 emit된 데이터 : " + data));

        Thread.sleep(5000L);

        /**
         * 파라미터로 입력한 시간만큼 건너뛴다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .take(5)
                .skip(Duration.ofMillis(2500))
                .subscribe(System.out::println);

        Thread.sleep(5000L);

        /**
         * emit된 데이터 중에서 파라미터로 입력된 갯수만큼 가장 마지막에 emit된 데이터부터 건너뛴다.
         */
        Flux
                .fromIterable(Arrays.asList("apple", "orange", "grape"))
                .skipLast(2)
                .subscribe(System.out::println);

        /**
         * 파라미터로 입력되는 Predicate가 true가 될때까지 건너뛴다.
         */
        Flux
                .fromIterable(Arrays.asList("apple", "orange", "grape"))
                .skipUntil(data -> data.equals("orange"))
                .subscribe(System.out::println);

        /**
         * 파라미터로 입력된 Publisher가 onNext 또는 onComplete signal을 발생시킬 때까지 Upstream에서 emit된 데이터를 건너뛴다.
         */
        Flux
                .interval(Duration.ofSeconds(1))
                .skipUntilOther(
                        /**
                         * 아래 Publisher는 onComplete를 발생시킨다.
                         */
                        Mono.empty()
                                .delay(Duration.ofMillis(2500))
                )
                .subscribe(System.out::println);
        Thread.sleep(4000L);

        /**
         * 파라미터로 입력되는 Predicate가 true인 동안 emit되는 데이터를 건너뛴다.
         */
        Flux
                .fromIterable(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90))
                .skipWhile(data -> data < 50)
                .subscribe(System.out::println);
    }
}
