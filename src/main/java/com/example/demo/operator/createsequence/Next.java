package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;

import java.util.Arrays;

public class Next {
    public static void main(String[] args) {
        /**
         * emit된 데이터 중에서 첫번째 데이터만 DownStream으로 emit한다.
         */
        Flux
                .fromIterable(Arrays.asList(1, 2, 3, 4, 5, 5, 5, 6, 7, 8, 9))
                .doOnNext(data -> System.out.println("UpStream에서 emit된 데이터 : " + data))
                .filter(data -> data == 5)
                /**
                 * next()를 주석처리하면 9까지 emit시도한다.
                 */
                .next()
                .subscribe(System.out::println);
    }
}
