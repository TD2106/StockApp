package dao;

import dbconnection.DBConnection;
import model.Comment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentDAO {
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

}
