package com.cloud.student;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.sql.*;
import java.io.*;
import java.net.InetSocketAddress;
public class SimpleServer {
	public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        server.createContext("/addStudent", new MyHandler());
        server.createContext("/viewStudents", new ViewHandler());
        server.createContext("/deleteStudent", new DeleteHandler());
        server.createContext("/editStudent", new EditHandler());
        server.createContext("/updateStudent", new UpdateHandler());
        server.createContext("/getData", new DataHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:9090");
    }

	static class MyHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        InputStream is = t.getRequestBody();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String data = br.readLine();

	        String name = "";
	        int marks = 0;
	        String grade = "";

	        try {
	            String[] params = data.split("&");

	            name = params[0].split("=")[1].replace("+", " ");
	            marks = Integer.parseInt(params[1].split("=")[1]);
	            grade = params[2].split("=")[1];

	            StudentDAO.insertStudent(name, marks, grade);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String response = "Student Added to Database Successfully";

	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }
	    
	}
	static class ViewHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        StringBuilder response = new StringBuilder();

	        response.append("<h2>Student List</h2>");
	        response.append("<table border='1'>");
	        response.append("<tr><th>ID</th><th>Name</th><th>Marks</th><th>Grade</th><th>Action</th></tr>");

	        try {
	            Connection con = DBConnection.getConnection();

	            String query = "SELECT * FROM students";

	            PreparedStatement ps = con.prepareStatement(query);

	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	            	response.append("<tr>");
	            	response.append("<td>" + rs.getInt("id") + "</td>");
	            	response.append("<td>" + rs.getString("name").replace("+", " ") + "</td>");
	            	response.append("<td>" + rs.getInt("marks") + "</td>");
	            	response.append("<td>" + rs.getString("grade") + "</td>");
	            	response.append("<td>");
	            	response.append("<a href='/deleteStudent?id=" + rs.getInt("id") + "'>Delete</a> ");
	            	response.append("<a href='/editStudent?id=" + rs.getInt("id") + "'>Edit</a>");
	            	response.append("</td>");
	            	response.append("</tr>"); 
	            	
	            }
	            response.append("</table>");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.toString().getBytes());
	        os.close();
	    }
	}
	static class DeleteHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        String query = t.getRequestURI().getQuery();
	        int id = Integer.parseInt(query.split("=")[1]);

	        try {
	            StudentDAO.deleteStudent(id);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String response = "Student Deleted Successfully";

	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }
	}
	static class EditHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        String query = t.getRequestURI().getQuery();
	        int id = Integer.parseInt(query.split("=")[1]);

	        StringBuilder response = new StringBuilder();

	        response.append("<h2>Edit Student</h2>");
	        response.append("<form action='/updateStudent' method='post'>");
	        response.append("<input type='hidden' name='id' value='" + id + "'>");
	        response.append("Marks: <input type='number' name='marks'><br><br>");
	        response.append("Grade: <input type='text' name='grade'><br><br>");
	        response.append("<button type='submit'>Update</button>");
	        response.append("</form>");

	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.toString().getBytes());
	        os.close();
	    }
	}
	static class UpdateHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        InputStream is = t.getRequestBody();
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String data = br.readLine();

	        try {
	            String[] params = data.split("&");

	            int id = Integer.parseInt(params[0].split("=")[1]);
	            int marks = Integer.parseInt(params[1].split("=")[1]);
	            String grade = params[2].split("=")[1];

	            StudentDAO.updateStudent(id, marks, grade);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        String response = "Student Updated Successfully";

	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }
	}
	static class DataHandler implements HttpHandler {
	    public void handle(HttpExchange t) throws IOException {

	        StringBuilder json = new StringBuilder();
	        json.append("[");

	        try {
	            Connection con = DBConnection.getConnection();

	            String query = "SELECT * FROM students";
	            PreparedStatement ps = con.prepareStatement(query);
	            ResultSet rs = ps.executeQuery();

	            boolean first = true;

	            while (rs.next()) {

	                if (!first) {
	                    json.append(",");
	                }

	                json.append("{");
	                json.append("\"name\":\"" + rs.getString("name").replace("+", " ") + "\",");
	                json.append("\"marks\":" + rs.getInt("marks"));
	                json.append("}");

	                first = false;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        json.append("]");

	        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
	        t.sendResponseHeaders(200, json.length());

	        OutputStream os = t.getResponseBody();
	        os.write(json.toString().getBytes());
	        os.close();
	    }
	}
}
