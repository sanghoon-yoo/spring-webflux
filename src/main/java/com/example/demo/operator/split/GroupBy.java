package com.example.demo.operator.split;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class GroupBy {
    public static void main(String[] args) {
        /**
         * GroupBy 예제
         * 저자별로 그룹화 한 도서가 모두 판매 되었을 때의 총 인세 수익을 계산한다
         * emit되는 데이터를 keyMapper로 생성한 key를 기준으로 그룹화 한 GroupedFlux를 리턴한다
         */
        Flux
                .fromIterable(Arrays.asList(
                        new Book("김영한", 12000, 50, "제네릭과 컬렉션")
                        , new Book("김영한", 10000, 70, "자바 중급 2편")
                        , new Book("장기효", 11000, 30, "React와 Vue로 배우는 TypeScript")
                        , new Book("장기효", 18000, 50, "Vue 3 중급 Composition API")
                        , new Book("Kevin", 9000, 100, "Spring Reactor")
                ))
                .groupBy(book -> book.getAuthor())
                .flatMap(groupedFlux ->
                        Mono
                                .just(groupedFlux.key())
                                .zipWith(
                                        groupedFlux
                                                .map(book -> (int) (book.getPrice() * book.getStockQuantity() * 0.1))
                                                .reduce((y1, y2) -> y1 + y2),
                                        (authorName, sumRoyalty) -> authorName + "'s royalty: " + sumRoyalty)
                )
                .subscribe(System.out::println);
    }
}

class Book {
    private String author;
    private int price;
    private int stockQuantity;

    private String bookName;

    public Book(String author, int price, int stockQuantity, String bookName) {
        this.author = author;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getBookName() {
        return bookName;
    }
}
