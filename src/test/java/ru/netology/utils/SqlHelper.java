package ru.netology.utils;

import lombok.Getter;

import java.sql.*;

public class SqlHelper {

    public static void cleanTables() throws SQLException {
        String deleteOrderEntity = "delete from order_entity;";
        String deletePaymentEntity = "delete from payment_entity;";
        String deleteCreditEntity = "delete from credit_request_entity;";

        try (
                Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
                PreparedStatement statementOrderEntity = connection.prepareStatement(deleteOrderEntity);
                PreparedStatement statementPaymentEntity = connection.prepareStatement(deletePaymentEntity);
                PreparedStatement statementCreditEntity = connection.prepareStatement(deleteCreditEntity);
        ) {
            statementOrderEntity.executeUpdate();
            statementPaymentEntity.executeUpdate();
            statementCreditEntity.executeUpdate();
        }
    }

    public static boolean checkApprovedPayment() {
        String dataStatement = "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'APPROVED' and oe.payment_id is not NULL;";
        return executeQuery(dataStatement);
    }

    public static boolean checkDeclinedPayment() {
        String dataStatement = "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'DECLINED' and oe.payment_id is not NULL;";
        return executeQuery(dataStatement);
    }

    public static boolean checkApprovedCredit() {
        String dataStatement = "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'APPROVED' and oe.credit_id is not NULL;";
        return executeQuery(dataStatement);
    }

    public static boolean checkDeclinedCredit() {
        String dataStatement = "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'DECLINED' and oe.credit_id is not NULL;";
        return executeQuery(dataStatement);
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
        return "jdbc:mysql://192.168.99.100:3306/app";
    }


    private static String getUser() {
        return "app";
    }


    private static String getPassword() {
        return "pass";
    }
}
