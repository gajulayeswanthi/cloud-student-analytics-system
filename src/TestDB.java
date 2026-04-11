import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/studentDB",
                "root",
                "yeshu123"
            );

            if (con != null) {
                System.out.println("DB CONNECTED SUCCESSFULLY");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
