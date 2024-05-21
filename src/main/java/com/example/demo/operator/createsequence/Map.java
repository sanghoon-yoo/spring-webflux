package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;

import java.util.Arrays;

public class Map {
    public static void main(String[] args) {
        Flux
                .just("Green-color", "Yellow-color", "Blue-color")
                .map(color -> color.replace("color", "색깔"))
                .subscribe(System.out::println);

        Flux
                .fromIterable(Arrays.asList(4, 9, 16, 25, 36))
                .map(data -> calSqrt(data))
                .subscribe(System.out::println);
    }

    private static double calSqrt(int num) {
        return Math.sqrt(num);
    }
}
