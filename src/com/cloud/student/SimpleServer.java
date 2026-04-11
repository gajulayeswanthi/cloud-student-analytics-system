package com.cloud.student;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.nio.file.Files;
import java.io.File;

public class SimpleServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        server.createContext("/", new HomeHandler());
        server.createContext("/addStudent", new MyHandler());
        server.createContext("/viewStudents", new ViewHandler());
        server.createContext("/deleteStudent", new DeleteHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:9090");
    }

  
    static class HomeHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            String path = t.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            File file = new File("/home/ec2-user/CloudStudentSystem/StudentFrontend" + path);

            if (!file.exists()) {
                String res = "404 Not Found";
                t.sendResponseHeaders(404, res.length());
                OutputStream os = t.getResponseBody();
                os.write(res.getBytes());
                os.close();
                return;
            }

            byte[] data = Files.readAllBytes(file.toPath());

            t.getResponseHeaders().add("Content-Type", "text/html");
            t.sendResponseHeaders(200, data.length);
            OutputStream os = t.getResponseBody();
            os.write(data);
            os.close();
        }
    }

 
    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            try {
                String data = new String(t.getRequestBody().readAllBytes());

                String[] params = data.split("&");

                String name = params[0].split("=")[1].replace("+", " ");
                int marks = Integer.parseInt(params[1].split("=")[1]);
                String grade = params[2].split("=")[1];

                Connection con = DBConnection.getConnection();

                String sql = "INSERT INTO students(name, marks, grade) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setInt(2, marks);
                ps.setString(3, grade);

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

            t.getResponseHeaders().add("Location", "/viewStudents");
            t.sendResponseHeaders(302, -1);
            t.getResponseBody().write(new byte[0]);
             t.getResponseBody().close();
        }
    }
        static class ViewHandler implements HttpHandler {
    public void handle(HttpExchange t) throws IOException {

        StringBuilder sb = new StringBuilder();

        try {
            Connection con = DBConnection.getConnection();

            sb.append("<h2>Student List</h2>");
            sb.append("<table border='1'>");
            sb.append("<tr><th>ID</th><th>Name</th><th>Marks</th><th>Grade</th><th>Action</th></tr>");

            String sql = "SELECT * FROM students";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                sb.append("<tr>");
                sb.append("<td>").append(rs.getInt("id")).append("</td>");
                sb.append("<td>").append(rs.getString("name")).append("</td>");
                sb.append("<td>").append(rs.getInt("marks")).append("</td>");
                sb.append("<td>").append(rs.getString("grade")).append("</td>");
                sb.append("<td><a href='/deleteStudent?id=")
                        .append(rs.getInt("id"))
                        .append("'>Delete</a></td>");
                sb.append("</tr>");
            }

            if (!found) {
                sb.append("<tr><td colspan='5'>NO DATA FOUND</td></tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            sb.append("<tr><td colspan='5'>ERROR: ")
                    .append(e.getMessage())
                    .append("</td></tr>");
        }

        sb.append("</table>");

        byte[] res = sb.toString().getBytes();

        t.getResponseHeaders().add("Content-Type", "text/html");
        t.sendResponseHeaders(200, res.length);
        t.getResponseBody().write(res);
        t.getResponseBody().close();
    }
}

    static class DeleteHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            try {
                String query = t.getRequestURI().getQuery();
                int id = Integer.parseInt(query.split("=")[1]);

                Connection con = DBConnection.getConnection();

                String sql = "DELETE FROM students WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

            t.getResponseHeaders().add("Location", "/viewStudents");
            t.sendResponseHeaders(302, -1);
            t.close();
        }
    }
}
