package com.example.demo.sink;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class SinksManyMulicast {
    public static void main(String[] args) {
        //munitcast는 하나 이상의 Subscriber에게 데이터를 emit할 수 있다.
        Sinks.Many<Integer> multicastSink = Sinks.many().multicast().onBackpressureBuffer();
        Flux<Integer>  fluxView = multicastSink.asFlux();

        multicastSink.emitNext(1, FAIL_FAST);
        multicastSink.emitNext(2, FAIL_FAST);

        fluxView.subscribe(data -> System.out.println("[multicast] Subscriber1 " + data));
        //여기서는 1과 2를 구독받지 못한다. 멀티캐스트에서는 핫시퀀스 동작 방식 중에서 '첫번째 구독이 발생하는 시점에서 데이터 emit이 시작'되는 웜업방식으로 동작한다.
        //네트워크의 멀티캐스트와 마찬가지 원리. 불특정 다수의 노드에 데이터를 전송하지만 뒤늦게 네트워크에 참여하여 데이터를 못받은 노드가 있다고해서 다시 전송해주지 않는다.
        fluxView.subscribe(data -> System.out.println("[multicast] Subscriber2 " + data));

        //구독을 걸어놨기 때문에 3도 출력된다.
        multicastSink.emitNext(3, FAIL_FAST);
    }
}
