package com.example.demo.operator.errorhandle;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Error {
    public static void main(String[] args) {
        /**
         * error operator 기본 개념예제
         * 명시적으로 error signal을 발생시켜야 하는 경우
         * throw 와 비슷한 기능
         */
        Flux
                .range(1, 5)
                .flatMap(num -> {
                    if ((num * 2) % 3 == 0) {
                        return Mono.error(new IllegalStateException("Not allowed multiplication of 3"));
                    }
                    return Mono.just(num * 2);
                })
                .subscribe(
                        data -> System.out.println(data)
                        , error -> System.out.println("errMsg : " + error)
                );

    }
}
