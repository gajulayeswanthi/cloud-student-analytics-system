package com.cloud.student;
import java.sql.Connection;
public class MainApp {
	public static void main(String[] args) {

		StudentDAO.insertStudent("Ravi", 85, "A");
		StudentDAO.insertStudent("Sita", 90, "A");
       
        
        StudentDAO.viewStudents();
        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Connection Working");
        } else {
            System.out.println("Connection Not Working");
        }
    }
}
