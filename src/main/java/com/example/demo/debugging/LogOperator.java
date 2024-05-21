package com.example.demo.debugging;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

public class LogOperator {
    public static void main(String[] args) throws InterruptedException {
        /**
         * log() Operator를 사용하면 구독이벤트부터 퍼블리셔의 통지 메시지까지 자세한 흐름이 출력된다.
         */
        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .subscribeOn(Schedulers.boundedElastic())
                .log("Fruit.Source")
                .publishOn(Schedulers.parallel())
                .map(String::toLowerCase)
                .log("Fruit.Lower")
                .map(fruits -> fruits.substring(0, fruits.length() - 1))
                .log("Fruit.Substring")
                .map(fruits::get)
                .log("Fruit.Name")
                .subscribe(data -> System.out.println(data)
                        , error -> {}//error.printStackTrace()
                );

        Thread.sleep(100L);
    }

    public static Map<String, String> fruits = new HashMap<>();
    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("grape", "포도");
    }
}
