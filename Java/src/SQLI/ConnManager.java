package src.SQLI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnManager {
    private static final String url = "jdbc:mysql://localhost:3306/db";
    private static volatile Connection connection;
    private static String user = "root";
    private static String pass = "";

    private ConnManager() {
    }

    public static Connection connect() {
        if (connection == null) {
            synchronized (ConnManager.class) {
                if (connection == null) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(url, user, pass);
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    public void disconnect() {

        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
