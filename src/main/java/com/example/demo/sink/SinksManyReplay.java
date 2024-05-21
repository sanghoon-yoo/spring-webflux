package com.example.demo.sink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class SinksManyReplay {
    public static void main(String[] args) {
        //replay limit은 구독발생 시점 특정 개수의 최신데이터만 전달하고, 구독 이후의 데이터도 전달한다.
//        Sinks.Many<Integer> replaySink = Sinks.many().replay().limit(2);

        //replay all은 구독 시점과 상관 없이 emit된 모든 데이터를 전달한다.
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        Flux<Integer> fluxView = replaySink.asFlux();

        replaySink.emitNext(1, FAIL_FAST);
        replaySink.emitNext(2, FAIL_FAST);
        replaySink.emitNext(3, FAIL_FAST);

        fluxView.subscribe(data -> System.out.println("[replay] Subscriber1 " + data));

        replaySink.emitNext(4, FAIL_FAST);

        fluxView.subscribe(data -> System.out.println("[replay] Subscriber2 " + data));
    }
}
