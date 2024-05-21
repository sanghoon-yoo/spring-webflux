package com.example.demo.operator.errorhandle;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Retry {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 에러가 발생했을 때, 지정된 횟수만큼 원본 Publisher의 Sequence를 다시 구독한다
         */
        final int[] count = {1};
        Flux
                .range(1, 3)
                .delayElements(Duration.ofSeconds(1))
                .map(num -> {
                    if (num == 3 && count[0] == 1) {
                        count[0]++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return num;
                })
                .timeout(Duration.ofMillis(1500))
                /**
                 * error가 발생했을 때 1회 재시도
                 */
                .retry(1)
                .subscribe(
                        System.out::println
                        , System.out::println
                        , System.out::println
                );
        System.out.println("test");
        Thread.sleep(7000);
    }
}
