package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

public class Range {
    public static void main(String[] args) {
        Flux
                /**
                 * 첫번째 파라미터부터 두번째 파라미터만큼 1씩 증가한 연속된 정수를 emit한다
                 */
                .range(5, 10)
                .subscribe(System.out::println);

        List<String> coinNames = Arrays.asList("BTC", "ETH", "XRP", "ICX", "EOS", "BCH");
        Flux
                /**
                 * 명령형 방식의 for문을 대체하는 예제
                 */
                .range(0, coinNames.size())
                .subscribe(index -> System.out.println(coinNames.get(index)));

        List<Tuple2<Integer, Long>> btcTopPricesPerYear = Arrays.asList(
                Tuples.of(2010, 565L),
                Tuples.of(2011, 36_094L),
                Tuples.of(2012, 17_425L),
                Tuples.of(2013, 1_405_209L),
                Tuples.of(2014, 1_237_182L),
                Tuples.of(2015, 557_603L),
                Tuples.of(2016, 1_111_811L),
                Tuples.of(2017, 22_483_583L),
                Tuples.of(2018, 19_521_543L),
                Tuples.of(2019, 15_761_568L),
                Tuples.of(2020, 22_439_002L),
                Tuples.of(2021, 63_364_000L)
        );
        Flux
                /**
                 * range()를 사용해서 list의 특정 index에 해당하는 데이터를 조회하는 예제
                 */
                .range(7, 5)
                .map(index -> btcTopPricesPerYear.get(index))
                .subscribe(tuple -> System.out.println(tuple.getT1() + "'s " + tuple.getT2()));
    }
}
