package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PaymentPage {

    private String host = "http://localhost:8080";
    private SelenideElement paymentButton = $(byText("Купить")).parent().parent();
    private SelenideElement continueButton = $(byText("Продолжить")).parent().parent();
    private SelenideElement cardNumberField = $(byText("Номер карты")).parent();
    private SelenideElement cardNumberInput = cardNumberField.$(".input__control");
    private SelenideElement monthField = $(byText("Месяц")).parent();
    private SelenideElement monthInput = monthField.$(".input__control");
    private SelenideElement yearField = $(byText("Год")).parent();
    private SelenideElement yearInput = yearField.$(".input__control");
    private SelenideElement ownerField = $(byText("Владелец")).parent();
    private SelenideElement ownerInput = ownerField.$(".input__control");
    private SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    private SelenideElement cvcInput = cvcField.$(".input__control");

    @Test
    public void shouldBeErrorCardNumberInvalid() {
        open(host);
        paymentButton.click();
        fillData("444411115555777", "12", "22", "Нос Дарья","999");
        continueButton.click();

        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    @Test
    public void shouldBeErrorEarlyMonth() {
        open(host);
        paymentButton.click();
        fillData("4444444444444441", "11", "19", "Нос Дарья","456");
        continueButton.click();

        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void fillData(String cardNumber, String month, String year, String owner, String cvc) {
        cardNumberInput.setValue(cardNumber);
        monthInput.setValue(month);
        yearInput.setValue(year);
        ownerInput.setValue(owner);
        cvcInput.setValue(cvc);
    }

}
