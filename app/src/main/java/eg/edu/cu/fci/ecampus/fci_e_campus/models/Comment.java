package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import java.util.Date;

/**
 * Created by ahmed on 6/23/2018.
 */

public class Comment {
    private String text;
    private Date date;
    private String authorUsername;

    public Comment() {
    }

    public Comment(String text, Date date, String authorUsername) {
        this.text = text;
        this.date = date;
        this.authorUsername = authorUsername;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
}
