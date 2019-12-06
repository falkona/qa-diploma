package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Data
public class PaymentPage {

    private String host = "http://localhost:8080";
    @Getter private SelenideElement paymentButton = $(byText("Купить")).parent().parent();
    @Getter private SelenideElement continueButton = $(byText("Продолжить")).parent().parent();
    @Getter private SelenideElement cardNumberField = $(byText("Номер карты")).parent();
    @Getter private SelenideElement monthField = $(byText("Месяц")).parent();
    @Getter private SelenideElement yearField = $(byText("Год")).parent();
    @Getter private SelenideElement ownerField = $(byText("Владелец")).parent();
    @Getter private SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    @Getter private SelenideElement notificationSuccess = $(".notification_status_ok ");
    @Getter private SelenideElement notificationError = $(".notification_status_error");

    public PaymentPage() {
        open(host);
        paymentButton.click();
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

}
