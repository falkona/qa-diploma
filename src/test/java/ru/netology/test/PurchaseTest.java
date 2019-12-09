package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.pages.CreditPage;
import ru.netology.pages.Page;
import ru.netology.pages.PaymentPage;
import ru.netology.utils.SqlHelper;

import java.sql.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseTest {

    @DisplayName("1. Оплата по карте: успешная операция + 8. Проверка полей Месяц, Год (дата равна текущей)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearPayment() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "08", "22", "Нос Дарья","456");
        paymentPage.commit();
        paymentPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedPayment());
    }

    @DisplayName("2. Оплата по карте: неуспешная операция")
    @Test
    public void shouldBeDeclinedValidCardPayment() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444442", "12", "22", "Нос Дарья Дмитриевна","905");
        paymentPage.commit();
        paymentPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedPayment());
    }

    @DisplayName("3. Оплата в кредит: успешная операция + 8. Проверка полей Месяц, Год (дата равна текущей)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearCredit() {
        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData("4444444444444441", "12", "19", "Нос Дарья","456");
        creditPage.commit();
        creditPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedCredit());
    }

    @DisplayName("4. Оплата в кредит: неуспешная операция")
    @Test
    public void shouldBeDeclinedValidCardCredit() {
        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData("4444444444444442", "12", "22", "Нос Дарья Дмитриевна","905");
        creditPage.commit();
        creditPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedCredit());
    }

    @DisplayName("5. Проверка поля Номер карты")
    @Test
    public void shouldBeErrorCardNumberInvalid() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("444411115555777", "12", "22", "Нос Дарья","999");
        paymentPage.commit();
        paymentPage.getCardNumberError();
    }

    @DisplayName("6. Проверка поля Месяц (дата меньше текущей)")
    @Test
    public void shouldBeErrorEarlyMonth() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "11", "19", "Нос Дарья","999");
        paymentPage.commit();
        paymentPage.getWrongMonthError();
    }

    @DisplayName("7. Проверка поля Год (дата меньше текущей)")
    @Test
    public void shouldBeErrorEarlyYear() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "18", "Нос Дарья","456");
        paymentPage.commit();
        paymentPage.getEarlyYearError();
    }

    @DisplayName("9. Проверка поля cvc")
    @Test
    public void shouldBeErrorShortCvc() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "22", "Нос Дарья","45");
        paymentPage.commit();
        paymentPage.getWrongCvcError();
    }

    @DisplayName("10. Проверка поля Имя (не заполнено)")
    @Test
    public void shouldBeErrorEmptyName() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "22", "","451");
        paymentPage.commit();
        paymentPage.getEmptyNameError();
    }

    @DisplayName("11. Проверка поля Номер карты (не заполнено)")
    @Test
    public void shouldBeErrorEmptyCard() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("", "12", "22", "Дарья","452");
        paymentPage.commit();
        paymentPage.getEmptyCardError();
    }

    @DisplayName("12. Проверка покупки с несуществующей карты - UI")
    @Test
    public void shouldBeErrorCardDoesNotExist() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("1234123400000000", "12", "22", "Дарья","452");
        paymentPage.commit();
        paymentPage.checkOperationDeclined();
    }

    @DisplayName("13. Проверка покупки с несуществующей карты - REST")
    @Test
    public void shouldBeErrorCardDoesNotExistRest() {
        String request = "{\n" +
                "           \"number\":\"4444 4444 4444 4443\",\n" +
                "           \"year\":\"22\",\n" +
                "           \"month\":\"08\",\n" +
                "           \"holder\":\"Нос Дарья\",\n" +
                "           \"cvc\":\"999\"}";

        given().
                header("Content-Type", "application/json").
                body(request).
        when().
                post("http://localhost:8080/api/v1/credit").
        then().
                statusCode(500).
                body("message", equalTo("400 Bad Request"));
    }

    @AfterEach
    public void cleanTables() throws SQLException{
        SqlHelper.cleanTables();
    }
}
