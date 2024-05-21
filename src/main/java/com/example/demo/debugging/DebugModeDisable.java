package com.example.demo.debugging;

import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

public class DebugModeDisable {
    public static void main(String[] args) {
        /**
         * Non Debug Mode
         * zero 나누기 Exception이 발생한다.
         */
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .subscribe(
                        data -> System.out.println("# onNext:" + data)
                        , error -> error.printStackTrace()//System.out.println(error)
                );

        /**
         * NPE 발생
         */
        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .map(String::toLowerCase)
                .map(fruits -> fruits.substring(0, fruits.length() - 1))
                .map(fruits::get)
                .subscribe(data -> System.out.println(data)
                        , error -> error.printStackTrace()
                );
    }

    public static Map<String, String> fruits = new HashMap<>();
    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("grape", "포도");
    }
}

