package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;


/**
 * Created by ahmed on 6/23/2018.
 */

public class Forum implements Serializable {
    @SerializedName("post_id") private int postID;
    @SerializedName("post_title") private String header;
    @SerializedName("post_body") private String body;
    @SerializedName("date_published") private String dateString;
    private Date date;
    @SerializedName("author_username") private String authorUsername;
    @SerializedName("answered") private int answered;

    public Forum() {
        this.date = new Date();
    }

    public Forum(String header, String body, Date date, String authorUsername, int answered) {
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
        try {
            this.date = DateUtils.convert(this.dateString);
        } catch (ParseException e) {
            Log.e("Parse Exception", e.toString());
        }
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

    public int isAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getConvertedDate() {
        return DateUtils.convertForumPost(getDate());
    }
}
