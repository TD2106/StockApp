package dao;
import dbconnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

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

    {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoginInformationCorrect(String userName,String pass) throws SQLException {
        String sqlQuery = "SELECT userid FROM user WHERE username = ? and password = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setString(1,userName);
        ps.setString(2,pass);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static boolean isUserWithNameOrEmailExists(String userName,String email) throws SQLException {
        String sqlQuery = "SELECT userid FROM user WHERE username = ? or email = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setString(1,userName);
        ps.setString(2,email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public static void addUser(String userName,String pass,String email) throws SQLException {
        String sqlQuery = "INSERT INTO user (username,email,password) VALUES (?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setString(1,userName);
        ps.setString(2,email);
        ps.setString(3,pass);
        ps.execute();
    }

    public String getUserName(int userID) throws SQLException {
        String sqlQuery = "SELECT username FROM user WHERE userid = ?";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ResultSet rs = ps.executeQuery();
        String result = "";
        while (rs.next()) {
            result = rs.getString("username");
        }
        return result;
    }
    public static void main(String[] args){
        try {
            addUser("Duy", "123", "td@gmail.com");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
