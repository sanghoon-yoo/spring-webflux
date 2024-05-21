package com.example.demo.context;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class ContextExam {
    public static final String HOST_NAME = "host_name";
    public static final String PATH_URI = "path_uri";
    public static final String httpHeader = "header";
    public static void main(String[] args) {
        /**
         * Context 활용 예제
         * 직교성을 가지는 정보를 표현할 때 주로 사용된다.
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Mono<String> mono =
                postTimeApi("datetime")
                        .contextWrite(Context.of(HOST_NAME, "worldtimeapi.org", PATH_URI, "/api/timezone/Asia/Seoul", httpHeader, httpHeaders));

        mono.subscribe(data -> System.out.println(data));

    }

    private static Mono<String> postTimeApi(String attr) {
        return Mono.zip(Mono.deferContextual(ctx -> Mono.just(ctx.get(httpHeader))), Mono.deferContextual(ctx -> Mono.just(ctx.get(HOST_NAME))), Mono.deferContextual(ctx -> Mono.just(ctx.get(PATH_URI))))
                /**
                 * 각 퍼블리셔의 emit을 직렬화한다.
                 */
                .flatMap(tuple -> Mono.just(
                        new RestTemplate().exchange(UriComponentsBuilder.newInstance().scheme("http")
                                .host(tuple.get(1).toString())
                                .port(80)
                                .path(tuple.get(2).toString())
                                .build()
                                .encode()
                                .toUri(), HttpMethod.GET, new HttpEntity<String>((HttpHeaders)tuple.get(0)), String.class)
                ).map(response -> {
                    String body = response.getBody();
                    JsonParser jsonParser = JsonParserFactory.getJsonParser();
                    Map<String, Object> stringObjectMap = jsonParser.parseMap(body);
                    return (String) stringObjectMap.get(attr);
                }));
    }
}
