package dao;

import dbconnection.DBConnection;
import model.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static void addComment(String content, int userID) throws SQLException {
        String sqlQuery = "INSERT INTO comment(content,userid,commentdate,commenttime) VALUES (?,?,CURDATE(),TIME(NOW()))";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ps.setString(1, content);
        ps.setInt(2, userID);
        ps.execute();
    }

    public static ArrayList<Comment> getAllComments() throws SQLException {
        String sqlQuery = "SELECT commentid,userid,content,commentdate,commenttime FROM comment";
        PreparedStatement ps = connection.prepareStatement(sqlQuery);
        ResultSet rs = ps.executeQuery();
        ArrayList<Comment> comments = new ArrayList<Comment>();
        while (rs.next()) {
            comments.add(new Comment(rs.getInt("userid"), rs.getInt("commentid"), rs.getString("content")
                    , rs.getString("commentdate"), rs.getString("commenttime")));
        }
        return comments;
    }

}
