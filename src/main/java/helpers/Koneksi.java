package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection(){
        Connection connection = null;
        try {
            System.out.println("Connecting..");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/daycare",
                    "root",
                    ""
            );
            System.out.println("Connected!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("Class tidak ditemukan");
        } catch (SQLException e) {
            System.err.println("Nama database salah");
        }
        return null;
    }
}
