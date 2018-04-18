package dao;

import dbconnection.DBConnection;
import model.Portfolio;
import model.UserStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public static Portfolio getUsersPortfolio(int userID) throws SQLException {
        String sqlQuery = "SELECT stockname,quantity FROM userstock WHERE userid = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        ArrayList<UserStock> stocks = new ArrayList<UserStock>();
        while (rs.next()) {
            stocks.add(new UserStock(rs.getString("stockname"), rs.getInt("quantity")));
        }
        return new Portfolio(stocks);
    }
}
