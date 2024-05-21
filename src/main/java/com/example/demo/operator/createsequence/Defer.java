package com.example.demo.operator.createsequence;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

public class Defer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 실제로 구독이 발생하는 시점에 데이터를 emit하는 예제
         * 출력되는 시간 값이 다르다. 데이터가 emit되는 시점이 다르기 때문
         */
        System.out.println("# starting");

        Mono<LocalDateTime> justMono = Mono.just(LocalDateTime.now());
        Mono<LocalDateTime> deferMono = Mono.defer(() -> Mono.just(LocalDateTime.now()));

        Thread.sleep(2000);

        justMono.subscribe(data -> System.out.println("just1 : " + data));
        deferMono.subscribe(data -> System.out.println("defer1 : " + data));

        Thread.sleep(2000);

        justMono.subscribe(data -> System.out.println("just2 : " + data));
        deferMono.subscribe(data -> System.out.println("defer2 : " + data));

        /**
         * switchIfEmpty()에 파라미터로 입력되는 Sequence는 업스트림에서 emit되는 데이터가 없을 경우 다운스트림에 emit한다.
         * 하지만 파라미터로 입력된 sayDefault()는 switchIfEmpty()가 선언된 시점에 이미 호출이 되기때문에
         * 다운스트림에 데이터를 emit 하지는 않지만 구독을 하지 않았음에도 불필요한 메서드 호출이 발생한다
         */
        System.out.println("# Start");
        Mono
                /**
                 * null을 emit하면 Hi가 출력된다
                 */
                .justOrEmpty("Hello")
                /**
                 * 2초 뒤에 emit
                 */
                .delayElement(Duration.ofSeconds(2))
                /**
                 * 업스트림에서 Hello를 emit했기 때문에 Hi는 출력되지 않는다.
                 */
                .switchIfEmpty(
                        sayDefault("Hi")
                        /**
                         * defer로 감싸게 되면 구독이 발생하지 않았으므로 sayDefault는 emit되지 않는다
                         */
//                        Mono.defer(() -> sayDefault("Hi"))
                )
                .subscribe(System.out::println);

        Thread.sleep(2500);
    }

    private static <T> Mono<T> sayDefault(T data) {
        System.out.println("call sayDefault");
        return Mono.just(data);
    }
}

