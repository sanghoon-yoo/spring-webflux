package com.example.demo.operator.split;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class BufferTimeOut {
    public static void main(String[] args) {
        /**
         * maxTime에 도달하면 버퍼를 비운다
         * maxTime에 도달하기 전에 maxSize 만큼의 데이터가 버퍼에 채워지면 maxTime 까지 기다리지않고 버퍼를 비운다
         */
        Flux
                .range(1, 20)
                .map(num -> {
                    try {
                        if (num < 10) {
                            Thread.sleep(100L);
                        } else {
                            Thread.sleep(300L);
                        }
                    } catch (InterruptedException e) {}
                    return num;
                })
                .bufferTimeout(3, Duration.ofMillis(400L))
                .subscribe(System.out::println);

    }
}
