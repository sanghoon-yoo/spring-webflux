package com.example.demo.scheduler;

import org.springframework.http.converter.json.GsonBuilderUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulerMethod {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Schedulers.immediate() : 별도의 스레드를 추가 할당하지 않고, 현재 스레드에서 실행된다.
         * Schedulers.single() : 하나의 스레드를 재사용한다. 저지연(low latency) 일회성 실행에 최적화 되어 있다.
         * Schedulers.boundedElastic() : 스레드 풀을 생성하여 생성된 스레드를 재사용한다. 생성할 수 있는 스레드 수에 제한이 있다.(Default, CPU Core x 10)
         *  - 긴 실행 시간을 가질 수 있는 Blocking I/O 작업에 최적화 되어 있다.
         * Schedulers.parallel() : 여러개의 스레드를 할당해서 동시에 작업을 수행할 수 있다. Non-Blocking I/O 작업에 최적화 되어 있다.
         * Schedulers.fromExecutorService() : 기존의 ExecutorService를 사용하여 스레드를 생성한다. 의미있는 식별자를 제공하기 때문에 Metric에서 주로 사용한다.
         * Schedulers.newXXXX() : 다양한 유형의 새로운 Scheduler를 생성할 수 있다. ex) newSingle(), newParallel(), newboundedElastic()
         *  - Scheduler의 이름을 직접 지정할 수 있다.
         */
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> System.out.println(Thread.currentThread().getName() + " filter:" + data))
                .publishOn(Schedulers.parallel())
                .map(data -> data * 10)
                .doOnNext(data -> System.out.println(Thread.currentThread().getName() + " map:" + data))
                .subscribe(data -> System.out.println(Thread.currentThread().getName() + " onNext: " + data));
        Thread.sleep(200L);

    }
}
