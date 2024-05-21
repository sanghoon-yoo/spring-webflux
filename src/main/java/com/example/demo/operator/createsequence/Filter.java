package com.example.demo.operator.createsequence;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CoronaVaccineService {
    private static Map<Filter.CoronaVaccine, Tuple2<Filter.CoronaVaccine, Integer>> sampleMap = getSampleDataMap();

    public static Mono<Boolean> isGreaterThan(Filter.CoronaVaccine vaccine, int amount) {
        return Mono
                .just(sampleMap.get(vaccine).getT2() > amount)
                /**
                 * 별도 스레드에서 동작
                 */
                .publishOn(Schedulers.parallel());
    }
    private static Map<Filter.CoronaVaccine, Tuple2<Filter.CoronaVaccine, Integer>> getSampleDataMap() {
        HashMap<Filter.CoronaVaccine, Tuple2<Filter.CoronaVaccine, Integer>> map = new HashMap<>();
        map.put(Filter.CoronaVaccine.Pfizer, Tuples.of(Filter.CoronaVaccine.Pfizer, 565));
        map.put(Filter.CoronaVaccine.Astrazenaca, Tuples.of(Filter.CoronaVaccine.Astrazenaca, 36_094));
        map.put(Filter.CoronaVaccine.Janssen, Tuples.of(Filter.CoronaVaccine.Janssen, 17_425));
        map.put(Filter.CoronaVaccine.Moderna, Tuples.of(Filter.CoronaVaccine.Moderna, 12_405_209));
        map.put(Filter.CoronaVaccine.Novavax, Tuples.of(Filter.CoronaVaccine.Novavax, 116_237_182));
        return map;
    }
}

public class Filter {
    public static void main(String[] args) throws InterruptedException {
        Flux
                .range(1, 20)
                .filter(num -> num % 2 == 0)
                .subscribe(System.out::println);

        Flux
                .fromIterable(Arrays.asList(
                        Tuples.of(2010, 565L),
                        Tuples.of(2011, 36_094L),
                        Tuples.of(2012, 17_425L),
                        Tuples.of(2013, 1_405_209L),
                        Tuples.of(2014, 1_237_182L),
                        Tuples.of(2015, 557_603L),
                        Tuples.of(2016, 1_111_811L),
                        Tuples.of(2017, 22_483_583L),
                        Tuples.of(2018, 19_521_543L),
                        Tuples.of(2019, 15_761_568L),
                        Tuples.of(2020, 22_439_002L),
                        Tuples.of(2021, 63_364_000L)
                ))
                .filter(tuple -> tuple.getT2() > 10_000_000)
                .subscribe(filtered -> System.out.println(filtered.getT1() + " : " + filtered.getT2()));

        /**
         *  filterWhen을 사용해서 병렬 실행환경에서 필터링한다.
         */
        Flux
                .fromIterable(CoronaVaccine.toList())
                .filterWhen(vaccine -> CoronaVaccineService.isGreaterThan(vaccine, 3_000_000))
                .subscribe(System.out::println);
        Thread.sleep(3000);
    }

    enum CoronaVaccine{
        Pfizer,
        Astrazenaca,
        Moderna,
        Janssen,
        Novavax;

        public static List<CoronaVaccine> toList() {
            return Arrays.asList(
                    CoronaVaccine.Pfizer,
                    CoronaVaccine.Astrazenaca,
                    CoronaVaccine.Moderna,
                    CoronaVaccine.Janssen,
                    CoronaVaccine.Novavax
            );
        }
    }
}
