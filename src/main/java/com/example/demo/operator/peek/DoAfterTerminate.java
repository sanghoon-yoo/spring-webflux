package com.example.demo.operator.peek;

import reactor.core.publisher.Flux;

public class DoAfterTerminate {
    public static void main(String[] args) {
        /**
         * doOnTerminate()와 doAfterTerminate()의 차이점을 이해하기 위한 예제
         * doOnTerminate()는 upstream operator가 종료할 때 호출된다
         * doAfterTerminate()는 전체 Sequence가 종료할 때 DownStream에서 UpStream으로 전파(Propagation)되면서 호출된다
         */
        Flux
                .just("HI", "HELLO")
                .filter(data -> data.equals("HI"))
                .doOnTerminate(() -> System.out.println("doOnTerminate : filter"))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate : filter"))
                .map(data -> data.toLowerCase())
                .doOnTerminate(() -> System.out.println("doOnTerminate : map"))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate : map"))
                .subscribe(
                        data -> System.out.println(data)
                        , error -> {
                        }
                        , () -> System.out.println("Complete signal")
                );
    }
}
