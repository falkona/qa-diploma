package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class Page {

    @Getter protected String host = "http://localhost:8080";
    @Getter protected SelenideElement paymentButton = $(byText("Купить")).parent().parent();
    @Getter protected SelenideElement creditButton = $(byText("Купить в кредит")).parent().parent();
    @Getter protected SelenideElement continueButton = $(byText("Продолжить")).parent().parent();
    @Getter protected SelenideElement cardNumberField = $(byText("Номер карты")).parent();
    @Getter protected SelenideElement monthField = $(byText("Месяц")).parent();
    @Getter protected SelenideElement yearField = $(byText("Год")).parent();
    @Getter protected SelenideElement ownerField = $(byText("Владелец")).parent();
    @Getter protected SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    @Getter protected SelenideElement notificationSuccess = $(".notification_status_ok ");
    @Getter protected SelenideElement notificationError = $(".notification_status_error");

    public Page() {
        open(host);
    }

    public void commit() {
        continueButton.click();
    }

    public void fillData(String cardNumber, String month, String year, String owner, String cvc) {
        cardNumberField.$(".input__control").setValue(cardNumber);
        monthField.$(".input__control").setValue(month);
        yearField.$(".input__control").setValue(year);
        ownerField.$(".input__control").setValue(owner);
        cvcField.$(".input__control").setValue(cvc);
    }

    public PaymentPage toPaymentPage() {
        PaymentPage paymentPage = page(PaymentPage.class);
        paymentPage.paymentButton.click();
        return paymentPage;
    }

    public CreditPage toCreditPage() {
        CreditPage creditPage = page(CreditPage.class);
        creditPage.creditButton.click();
        return creditPage;
    }

    public void getCardNumberError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void getWrongMonthError() {
        monthField.$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    public void getEarlyYearError() {
        yearField.$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    public void getWrongCvcError() {
        cvcField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    public void getEmptyNameError() {
        ownerField.$(".input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void getEmptyCardError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }
}
