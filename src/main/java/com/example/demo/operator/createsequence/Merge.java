package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Merge {
    public static HashMap<String, Mono<String>> nppMap = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        /**
         * 파라미터로 입력된 Publisher Sequence에서 emit된 데이터를 emit된 시간이 빠른 순서대로 merge한다
         */
        Flux
                .merge(
                        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                        Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L))
                )
                .subscribe(System.out::println);

        Thread.sleep(3500L);

        /**
         * merge를 활용하여 melt down되고 있는 미 동부 원자력 발전소가 복구되는 메시지를 출력하는 예제
         */
        String[] usaStates = {"Ohio", "Michigan", "New Jersey", "Illinois", "New Hampshire", "Virginia", "Vermont", "North Carolina", "Ontario", "Georgia"};
        nppMap.put("Ontario", Mono.just("Ontario Done").delayElement(Duration.ofMillis(1500L)));
        nppMap.put("Vermont", Mono.just("Vermont Done").delayElement(Duration.ofMillis(400L)));
        nppMap.put("New Hampshire", Mono.just("New Hampshire Done").delayElement(Duration.ofMillis(700L)));
        nppMap.put("New Jersey", Mono.just("New Jersey Done").delayElement(Duration.ofMillis(500L)));
        nppMap.put("Ohio", Mono.just("Ohio Done").delayElement(Duration.ofMillis(1000L)));
        nppMap.put("Michigan", Mono.just("Michigan Done").delayElement(Duration.ofMillis(200L)));
        nppMap.put("Illinois", Mono.just("Illinois Done").delayElement(Duration.ofMillis(300L)));
        nppMap.put("Virginia", Mono.just("Virginia Done").delayElement(Duration.ofMillis(600L)));
        nppMap.put("North Carolina", Mono.just("North Carolina Done").delayElement(Duration.ofMillis(800L)));
        nppMap.put("Georgia", Mono.just("Georgia Done").delayElement(Duration.ofMillis(900L)));

        Flux
                .merge(getMeltDownRecoveryMessage(usaStates))
                .subscribe(System.out::println);

        Thread.sleep(3500L);
    }

    private static List<Mono<String>> getMeltDownRecoveryMessage(String[] usaStates) {
        List<Mono<String>> messages = new ArrayList<>();

        for (String state : usaStates) {
            messages.add(nppMap.get(state));
        }

        return messages;
    }

}
