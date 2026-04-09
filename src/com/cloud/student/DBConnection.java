package com.cloud.student;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {
    public static Connection getConnection() {
	        Connection con = null;

	        try {
	            String url = "jdbc:mysql://localhost:3306/studentDB";
	            String user = "root";
	            String password = "yeshu123";

	            con = DriverManager.getConnection(url, user, password);
	            System.out.println("Database Connected Successfully");

	        } catch (Exception e) {
	            System.out.println("Connection Failed");
	            e.printStackTrace();
	        }

	        return con;
	    }
	}   


