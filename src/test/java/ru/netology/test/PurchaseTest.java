package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import ru.netology.pages.PaymentPage;

public class PurchaseTest {

    @Test
    public void shouldBeErrorCardNumberInvalid() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("444411115555777", "12", "22", "Нос Дарья","999");
        paymentPage.commit();
        paymentPage.getCardNumberField().$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    @Test
    public void shouldBeErrorEarlyMonth() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("4444444444444441", "11", "19", "Нос Дарья","999");
        paymentPage.commit();
        paymentPage.getMonthField().$(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
    }

    @Test
    public void shouldBeErrorEarlyYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("4444444444444441", "12", "18", "Нос Дарья","456");
        paymentPage.commit();
        paymentPage.getYearField().$(".input__sub").shouldHave(Condition.exactText("Истёк срок действия карты"));
    }

    @Test
    public void shouldBeSuccessCurrentMonthAndYear() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("4444444444444441", "12", "19", "Нос Дарья","456");
        paymentPage.commit();
        paymentPage.getNotificationSuccess().waitUntil(Condition.visible, 15000);
    }

    @Test
    public void shouldBeErrorShortCvc() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("4444444444444441", "12", "22", "Нос Дарья","45");
        paymentPage.commit();
        paymentPage.getCvcField().$(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
    }

    @Test
    public void shouldBeApprovedValidCardRest() {
        // Rest api
    }

    @Test
    public void shouldBeDeclinedValidCardRest() {
        // Rest api
    }

    @Test
    public void shouldBeDeclinedValidCard() {
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.fillData("4444444444444442", "12", "22", "Нос Дарья Дмитриевна","905");
        paymentPage.commit();
        paymentPage.getNotificationError().waitUntil(Condition.visible, 15000);
    }


}
