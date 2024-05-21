package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ContextIntro {
    public static void main(String[] args) throws InterruptedException {
        /**
         * contextWrite()으로 Context에 값을 쓸 수 있고, ContextView.get()을 통해서 Context에 저장된 값을 read할 수 있다.
         * ContextView는 deferContextual() 또는 transformDeferredContextual()을 통해 제공된다.
         */
        String key = "message";
        Mono<String> mono = Mono.deferContextual(ctx ->
                        Mono.just("Hello" + " " + ctx.get(key)).doOnNext(data -> System.out.println(Thread.currentThread().getName() + " " + data))
                )
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                /**
                 * 퍼블리셔와 다운 스트림의 스레드가 다르지만, ContextView의 값은 ThreadLocal처럼 접근 가능하다.
                 */
                .transformDeferredContextual((mono2, ctx) -> mono2.map(data -> data + " " + ctx.get(key)))
                /**
                 * 퍼블리셔의 전역 오브젝트(컨텍스트)를 세팅.
                 */
                .contextWrite(context -> context.put(key, "Reactor"));

        mono.subscribe(data -> System.out.println(Thread.currentThread().getName() + " " + "# onNext: " + data));
        Thread.sleep(100L);
    }
}
