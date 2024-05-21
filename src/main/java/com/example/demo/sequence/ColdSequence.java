package com.example.demo.sequence;

import reactor.core.publisher.Flux;

import java.util.Arrays;

public class ColdSequence {
    public static void main(String[] args) {
        Flux<String> coldFlux = Flux.fromIterable(Arrays.asList("RED", "YELLOW", "PINK"))
                .map(String::toLowerCase);
        coldFlux.subscribe(color -> System.out.println("# Subcriber1: " + color));
        System.out.println("----------------------------------");
        coldFlux.subscribe(color -> System.out.println("# Subcriber2: " + color));

    }
}
