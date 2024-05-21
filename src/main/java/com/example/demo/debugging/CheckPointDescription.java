package com.example.demo.debugging;

import reactor.core.publisher.Flux;

public class CheckPointDescription {
    public static void main(String[] args) {
        /**
         * Description만 달 경우 Assembly trace를 캡처하지 않는다.
         * 두번때 파라미터로 true를 넘기면 Assembly trace도 캡처되어 출력된다.
         */
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .checkpoint("CheckPointDescription Example", true)
                .map(num -> num + 2)
                .checkpoint("CheckPointDescription Example", true)
                .subscribe(data -> System.out.println(data)
                        , error -> error.printStackTrace()
                );
    }
}
