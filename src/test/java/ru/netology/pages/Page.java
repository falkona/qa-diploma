package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import ru.netology.utils.Card;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class Page {

    @Getter protected String host = "http://localhost:8080";
    @Getter protected SelenideElement paymentButton = $(byText("������")).parent().parent();
    @Getter protected SelenideElement creditButton = $(byText("������ � ������")).parent().parent();
    @Getter protected SelenideElement continueButton = $(byText("����������")).parent().parent();
    @Getter protected SelenideElement cardNumberField = $(byText("����� �����")).parent();
    @Getter protected SelenideElement monthField = $(byText("�����")).parent();
    @Getter protected SelenideElement yearField = $(byText("���")).parent();
    @Getter protected SelenideElement ownerField = $(byText("��������")).parent();
    @Getter protected SelenideElement cvcField = $(byText("CVC/CVV")).parent();
    @Getter protected SelenideElement notificationSuccess = $(".notification_status_ok ");
    @Getter protected SelenideElement notificationError = $(".notification_status_error");

    public Page() {
        open(host);
    }

    public void commit() {
        continueButton.click();
    }

    public void fillData(Card card) {
        cardNumberField.$(".input__control").setValue(card.getCardNumber());
        monthField.$(".input__control").setValue(card.getMonth());
        yearField.$(".input__control").setValue(card.getYear());
        ownerField.$(".input__control").setValue(card.getOwner());
        cvcField.$(".input__control").setValue(card.getCvc());
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

    public void checkCardNumberError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("�������� ������"));
    }

    public void checkWrongMonthError() {
        monthField.$(".input__sub").shouldHave(Condition.exactText("������� ������ ���� �������� �����"));
    }

    public void checkEarlyYearError() {
        yearField.$(".input__sub").shouldHave(Condition.exactText("���� ���� �������� �����"));
    }

    public void checkWrongCvcError() {
        cvcField.$(".input__sub").shouldHave(Condition.exactText("�������� ������"));
    }

    public void checkEmptyNameError() {
        ownerField.$(".input__sub").shouldHave(Condition.exactText("���� ����������� ��� ����������"));
    }

    public void checkEmptyCardError() {
        cardNumberField.$(".input__sub").shouldHave(Condition.exactText("�������� ������"));
    }
}
