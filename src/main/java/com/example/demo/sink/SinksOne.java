package com.example.demo.sink;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class SinksOne {
    public static void main(String[] args) {
        //emit된 데이터 중에서 단 하나의 데이터만 Subscriber에게 전달한다. 나머지 데이터는 드롭된다.
        Sinks.One<String> sinkOne = Sinks.one();
        Mono<String> mono = sinkOne.asMono();

        sinkOne.emitValue("Hi Reactor1", FAIL_FAST);
        //두번째 emit은 드롭됨
        sinkOne.emitValue("Hi Reactor2", FAIL_FAST);

        mono.subscribe(data -> System.out.println("[sinkOne] Subscriber1 " + data));
        mono.subscribe(data -> System.out.println("[sinkOne] Subscriber2 " + data));
    }
}
