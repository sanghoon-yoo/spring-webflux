package com.example.demo;

import com.example.demo.operator.createsequence.Map;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class HelloReactor {
    public static void main(String[] args) {
        Mono.just("Hello Reactor")
                .subscribe(message -> System.out.println(message));
    }
}
