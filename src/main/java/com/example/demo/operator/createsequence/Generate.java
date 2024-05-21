package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Generate {
    public static void main(String[] args) {
        /**
         * generate 개념 이해 예제
         * Callable(함수형 인터페이스) : 초기 상태 값 또는 객체를 제공한다. (State Supplier)
         * BiFunction<S, T, S>(함수형 인터페이스) : SynchronousSink 와 현재 상태(state)를 사용하여 single signal을 생성한다.
         * Consumer(함수형 인터페이스) : Generator 종료 또는 Subscriber의 구독 취소 시, 호출 되어 후처리 작업을 한다. (State Consumer)
         */
        Flux
                .generate(
                        /**
                         * 초기 상태 값을 0으로 세팅
                         */
                        () -> 0,
                        /**
                         *  초기 상태 값을 state로 전달한다.
                         */
                        (state, sink) -> {
                            /**
                             * 초기값으로 전달받은 0을 emit
                             */
                            sink.next(state);
                            if (state == 10)
                            /**
                             * 조건을 만족하므로 Sequence 종료
                             */
                                sink.complete();
                            /**
                             * state를 증가시킨 후 다시 BiFunction state로 전달
                             */
                            return ++state;
                        })
                .subscribe(System.out::println);

        /**
         * 구구단 2단
         */
        Flux
                .generate(
                        () -> Tuples.of(2, 1),
                        (state, sink) -> {
                            sink.next(state.getT1() + " * " + state.getT2() + " = " + state.getT1() * state.getT2());
                            if (state.getT2() == 9)
                                sink.complete();
                            return Tuples.of(state.getT1(), state.getT2() + 1);
                        },
                        state -> System.out.println("# 구구단 " + state.getT1() + "단 종료!")
                )
                .subscribe(System.out::println);

        /**
         * 2016년도 이후의 해당 연도 BTC 최고가 금액을 출력하는 예제
         */
        HashMap<Integer, Tuple2<Integer, Long>> map = new HashMap<>();
        map.put(2010, Tuples.of(2010, 565L));
        map.put(2011, Tuples.of(2011, 36_094L));
        map.put(2012, Tuples.of(2012, 17_425L));
        map.put(2013, Tuples.of(2013, 1_405_209L));
        map.put(2014, Tuples.of(2014, 1_237_182L));
        map.put(2015, Tuples.of(2015, 557_603L));
        map.put(2016, Tuples.of(2016, 1_111_811L));
        map.put(2017, Tuples.of(2017, 22_483_583L));
        map.put(2018, Tuples.of(2018, 19_521_543L));
        map.put(2019, Tuples.of(2019, 15_761_568L));
        map.put(2020, Tuples.of(2020, 22_439_002L));
        map.put(2021, Tuples.of(2021, 63_364_000L));

        Flux
                .generate(
                        () -> 2017,
                        (state, sink) -> {
                            if (state > 2021) {
                                sink.complete();
                            } else {
                                sink.next(map.get(state));
                            }

                            return ++state;
                        }
                )
                .subscribe(System.out::println);
    }
}
