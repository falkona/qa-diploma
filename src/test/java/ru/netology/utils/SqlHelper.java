package ru.netology.utils;

import java.sql.*;

public class SqlHelper {

    public static void cleanTables() throws SQLException {
        String deleteOrderEntity = "delete from order_entity;";
        String deletePaymentEntity = "delete from payment_entity;";
        String deleteCreditEntity = "delete from credit_request_entity;";

        try (
                Connection connectionMysql = DriverManager.getConnection(getUrlMySQL(), getUser(), getPassword());
                Connection connectionPostgres = DriverManager.getConnection(getUrlPostgers(), getUser(), getPassword());

                PreparedStatement statementOrderEntity = connectionMysql.prepareStatement(deleteOrderEntity);
                PreparedStatement statementPaymentEntity = connectionMysql.prepareStatement(deletePaymentEntity);
                PreparedStatement statementCreditEntity = connectionMysql.prepareStatement(deleteCreditEntity);

                PreparedStatement statementOrderEntityPg = connectionPostgres.prepareStatement(deleteOrderEntity);
                PreparedStatement statementPaymentEntityPg = connectionPostgres.prepareStatement(deletePaymentEntity);
                PreparedStatement statementCreditEntityPg = connectionPostgres.prepareStatement(deleteCreditEntity);
        ) {
            statementOrderEntity.executeUpdate();
            statementPaymentEntity.executeUpdate();
            statementCreditEntity.executeUpdate();

            statementOrderEntityPg.executeUpdate();
            statementPaymentEntityPg.executeUpdate();
            statementCreditEntityPg.executeUpdate();
        }
    }

    public static String approvedPaymentStatement() {
        return "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'APPROVED' and oe.payment_id is not NULL and pe.amount = 4500000;";
    }

    public static boolean checkApprovedPaymentMySQL() {
        return executeQueryMySQL(approvedPaymentStatement());
    }

    public static boolean checkApprovedPaymentPostgres() {
        return executeQueryPostgres(approvedPaymentStatement());
    }

    public static String declinedPaymentStatement() {
        return "select pe.*, oe.* from payment_entity as pe " +
                "left join order_entity as oe " +
                "on pe. transaction_id = oe.payment_id " +
                "where pe.status = 'DECLINED' and oe.payment_id is not NULL and pe.amount = 4500000;";
    }

    public static boolean checkDeclinedPaymentMySQL() {
        return executeQueryMySQL(declinedPaymentStatement());
    }

    public static boolean checkDeclinedPaymentPostgres() {
        return executeQueryPostgres(declinedPaymentStatement());
    }

    public static String approvedCreditStatement() {
        return "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'APPROVED' and oe.credit_id is not NULL;";
    }

    public static boolean checkApprovedCreditMySQL() {
        return executeQueryMySQL(approvedCreditStatement());
    }

    public static boolean checkApprovedCreditPostgres() {
        return executeQueryPostgres(approvedCreditStatement());
    }

    public static String declinedCreditStatement() {
        return "select ce.*, oe.* from credit_request_entity as ce " +
                "left join order_entity as oe " +
                "on ce. bank_id = oe.credit_id " +
                "where ce.status = 'DECLINED' and oe.credit_id is not NULL;";
    }

    public static boolean checkDeclinedCreditMySQL() {
        return executeQueryMySQL(declinedCreditStatement());
    }

    public static boolean checkDeclinedCreditPostgres() {
        return executeQueryPostgres(declinedCreditStatement());
    }

    private static  boolean executeQueryMySQL(String dataStatement) {
        boolean result = false;
        try {
            Connection connection = DriverManager.getConnection(getUrlMySQL(), getUser(), getPassword());
            PreparedStatement statement = connection.prepareStatement(dataStatement);
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();

        } catch(SQLException exception) {
            exception.getErrorCode();
        }
        return result;
    }

    private static  boolean executeQueryPostgres(String dataStatement) {
        boolean result = false;
        try {
            Connection connection = DriverManager.getConnection(getUrlPostgers(), getUser(), getPassword());
            PreparedStatement statement = connection.prepareStatement(dataStatement);
            ResultSet resultSet = statement.executeQuery();
            result = resultSet.next();

        } catch(SQLException exception) {
            exception.getErrorCode();
        }
        return result;
    }

    private static String getUrlPostgers() {
        return "jdbc:postgresql://192.168.99.100:5432/app";
    }

    private static String getUrlMySQL() {
        return "jdbc:mysql://192.168.99.100:3306/app";
    }

    private static String getUser() {
        return "app";
    }

    private static String getPassword() {
        return "pass";
    }
}
