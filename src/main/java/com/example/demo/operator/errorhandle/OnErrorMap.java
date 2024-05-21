package com.example.demo.operator.errorhandle;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Map;


public class OnErrorMap {
    private final static URI WORLD_TIME_URI = UriComponentsBuilder.newInstance().scheme("http")
            .host("worldtimeapi.org")
            .port(80)
            .path("/api/timezone/Asia/Washington")
            .build()
            .encode()
            .toUri();
    public static void main(String[] args) {
        /**
         * UpStream에서 error signal이 전송되면 에러 정보(예외)를 전달 받아 또 다른 타입의 예외로 변환해서 DownStream으로 전송한다.
         * CustomException 으로 예외 던지는 것으로 응용 가능
         */
        /*Flux
                .just(1, 3, 0, 6, 8)
                .filter(num -> num % 3 == 0)
                .doOnNext(System.out::println)
                .map(num -> (num * 2) / num)
                .onErrorMap(error -> new RuntimeException("0으로 나눌 수 없습니다"))
                .subscribe(
                        System.out::println
                        , error -> System.out.println(error)
                );*/

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Mono.fromSupplier(() ->
                        restTemplate.exchange(WORLD_TIME_URI, HttpMethod.GET, new HttpEntity<String>(headers), String.class)
                )
                .onErrorMap(HttpClientErrorException.class, (HttpClientErrorException ex) -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return new TimezoneNotFoundException(ex.getResponseBodyAsString());
                    }
                    return new HttpClientErrorException(ex.getStatusCode());
                })
                .map(response -> {
                    String body = response.getBody();
                    JsonParser jsonParser = JsonParserFactory.getJsonParser();
                    Map<String, Object> stringObjectMap = jsonParser.parseMap(body);
                    return (String)stringObjectMap.get("datetime");
                })
                .subscribe(
                        data -> System.out.println(data),
                        error -> {System.out.println(error);},
                        () -> System.out.println("# emitted onComplete signal")
                );
    }

}
class TimezoneNotFoundException extends Exception {
    public TimezoneNotFoundException(String message) {
        super(message);
    }
}
