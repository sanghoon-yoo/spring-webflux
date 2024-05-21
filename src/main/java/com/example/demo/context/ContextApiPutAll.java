package com.example.demo.context;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextApiPutAll {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Context API 예제 코드
         * putAll(ContextView) API 사용
         * 세팅된 Context값을 병합할 때 사용한다.
         */
        String key1 = "id";
        String key2 = "name";
        String key3 = "country";

        Mono.deferContextual(ctx ->
                        Mono.just("ID: " + " " + ctx.get(key1) + ", " + "Name: " + ctx.get(key2) + ", " + "Country: " + ctx.get(key3))
                )
                .publishOn(Schedulers.parallel())
                .contextWrite(context -> context.putAll(Context.of(key2, "Sanghoon", key3, "Korea").readOnly()))
                /**
                 * Context는 DownStream에서 UpStream으로 전파된다.
                 */
                .contextWrite(context -> context.put(key1, "itVillage"))
                .subscribe(data -> System.out.println("# onNext: " + data));

        Thread.sleep(100L);
    }
}
