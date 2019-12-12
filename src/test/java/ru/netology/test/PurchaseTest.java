package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.pages.CreditPage;
import ru.netology.pages.Page;
import ru.netology.pages.PaymentPage;
import ru.netology.utils.Card;
import ru.netology.utils.DataGenerator;
import ru.netology.utils.SqlHelper;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseTest {

    @DisplayName("1. Оплата по карте: успешная операция + 8. Проверка полей Месяц, Год (дата равна текущей)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearPayment() {
        Card approvedCard = new Card();
        approvedCard.setCardNumber(DataGenerator.approvedCardNumber());
        approvedCard.setMonth(DataGenerator.currentMonth());
        approvedCard.setYear(DataGenerator.currentYear());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(approvedCard);
        paymentPage.commit();
        paymentPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedPayment(), "Строка не найдена в БД");
    }

    @DisplayName("2. Оплата по карте: неуспешная операция")
    @Test
    public void shouldBeDeclinedValidCardPayment() {
        Card declinedCard = new Card();
        declinedCard.setCardNumber(DataGenerator.declinedCardNumber());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(declinedCard);
        paymentPage.commit();
        paymentPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedPayment(), "Строка не найдена в БД");
    }

    @DisplayName("3. Оплата в кредит: успешная операция + 8. Проверка полей Месяц, Год (дата равна текущей)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearCredit() {
        Card approvedCard = new Card();
        approvedCard.setCardNumber(DataGenerator.approvedCardNumber());
        approvedCard.setMonth(DataGenerator.currentMonth());
        approvedCard.setYear(DataGenerator.currentYear());

        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData(approvedCard);
        creditPage.commit();
        creditPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedCredit(), "Строка не найдена в БД");
    }

    @DisplayName("4. Оплата в кредит: неуспешная операция")
    @Test
    public void shouldBeDeclinedValidCardCredit() {
        Card declinedCard = new Card();
        declinedCard.setCardNumber(DataGenerator.declinedCardNumber());;

        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData(declinedCard);
        creditPage.commit();
        creditPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedCredit(), "Строка не найдена");
    }

    @DisplayName("5. Проверка поля Номер карты")
    @Test
    public void shouldBeErrorCardNumberInvalid() {
        Card invalidCard = new Card();
        invalidCard.setCardNumber(DataGenerator.invalidCardNumber());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkCardNumberError();
    }

    @DisplayName("6. Проверка поля Месяц (дата меньше текущей)")
    @Test
    public void shouldBeErrorEarlyMonth() {
        Card invalidCard = new Card();
        invalidCard.setMonth(DataGenerator.pastMonth());
        invalidCard.setYear(DataGenerator.currentYear());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkWrongMonthError();
    }

    @DisplayName("7. Проверка поля Год (дата меньше текущей)")
    @Test
    public void shouldBeErrorEarlyYear() {
        Card invalidCard = new Card();
        invalidCard.setYear(DataGenerator.pastYear());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkEarlyYearError();
    }

    @DisplayName("9. Проверка поля cvc")
    @Test
    public void shouldBeErrorShortCvc() {
        Card invalidCard = new Card();
        invalidCard.setCvc(DataGenerator.shortCvc());

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkWrongCvcError();
    }

    @DisplayName("10. Проверка поля Имя (не заполнено)")
    @Test
    public void shouldBeErrorEmptyName() {
        Card invalidCard = new Card();
        invalidCard.setOwner("");

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkEmptyNameError();
    }

    @DisplayName("11. Проверка поля Номер карты (не заполнено)")
    @Test
    public void shouldBeErrorEmptyCard() {
        Card invalidCard = new Card();
        invalidCard.setCardNumber("");

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkEmptyCardError();
    }

    @DisplayName("12. Проверка покупки с несуществующей карты - UI")
    @Test
    public void shouldBeErrorCardDoesNotExist() {
        Card invalidCard = new Card();

        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData(invalidCard);
        paymentPage.commit();
        paymentPage.checkOperationDeclined();
    }

    @AfterEach
    public void cleanTables() throws SQLException{
        SqlHelper.cleanTables();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
}
