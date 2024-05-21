package com.example.demo.monoflux;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MonoWorldTimeApi {
    public static void main(String[] args) {
        URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Mono.just(
                restTemplate.exchange(worldTimeUri, HttpMethod.GET, new HttpEntity<String>(headers), String.class)
                )
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
