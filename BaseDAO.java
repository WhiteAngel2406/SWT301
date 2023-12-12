package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseDAO {
    public static Connection getConnection() {

        String url = "jdbc:mysql://127.0.0.1:3306/cmslvl10_db";
        String user = "root";
        String password = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {

        Connection conn =getConnection();

        if (conn != null) {
            System.out.println("Connect successfully");
        } else {
            System.out.println("Connect failed");
        }
    }
}
