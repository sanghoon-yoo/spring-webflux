package com.example.demo.monoflux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxExam {
    public static void main(String[] args) {
        Flux.just(6, 9, 13)
                .map(num -> num % 2)
                .subscribe(remainder -> System.out.println("# remainder: " + remainder));

        Flux.fromArray(new Integer[]{3, 6, 7, 9})
                .filter(num -> num > 6)
                .map(num -> num * 2)
                .subscribe(multiply -> System.out.println("# multiply:" + multiply));

        Flux<Object> flux =
                Mono.justOrEmpty(null)
                        .concatWith(Mono.justOrEmpty("Jobs"));
        flux.subscribe(data -> System.out.println("# result:" + data));

        Flux.concat(
                        Flux.just("Venus"),
                        Flux.just("Earth"),
                        Flux.just("Mars")
                )
                .collectList()
                .subscribe(planetList -> System.out.println("# Solar System: " + planetList));

    }
}
