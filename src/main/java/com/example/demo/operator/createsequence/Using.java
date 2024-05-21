package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Using {
    public static void main(String[] args) {
        /**
         * using()의 파라미터
         * Callable(함수형 인터페이스) : Resource를 input으로 제공한다. (resource supplier)
         * Function(함수형 인터페이스) : input으로 전달받은 Resource를 새로 생성한 퍼블리셔로 emit한다. (source supplier)
         * Consumer(함수형 인터페이스) : 사용이 끝난 Resource를 해제한다. (resource cleanup)
         */
        Mono
                .using(() -> "Resource",
                        resource -> Mono.just(resource),
                        resource -> System.out.println("cleanup: " + resource))
                .subscribe(System.out::println);

        /**
         * using()을 사용하기 적절한 예제
         * 파일 읽기 및 닫기를 한번에 처리한다
         */
        Path path = Paths.get("C:\\Users\\sanghoon.yoo\\Downloads\\테스트 정보.txt");
        Flux
                .using(() -> Files.lines(path),
                        stream -> Flux.fromStream(stream),
                        Stream::close
                )
                .subscribe(System.out::println);
    }
}
