package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import java.util.Date;

/**
 * Created by ahmed on 6/23/2018.
 */

public class Forum {
    private String header;
    private String body;
    private Date date;
    private String authorUsername;
    private boolean answered;

    public Forum() {
    }

    public Forum(String header, String body, Date date, String authorUsername, boolean answered) {
        this.header = header;
        this.body = body;
        this.date = date;
        this.authorUsername = authorUsername;
        this.answered = answered;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}
