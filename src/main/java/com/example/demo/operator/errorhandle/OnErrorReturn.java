package com.example.demo.operator.errorhandle;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.IllegalFormatException;

public class OnErrorReturn {
    public static void main(String[] args) {
        /**
         * onError signal을 DownStream에 내려보내지 않고, 대체 값을 emit한다
         * try catch에서 따로 처리하는 것과 비슷
         */
        Flux
                .fromArray(new String[]{"a", "b", null, "d"})
                .map(data -> data.toUpperCase())
                .onErrorReturn(IllegalFormatException.class, "Illegal alphavet Name")
                .onErrorReturn(NullPointerException.class, "No alphavet Name")
                .onErrorReturn(Exception.class, "Unknown Error")
                .subscribe(
                        System.out::println
                        /**
                         * error signal이 발생하지 않는다
                         */
                        , error -> System.out.println("errMsg : " + error)
                )
        ;
    }
}
