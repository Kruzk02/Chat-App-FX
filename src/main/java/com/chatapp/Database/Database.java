package com.chatapp.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static String DB_Driver = "com.mysql.cj.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost:3306/chatapp";
    private static String DB_User = "root";
    private static String DB_Password = "Password";

    public static Connection getDBConnection(){
        Connection connection = null;
        try{
            Class.forName(DB_Driver);
            connection = DriverManager.getConnection(DB_URL,DB_User,DB_Password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
