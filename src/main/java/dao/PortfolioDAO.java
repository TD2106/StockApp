package dao;

import dbconnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PortfolioDAO {
    private static Connection connection;

    static {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addNewStock(int userID,String stockName,int quantity) throws SQLException {
        String sqlQuery = "INSERT INTO userstock(userid,stockname,quantity) VALUES (?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setInt(1,userID);
        ps.setString(2,stockName);
        ps.setInt(3,quantity);
        ps.execute();
    }

    public static void editStock(int userID,String stockName,int quantity) throws SQLException {
        String sqlQuery = "UPDATE userstock SET quantity = ? WHERE userid = ? AND stockname = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setInt(1,quantity);
        ps.setInt(2,userID);
        ps.setString(3,stockName);
        ps.execute();
    }

    public static void deleteStock(int userID, String stockName) throws SQLException {
        String sqlQuery = "DELETE FROM userstock WHERE userid = ? and stockname = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setInt(1,userID);
        ps.setString(2,stockName);
        ps.execute();
    }


}
