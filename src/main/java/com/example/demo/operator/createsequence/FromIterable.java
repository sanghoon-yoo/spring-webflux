package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.Arrays;

public class FromIterable {
    public static void main(String[] args) {
        /**
         * Iterable의 구현 클래스를 파라미터로 입력받아 차례대로 emit한다
         */
        Flux
                .fromIterable(
//                        Arrays.asList("BTC", "ETH", "XRP", "ICX", "EOS", "BCH")
                        Arrays.asList(
                                Tuples.of("BTC", 52_000_000),
                                Tuples.of("ETH", 1_720_000),
                                Tuples.of("XRP", 533),
                                Tuples.of("ICX", 2_080),
                                Tuples.of("EOS", 4_020),
                                Tuples.of("BCH", 558_000)
                        )
                )
                .subscribe(data -> System.out.println("코인명 : " + data.getT1() + " 가격 : " + data.getT2()));

    }
}
