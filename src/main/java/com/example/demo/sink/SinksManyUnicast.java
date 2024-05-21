package com.example.demo.sink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class SinksManyUnicast {
    public static void main(String[] args) {
        //unicast는 하나의 Subscriber에게만 데이터를 emit할 수 있다.
        Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux<Integer> fluxView = unicastSink.asFlux();

        unicastSink.emitNext(1, FAIL_FAST);
        unicastSink.emitNext(2, FAIL_FAST);

        fluxView.subscribe(data -> System.out.println("[unicast] Subscriber1 " + data));
        //구독을 걸어놨기 때문에 3도 출력된다.
        unicastSink.emitNext(3, FAIL_FAST);

        //Exception 발생 : Sinks.many().unicast() sinks only allow a single Subscriber
        //fluxView.subscribe(data -> System.out.println("[unicast] Subscriber2 " + data));
    }
}
