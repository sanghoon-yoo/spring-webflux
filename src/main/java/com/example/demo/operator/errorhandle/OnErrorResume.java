package com.example.demo.operator.errorhandle;

import reactor.core.publisher.Flux;

import java.util.NoSuchElementException;

public class OnErrorResume {
    public static void main(String[] args) {
        /**
         * onError signal을 DownStream에 내려보내지 않고, 대체 publisher로 emit한다
         */
        final String keyword = "good";
        getStringFromCache(keyword)
                .onErrorResume(error -> getStringFromDataBase(keyword))
                .subscribe(
                        System.out::println
                        , error -> System.out.println(error)
                );
    }
    private static Flux<String> getStringFromCache(final String keyword) {
        return Flux
                .fromArray(new String[]{"design", "pattern", "is", "pretty"})
                .filter(data -> data.contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchElementException("No such word")));
    }

    private static Flux<String> getStringFromDataBase(final String keyword) {
        return Flux
                .fromArray(new String[]{"design", "pattern", "is", "pretty", "good"})
                .filter(data -> data.contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchElementException("No such word")));
    }
}
