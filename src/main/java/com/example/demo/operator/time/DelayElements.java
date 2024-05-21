package com.example.demo.operator.time;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class DelayElements {
    public static void main(String[] args) throws InterruptedException {
        /**
         * DelayElements 개념예제
         * upStream에서의 데이터 emit을 파라미터로 입력한 시간만큼 지연 시킨다.
         * delayElements를 거친 데이터는 별도의 Thread(parallel)에서 실행된다.
         */
        Flux
                .range(1, 10)
//                .delayElements(Duration.ofMillis(500))
                .doOnNext(num -> System.out.println(num))
                .delayElements(Duration.ofMillis(1000))
                .subscribe(data -> System.out.println(data));
        Thread.sleep(10000);
    }
}
