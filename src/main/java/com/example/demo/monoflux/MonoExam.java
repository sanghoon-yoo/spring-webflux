package com.example.demo.monoflux;

import reactor.core.publisher.Mono;

public class MonoExam {
    public static void main(String[] args) {
        Mono.just("hi")//.empty()
                .subscribe(
                        data -> System.out.println("# emitted data: " + data),
                        error -> {
                        },
                        () -> System.out.println("# emitted onComplete signal")
                );
    }
}
