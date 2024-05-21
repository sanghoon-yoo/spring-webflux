package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Zip {
    /**
     * 시간 대 별 확진자 데이터
     */
    public static final List<Tuple2<Integer, Integer>> seoulInfected =
            Arrays.asList(
                    Tuples.of(1, 0), Tuples.of(2, 0), Tuples.of(3, 0), Tuples.of(4, 0), Tuples.of(5, 0), Tuples.of(6, 0),
                    Tuples.of(7, 0), Tuples.of(8, 0), Tuples.of(9, 0), Tuples.of(10, 20), Tuples.of(11, 23), Tuples.of(12, 33),
                    Tuples.of(13, 10), Tuples.of(14, 15), Tuples.of(15, 20), Tuples.of(16, 30), Tuples.of(17, 10), Tuples.of(18, 11),
                    Tuples.of(19, 13), Tuples.of(20, 8), Tuples.of(21, 14), Tuples.of(22, 4), Tuples.of(23, 7), Tuples.of(24, 2)
                    );
    public static final List<Tuple2<Integer, Integer>> incheonInfected =
            Arrays.asList(
                    Tuples.of(1, 0), Tuples.of(2, 0), Tuples.of(3, 0), Tuples.of(4, 0), Tuples.of(5, 0), Tuples.of(6, 0),
                    Tuples.of(7, 0), Tuples.of(8, 0), Tuples.of(9, 0), Tuples.of(10, 3), Tuples.of(11, 5), Tuples.of(12, 2),
                    Tuples.of(13, 10), Tuples.of(14, 5), Tuples.of(15, 6), Tuples.of(16, 7), Tuples.of(17, 2), Tuples.of(18, 5),
                    Tuples.of(19, 2), Tuples.of(20, 0), Tuples.of(21, 2), Tuples.of(22, 0), Tuples.of(23, 2), Tuples.of(24, 1)
                    );
    public static final List<Tuple2<Integer, Integer>> suwonInfected =
            Arrays.asList(
                    Tuples.of(1, 0), Tuples.of(2, 0), Tuples.of(3, 0), Tuples.of(4, 0), Tuples.of(5, 0), Tuples.of(6, 0),
                    Tuples.of(7, 0), Tuples.of(8, 0), Tuples.of(9, 0), Tuples.of(10, 2), Tuples.of(11, 1), Tuples.of(12, 0),
                    Tuples.of(13, 3), Tuples.of(14, 2), Tuples.of(15, 3), Tuples.of(16, 6), Tuples.of(17, 3), Tuples.of(18, 1),
                    Tuples.of(19, 1), Tuples.of(20, 0), Tuples.of(21, 0), Tuples.of(22, 1), Tuples.of(23, 0), Tuples.of(24, 0)
                    );

    public static void main(String[] args) throws InterruptedException {
        /**
         * zip 기본개념예제
         * 파라미터로 입력된 Publisher Sequence에서 emit된 데이터를 결합한다.
         */
        Flux
                .zip(
                        /**
                         * 서로 emit되는 시점이 달라도 순서대로 묶어준다.
                         */
                        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                        Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)),
                        /**
                         * Combinator 함수형 인터페이스를 통해 zip내부에서 연산하여 반환한다.
                         */
                        (n1, n2) -> n1 * n2
                )
                .subscribe(System.out::println);

        Thread.sleep(2500L);

        /**
         * zip 활용 예제
         * 시간별 코로나 확진자 수를 집계하는 예제
         */
        getInfectedPersonsPerHour(10, 21)
                .subscribe(tuples -> {
                    Tuple3<Tuple2, Tuple2, Tuple2> coronaData = (Tuple3) tuples;
                    int sum = (int) coronaData.getT1().getT2() + (int) coronaData.getT2().getT2() + (int) coronaData.getT3().getT2();
                    System.out.println(coronaData.getT1().getT1() + "시:" + sum + "명");
                });

    }

    private static Flux getInfectedPersonsPerHour(int start, int end) {
        return Flux.zip(
                Flux.fromIterable(seoulInfected)
                        .filter(tuple -> tuple.getT1() >= start && tuple.getT1() <= end),
                Flux.fromIterable(incheonInfected)
                        .filter(tuple -> tuple.getT1() >= start && tuple.getT1() <= end),
                Flux.fromIterable(suwonInfected)
                        .filter(tuple -> tuple.getT1() >= start && tuple.getT1() <= end)
        );
    }
}
