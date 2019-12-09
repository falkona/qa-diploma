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

    @DisplayName("1. ������ �� �����: �������� �������� + 8. �������� ����� �����, ��� (���� ����� �������)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearPayment() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "08", "22", "��� �����","456");
        paymentPage.commit();
        paymentPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedPayment());
    }

    @DisplayName("2. ������ �� �����: ���������� ��������")
    @Test
    public void shouldBeDeclinedValidCardPayment() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444442", "12", "22", "��� ����� ����������","905");
        paymentPage.commit();
        paymentPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedPayment());
    }

    @DisplayName("3. ������ � ������: �������� �������� + 8. �������� ����� �����, ��� (���� ����� �������)")
    @Test
    public void shouldBeSuccessCurrentMonthAndYearCredit() {
        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData("4444444444444441", "12", "19", "��� �����","456");
        creditPage.commit();
        creditPage.checkOperationApproved();

        assertTrue(SqlHelper.checkApprovedCredit());
    }

    @DisplayName("4. ������ � ������: ���������� ��������")
    @Test
    public void shouldBeDeclinedValidCardCredit() {
        Page startPage = new Page();
        CreditPage creditPage = startPage.toCreditPage();
        creditPage.fillData("4444444444444442", "12", "22", "��� ����� ����������","905");
        creditPage.commit();
        creditPage.checkOperationDeclined();

        assertTrue(SqlHelper.checkDeclinedCredit());
    }

    @DisplayName("5. �������� ���� ����� �����")
    @Test
    public void shouldBeErrorCardNumberInvalid() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("444411115555777", "12", "22", "��� �����","999");
        paymentPage.commit();
        paymentPage.getCardNumberError();
    }

    @DisplayName("6. �������� ���� ����� (���� ������ �������)")
    @Test
    public void shouldBeErrorEarlyMonth() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "11", "19", "��� �����","999");
        paymentPage.commit();
        paymentPage.getWrongMonthError();
    }

    @DisplayName("7. �������� ���� ��� (���� ������ �������)")
    @Test
    public void shouldBeErrorEarlyYear() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "18", "��� �����","456");
        paymentPage.commit();
        paymentPage.getEarlyYearError();
    }

    @DisplayName("9. �������� ���� cvc")
    @Test
    public void shouldBeErrorShortCvc() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "22", "��� �����","45");
        paymentPage.commit();
        paymentPage.getWrongCvcError();
    }

    @DisplayName("10. �������� ���� ��� (�� ���������)")
    @Test
    public void shouldBeErrorEmptyName() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("4444444444444441", "12", "22", "","451");
        paymentPage.commit();
        paymentPage.getEmptyNameError();
    }

    @DisplayName("11. �������� ���� ����� ����� (�� ���������)")
    @Test
    public void shouldBeErrorEmptyCard() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("", "12", "22", "�����","452");
        paymentPage.commit();
        paymentPage.getEmptyCardError();
    }

    @DisplayName("12. �������� ������� � �������������� ����� - UI")
    @Test
    public void shouldBeErrorCardDoesNotExist() {
        Page startPage = new Page();
        PaymentPage paymentPage = startPage.toPaymentPage();
        paymentPage.fillData("1234123400000000", "12", "22", "�����","452");
        paymentPage.commit();
        paymentPage.checkOperationDeclined();
    }

    @DisplayName("13. �������� ������� � �������������� ����� - REST")
    @Test
    public void shouldBeErrorCardDoesNotExistRest() {
        String request = "{\n" +
                "           \"number\":\"4444 4444 4444 4443\",\n" +
                "           \"year\":\"22\",\n" +
                "           \"month\":\"08\",\n" +
                "           \"holder\":\"��� �����\",\n" +
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
