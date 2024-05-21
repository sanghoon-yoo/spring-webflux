package com.example.demo.operator.createsequence;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface CrytoCurrencyPriceListener{
    <T> void onPrice(List<T> priceList);
    void onComplete();
}

@FunctionalInterface
interface test<T> {
    T data() throws Exception;
}

class CryptoCurrencyPriceEmitter {
    private CrytoCurrencyPriceListener listener;

    public void setListener(CrytoCurrencyPriceListener listener) {
        this.listener = listener;
    }

    public <T> void flowInto(test<List<T>> t) throws Exception {
        listener.onPrice(t.data());
    }

    public void complete() {
        listener.onComplete();
    }
}

public class Create {
    public static int SIZE = 0;
    public static int COUNT = -1;

    public static int start = 1;
    public static int end = 4;

    private static List<Integer> datasource = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    public static void main(String[] args) throws Exception {
        System.out.println("# start");
        Flux
                .create((FluxSink<Integer> emitter) -> {
                    emitter.onRequest(n -> {
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < n; i++) {
                            if (COUNT >= 9) {
                                emitter.complete();
                            } else {
                                COUNT++;
                                emitter.next(datasource.get(COUNT));
                            }
                        }
                    });
                    emitter.onDispose(() -> System.out.println("# clean up"));
                })
                .subscribe(
                        new BaseSubscriber<Integer>() {
                            @Override
                            protected void hookOnSubscribe(Subscription subscription) {
                                request(2);
                            }

                            @Override
                            protected void hookOnNext(Integer value) {
                                SIZE++;
                                System.out.println(value);
                                if (SIZE == 2) {
                                    request(2);
                                    SIZE = 0;
                                }
                            }

                            @Override
                            protected void hookOnComplete() {
                                System.out.println("# onComplete");
                            }
                        }
                );

        System.out.println("# start");
        CryptoCurrencyPriceEmitter priceEmitter = new CryptoCurrencyPriceEmitter();

        Flux
                .create((FluxSink sink) -> {
                    priceEmitter.setListener(new CrytoCurrencyPriceListener() {

                        @Override
                        public <T> void onPrice(List<T> priceList) {
                            /**
                             * 데이터가 들어오면 sink.next로 emit한다
                             */
                            priceList.stream().forEach(price -> {
                                sink.next(price);
                            });
                        }

                        @Override
                        public void onComplete() {
                            sink.complete();
                        }
                    });
                })
                .publishOn(Schedulers.parallel())
                .subscribe(
                        /**
                         * data
                         */
                        System.out::println,
                        /**
                         * error
                         */
                        System.out::println,
                        /**
                         * complete
                         */
                        () -> System.out.println("# onComplete")
                );

        Thread.sleep(3000L);

        /**
         * 데이터 전달
         * 외부에서 emit 가능
         */
        priceEmitter.flowInto(() -> {return Arrays.asList(1, 33, 42634);});

        Thread.sleep(2000L);

        priceEmitter.complete();

        Thread.sleep(100L);


        System.out.println("# start");
        Flux.create(
                        (FluxSink<Integer> emitter) -> {
                            emitter.onRequest(n -> {
                                System.out.println("# requested: " + n);
                                try {
                                    Thread.sleep(500L);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                for (int i = start; i <= end; i++) {
                                    emitter.next(i);
                                }
                                start += 4;
                                end += 4;
                            });
                            emitter.onDispose(() -> {
                                System.out.println("# clean up");
                            });
                            /**
                             * 2개씩 요청했는데 4개를 emit하므로 DROP한다
                             */
                        }, FluxSink.OverflowStrategy.DROP
                ).subscribeOn(Schedulers.boundedElastic())
                /**
                 * 별도의 스레드에서 2개씩 emit
                 */
                .publishOn(Schedulers.parallel(), 2)
                .subscribe(System.out::println);
        Thread.sleep(5000L);
    }
}
