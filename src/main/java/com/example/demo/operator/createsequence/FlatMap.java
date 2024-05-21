package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;

public class FlatMap {
    public static void main(String[] args) throws InterruptedException {
        /**
         * FlatMap Operator는 SpringWebFlux에서 빈번하게 사용 됨
         */
        Flux
                .just("Good", "Bad")
                .flatMap(feeling ->
                        Flux
                                .just("Morning", "Afternoon", "Evening")
                                .map(time -> feeling + " " + time)
                )
                .subscribe(System.out::println);

        Flux
                .just(3)
                .flatMap(dan -> Flux.range(1, 9).map(n -> dan + " * " + n + " = " + dan * n))
                .subscribe(System.out::println);

        /**
         * 들어오는 데이터들이 Publisher로 감싸져 있다면, 감싸져 있는 Publisher들을 벗겨내고 하나의 Publisher로 평면화 한다.
         */
        Flux
                .just("Hello", "World")
                .map(word -> Mono.just(word))
                .flatMap(word -> word)
                .subscribe(System.out::println);

        /**
         * 비동기적(병렬)으로 동작할 경우, emit되는 순서를 보장하지 않는다.
         */
        Flux
                .range(2, 8)
                .flatMap(dan -> Flux
                        .range(1, 9)
                        .publishOn(Schedulers.parallel())
                        .map(n -> dan + " * " + n + " = " + dan * n))
                .subscribe(System.out::println);
        Thread.sleep(200L);

        /**
         * flatMapIterable 기본 개념 예제
         * Iterable로 emit 된 데이터를 순차적으로 평면화 한다.
         * 유형별 코로나 백신 List를 평탄화 하는 예제
         */
        Flux
                .just(
                        Arrays.asList(
                                Tuples.of(Filter.CoronaVaccine.Astrazenaca, 3_000_000),
                                Tuples.of(Filter.CoronaVaccine.Janssen, 2_000_000)
                        ),
                        Arrays.asList(
                                Tuples.of(Filter.CoronaVaccine.Pfizer, 1_000_000),
                                Tuples.of(Filter.CoronaVaccine.Moderna, 4_000_000)
                        ),
                        Arrays.asList(
                                Tuples.of(Filter.CoronaVaccine.Novavax, 2_500_000)
                        )
                )
                .flatMapIterable(data -> data)
                .subscribe(System.out::println);

        /**
         * flatMapMany 활용 예제
         * Mono에서 emit된 데이터를 Flux로 변환한다.
         */
        //Flux를 Mono로 바꾸면 컴파일 에러 발생!!
        Flux
                .just(Tuples.of(500, 1000))
                .flatMap(data -> calculate(data))
                .subscribe(System.out::println);
        //Flux를 Mono로 바꿔도 컴파일 에러 발생안함
        Mono
                .just(Tuples.of(500, 1000))
                .flatMapMany(data -> calculate(data))
                .subscribe(System.out::println);

        /**
         * flatMapSequential 기념 개념 예제
         * 비동기적(병렬)로 동작할 경우에도 emit되는 순서를 보장한다.
         */
        Flux
                .range(2, 8)
                .flatMapSequential(dan -> Flux
                        .range(1, 9)
                        .publishOn(Schedulers.parallel())
                        .map(n -> dan + " * " + n + " = " + dan * n))

                .subscribe(System.out::println);

        Thread.sleep(200L);
    }

    private static Flux<Integer> calculate(Tuple2<Integer,Integer> datas) {
        return Flux
                .fromIterable(Arrays.asList(1, 2, 3, 4, 5))
                .map(num -> datas.getT1() * num + datas.getT2());
    }
}
