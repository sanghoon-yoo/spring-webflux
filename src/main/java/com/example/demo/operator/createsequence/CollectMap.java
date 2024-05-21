package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;

public class CollectMap {
    public static void main(String[] args) {
        /**
         *  emit되는 여러개의 데이터를 Map으로 반환하는 예제
         */
        Flux
                .range(2, 9)
                .collectMap(key -> key, value -> value * value)
                .subscribe(map -> System.out.println(map));
    }
}
