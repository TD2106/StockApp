/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbconnection;
import java.sql.*;
/**
 *
 * @author Duy Le
 */
public class DBConnection {
    private static Connection dbConnection;
    private static String connectionURL = "jdbc:mysql://localhost:3306/StockApp";
    private static String user = "root";
    private static String password = "lutden";

    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        if(dbConnection == null){
            dbConnection = DriverManager.getConnection(connectionURL, user, password);
        }
        return dbConnection;
    }



}
