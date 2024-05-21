package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Contxetfeture4 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * inner Sequence 내부에서는 외부 Context에 저장된 데이터를 읽을 수 있다.
         * inner Sequence 내부에서 Context에 저장된 데이터는 inner Sequence 외부에서 읽을 수 없다.
         */
        String key1 = "id";
        Mono.just("Sanghoon")
//                .transformDeferredContextual(((stringMono, contextView) -> contextView.get("job")))
                .flatMap(name -> Mono.deferContextual(ctx ->
                                Mono.just(ctx.get(key1) + ", " + name)
                                        .transformDeferredContextual((mono, innerCtx) ->
                                                mono.map(data -> data + ", " + innerCtx.get("job"))
                                        )
                                        .contextWrite(context -> context.put("job", "Software Engineer"))
                        )
                )
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.put(key1, "itVillage"))
                .subscribe(data -> System.out.println("# onNext: " + data));

        Thread.sleep(100L);
    }
}
