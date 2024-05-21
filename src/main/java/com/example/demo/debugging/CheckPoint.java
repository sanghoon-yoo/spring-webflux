package com.example.demo.debugging;

import reactor.core.publisher.Flux;

public class CheckPoint {
    public static void main(String[] args) {
        /**
         * 에러가 예상되는 assembly 지점에 checkpoint()를 사용해서 에러 발생 지점을 확인할 수 있다.
         * checkpoint()는 에러발생 시, traceback이 추가된다.
         */
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .checkpoint()
                .map(num -> num + 2)
                .checkpoint()
                .subscribe(data -> System.out.println(data)
                        , error -> error.printStackTrace()
                );
    }
}
