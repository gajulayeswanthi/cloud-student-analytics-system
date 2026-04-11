package com.cloud.student;

import java.sql.*;

public class StudentDAO {

    public static void insertStudent(String name, int marks, String grade) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;

            String sql = "INSERT INTO students(name, marks, grade) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setInt(2, marks);
            ps.setString(3, grade);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id) {
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;

            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
