package com.example.demo.operator.split;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.math.MathFlux;

public class Window {
    public static void main(String[] args) {
        /**
         * window 기본 개념 예제
         * UpStream에서 emit되는 첫 번째 데이터부터 maxSize의 숫자만큼의 데이터를 포함하는 새로운 Flux로 분할한다
         * 새롭게 생성되는 Flux를 윈도우라고 한다
         * 마지막 윈도우가 포함하는 데이터는 maxSize보다 작거나 같다
         */

        Flux
                .range(1, 11)
                .window(3)
                .flatMap(flux -> {
                    System.out.println("=====================");
                    //return flux;
                    return MathFlux.sumInt(flux);
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println(value);
                        request(2);
                    }
                });
    }
}
