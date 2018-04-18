package model;

import java.util.Date;

public class Comment {
    private int userID;
    private int commentID;
    private String content;
    private Date commentDate;

    public Comment(int userID, int commentID, String content, Date commentDate) {
        this.userID = userID;
        this.commentID = commentID;
        this.content = content;
        this.commentDate = commentDate;
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

    public Date getCommentDate() {
        return commentDate;
    }
}
