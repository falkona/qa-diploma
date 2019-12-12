package ru.netology.utils;

import java.sql.*;

public class SqlHelper {

    public static void cleanTables() throws SQLException {
        String deleteOrderEntity = "delete from order_entity;";
        String deletePaymentEntity = "delete from payment_entity;";
        String deleteCreditEntity = "delete from credit_request_entity;";

        try (
                Connection connectionMysql = DriverManager.getConnection(getUrl(), getUser(), getPassword());

                PreparedStatement statementOrderEntity = connectionMysql.prepareStatement(deleteOrderEntity);
                PreparedStatement statementPaymentEntity = connectionMysql.prepareStatement(deletePaymentEntity);
                PreparedStatement statementCreditEntity = connectionMysql.prepareStatement(deleteCreditEntity);
        ) {
            statementOrderEntity.executeUpdate();
            statementPaymentEntity.executeUpdate();
            statementCreditEntity.executeUpdate();
        }
    }

    public static String approvedPaymentStatement() {
        return "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'APPROVED' and oe.payment_id is not NULL and pe.amount = 4500000;";
    }

    public static boolean checkApprovedPayment() {
        return executeQuery(approvedPaymentStatement());
    }

    public static String declinedPaymentStatement() {
        return "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'DECLINED' and oe.payment_id is not NULL and pe.amount = 4500000;";
    }

    public static boolean checkDeclinedPayment() {
        return executeQuery(declinedPaymentStatement());
    }

    public static String approvedCreditStatement() {
        return "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'APPROVED' and oe.credit_id is not NULL;";
    }

    public static boolean checkApprovedCredit() {
        return executeQuery(approvedCreditStatement());
    }

    public static String declinedCreditStatement() {
        return "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'DECLINED' and oe.credit_id is not NULL;";
    }

    public static boolean checkDeclinedCredit() {
        return executeQuery(declinedCreditStatement());
    }

    private static  boolean executeQuery(String dataStatement) {
        boolean result = false;
        try {
            Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            PreparedStatement statement = connection.prepareStatement(dataStatement);
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();

        } catch(SQLException exception) {
            exception.getErrorCode();
        }
        return result;
    }

    private static String getUrl() {
        return System.getProperty("test.db.url");
    }

    private static String getUser() {
        return "app";
    }

    private static String getPassword() {
        return "pass";
    }
}
