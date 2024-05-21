package com.example.demo.stepverifier;

import com.example.demo.BackpressureExample;
import com.example.demo.RecordExample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SampleCode2 {
    @Test
    @DisplayName("Backpressure Error 전략 검증1")
    public void generateNumberTest() {
        StepVerifier
                /**
                 * Backpressure 전략에 따라 Exception이 발생하는 예제
                 * request데이터 개수보다 많은 데이터가 emit되어 OverflowException 발생
                 * OverFlowException이 발생하게 된 데이터는 discard 된다
                 * 나머지 emit된 데이터들은 Hooks.onNextDropped()에 의해 drop된다
                 */
                .create(BackpressureExample.generateNumberByErrorStrategy(), 1L)
                /**
                 * emit된 데이터를 소비한다
                 */
                .thenConsumeWhile(num -> num >= 1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Backpressure Error 전략 검증2")
    public void generateNumberTest2() {
        StepVerifier
                .create(BackpressureExample.generateNumberByErrorStrategy(), 1L)
                .thenConsumeWhile(num -> num >= 1)
                .expectError()
                /**
                 * verifyThenAssertThat() 사용하여 검증 이후 assertion method를 이용하서 추가 검증 가능
                 */
                .verifyThenAssertThat()
                /**
                 * 폐기 된 데이터가 있는지 검증
                 */
                .hasDiscardedElements()
                /**
                 * 어떤 데이터가 폐기되었는지 구체적 검증
                 */
                .hasDiscarded(2)
                /**
                 * 드랍 된 데이터가 있는지 검증
                 */
                .hasDroppedElements()
                .hasDropped(3, 4, 5, 6, 98, 99, 100);
    }

    @Test
    @DisplayName("Backpressure Drop 전략 검증3")
    public void generateNumberTest3() {
        StepVerifier
                .create(BackpressureExample.generateNumberByDropStrategy(), 1L)
                .thenConsumeWhile(num -> num >= 1)
                /**
                 * sequence가 모두 소진되어야만 expectComplete()를 만족한다.
                 */
                .expectComplete()
                .verifyThenAssertThat()
                .hasDiscardedElements()
                /**
                 * Backpressure DROP 전략은 Drop된 데이터가 discard된다.
                 */
                .hasDiscarded(2, 3, 4, 5, 6, 98, 99, 100);
//                .hasDropped(3, 4, 5, 6, 98, 99, 100);
    }

    @Test
    @DisplayName("emit되는 모든 데이터들을 캡쳐하여 컬렉션에 기록한 후, 기록 된 데이터들을 검증하는 예제")
    public void getCountryTest() {
        StepVerifier
                .create(RecordExample.getCountry(Flux.just("france", "russia", "greece", "poland")))
                .expectSubscription()
                /**
                 * emit된 데이터를 기록하는 세션을 시작한다. 즉, 컬렉션에 데이터를 추가한다.
                 */
                .recordWith(ArrayList::new)
                .thenConsumeWhile(country -> !country.isEmpty())
                /**
                 * recordWith로 모아진 컬렉션들을 검증
                 */
                .consumeRecordedWith(countries -> {
                    assertThat(countries, everyItem(hasLength(6)));
                    assertThat(countries
                                    .stream()
                                    .allMatch(country -> Character.isUpperCase(country.charAt(0))),
                            is(true)
                    );
                })
                /**
                 * assertThat을 사용하지 않는 방법
                 */
                .expectRecordedMatches(
                        countries ->
                                countries
                                        .stream()
                                        .allMatch(country ->
                                                Character.isUpperCase(country.charAt(0)))
                )
                .expectComplete()
                .verify();
    }
}
