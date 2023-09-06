package com.bmw.seckill.common.util.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class InsertTest {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/bmw_seckill?useSSL=true&charset=utf8mb4&serverTimezone=Hongkong"; // 修改为你的数据库连接URL
        String username = "root"; // 修改为你的数据库用户名
        String password = "wxm147369"; // 修改为你的数据库密码

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            String insertSql = "INSERT INTO seckill_user (id, name, phone, create_time, is_deleted) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                int startingId = 201;

                for (int i = startingId; i <= startingId + 999; i++) {
                    preparedStatement.setLong(1, i);
                    preparedStatement.setString(2, "User" + i);
                    preparedStatement.setString(3, "123456789" + (i - startingId));
                    preparedStatement.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
                    preparedStatement.setInt(5, 0);
                    preparedStatement.addBatch();
                }

                int[] affectedRows = preparedStatement.executeBatch();
                System.out.println("Inserted " + affectedRows.length + " rows.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}