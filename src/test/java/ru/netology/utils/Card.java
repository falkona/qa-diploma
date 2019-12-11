package ru.netology.utils;

import lombok.Data;

@Data
public class Card {
    private String cardNumber;
    private String month;
    private String year;
    private String owner;
    private String cvc;

    public Card() {
        cardNumber = DataGenerator.randomCardNumber();
        month = DataGenerator.anyMonth();
        year = DataGenerator.validFutureYear();
        owner = DataGenerator.name();
        cvc = DataGenerator.cvc();
    }
}
