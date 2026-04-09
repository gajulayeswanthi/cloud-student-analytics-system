package com.cloud.student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class StudentDAO {
	public static void insertStudent(String name, int marks, String grade) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO students(name, marks, grade) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setInt(2, marks);
            ps.setString(3, grade);

            ps.executeUpdate();

            System.out.println("Student Inserted Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void viewStudents() {

	    try {
	        Connection con = DBConnection.getConnection();

	        String query = "SELECT * FROM students";

	        PreparedStatement ps = con.prepareStatement(query);

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            int marks = rs.getInt("marks");
	            String grade = rs.getString("grade");

	            System.out.println(id + " " + name + " " + marks + " " + grade);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void updateStudent(int id, int marks, String grade) {

	    try {
	        Connection con = DBConnection.getConnection();

	        String query = "UPDATE students SET marks=?, grade=? WHERE id=?";

	        PreparedStatement ps = con.prepareStatement(query);

	        ps.setInt(1, marks);
	        ps.setString(2, grade);
	        ps.setInt(3, id);

	        ps.executeUpdate();

	        System.out.println("Student Updated Successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void deleteStudent(int id) {

	    try {
	        Connection con = DBConnection.getConnection();

	        String query = "DELETE FROM students WHERE id=?";

	        PreparedStatement ps = con.prepareStatement(query);

	        ps.setInt(1, id);

	        ps.executeUpdate();

	        System.out.println("Student Deleted Successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
