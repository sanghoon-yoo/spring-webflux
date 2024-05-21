package com.example.demo.backpressure;

import lombok.SneakyThrows;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureDoAvailableMore {
    public static int count = 0;
    public static void main(String[] args) {
        //Subscriber가 처리 가능한 만큼의 request 개수를 조절하는 Backpressure 예제
        Flux.range(1, 5)
                .doOnNext(data -> System.out.println("# doOnNext: " + data))
                .doOnRequest(data -> System.out.println("# doOnRequest: " + data))
                .subscribe(new BaseSubscriber<Integer>() {
                    //구독 시점의 hook
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @SneakyThrows
                    @Override
                    protected void hookOnNext(Integer value) {
                        count++;
                        System.out.println("# hookOnNext: " + value);
                        if (count == 2) {
                            Thread.sleep(2000L);
                            /**
                             * request를 높여서 더 많이 emit한다고 해도 Thread.sleep이 걸리기 직전까지의 emit를 받은 후 다시 request하므로 해당 시점부터 재요청된다.
                             * request를 1로 바꾸면 hookOnNext 요청이 중간에 끊겨서 emit이 중단된다.
                             */
                            request(2);
                            count = 0;
                        }
                    }
                });
    }
}
