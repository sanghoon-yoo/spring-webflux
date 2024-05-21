package com.example.demo.operator.errorhandle;

import reactor.core.publisher.Flux;

public class OnErrorContinue {
    public static void main(String[] args) {
        /**
         * 데이터 emit 중에 error가 발생하면 error는 무시하고 다음 데이터를 emit한다
         * 데이터 흐름이 반대로 동작하기 때문에 권장되는 방식이 아니다
         */
        Flux
                .just(1, 2, 4, 0, 6, 12)
                .map(num -> 12 / num)
                .onErrorContinue((error, num) -> System.out.println("Error : " + error + ", num : " + num))
                .subscribe(
                        System.out::println
                        , error -> System.out.println("Error : " + error)
                );
    }
}
