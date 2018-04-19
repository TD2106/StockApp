package model;

import java.util.Date;

public class Comment {
    private int userID;
    private int commentID;
    private String content;
    private String date;
    private String time;

    public Comment(int userID, int commentID, String content, String date, String time) {
        this.userID = userID;
        this.commentID = commentID;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public int getUserID() {
        return userID;
    }

    public int getCommentID() {
        return commentID;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
