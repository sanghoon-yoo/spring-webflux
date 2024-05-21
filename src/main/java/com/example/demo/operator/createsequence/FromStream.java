package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import java.util.Arrays;

public class FromStream {
    public static void main(String[] args) {
        /**
         * fromStream()의 사용 예제
         * Stream을 파라미터로 입력 받아 Stream에 포함된 데이터를 차례대로 emit한다
         */
        Flux
                .fromStream(
                        Arrays.asList("BTC", "ETH", "XRP", "ICX", "EOS", "BCH").stream()
                )
                .filter(coin -> coin.equals("BTC") || coin.equals("ETH"))
                .subscribe(System.out::println);
    }
}
