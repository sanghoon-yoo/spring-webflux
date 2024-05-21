package com.example.demo.operator.createsequence;

import reactor.core.publisher.Mono;

import java.util.Optional;

public class JustOrEmpty {
    public static void main(String[] args) {
        Mono
                /**
                 * just()에 null 값을 입력하면 NPE 발생하는 예제
                 */
//                .just(null)
                /**
                 * justOrEmpty()에 null 값을 입력하면 NPE 발생하지 않고, onNext emit 없이 onComplete 만 emit한다
                 */
//                .justOrEmpty(null)
                /**
                 * justOrEmpty()에 Optional.isPresent() 가 true가 아니라면, onNext emit없이 onComplete만 emit 한다
                 */
                .justOrEmpty(Optional.ofNullable(null))
                .log()
                .subscribe(data -> System.out.println(data));
    }
}
