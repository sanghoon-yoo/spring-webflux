package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

public class Concat {
    public static void main(String[] args) {
        /**
         * 파라미터로 입력된 Publisher Sequence들을 연결해서 차례대로 emit한다.
         */
        Flux
                .concat(Flux.just(1, 2, 3), Flux.just(4, 5))
                .subscribe(System.out::println);

        List<Flux<Integer>> source = List.of(Flux.just(1, 2, 3), Flux.just(4, 5));
        Flux
                .concat(source)
                .subscribe(System.out::println);

        /**
         * 유형별 코로나 백신 List를 concat하는 예제
         */
        Flux
                .concat(
                        Flux.fromIterable(
                                Arrays.asList(
                                        Tuples.of(Filter.CoronaVaccine.Astrazenaca, 3_000_000),
                                        Tuples.of(Filter.CoronaVaccine.Janssen, 2_000_000)
                                )
                        ),
                        Flux.fromIterable(
                                Arrays.asList(
                                        Tuples.of(Filter.CoronaVaccine.Pfizer, 1_000_000),
                                        Tuples.of(Filter.CoronaVaccine.Moderna, 4_000_000)
                                )
                        ),
                        Flux.fromIterable(
                                Arrays.asList(
                                        Tuples.of(Filter.CoronaVaccine.Novavax, 2_500_000)
                                )
                        )
                )
                .subscribe(System.out::println);
    }
}
