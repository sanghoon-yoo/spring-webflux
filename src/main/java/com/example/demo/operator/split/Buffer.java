package com.example.demo.operator.split;

import reactor.core.publisher.Flux;

public class Buffer {
    public static void main(String[] args) {
        /**
         * buffer가 채워주면 버퍼를 비운다
         * 버퍼에서 비워진 데이터는 List형태로 한번에 DownStream에 emit된다
         */
        Flux
                .range(1, 95)
                .buffer(10)
                .subscribe(System.out::println);
    }
}
