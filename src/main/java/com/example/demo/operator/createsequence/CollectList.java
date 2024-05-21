package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;

public class CollectList {
    public static void main(String[] args) {
        /**
         * emit된 여러개의 데이터를 List 타입으로 DownStream에 emit한다.
         */
        Flux
                .just("a", "b", "c")
                .map(alphabet -> alphabet.toUpperCase())
                .collectList()
                .subscribe(data -> System.out.println(String.join("", data)));
    }
}
