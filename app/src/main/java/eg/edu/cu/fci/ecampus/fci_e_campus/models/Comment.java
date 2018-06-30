package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;

/**
 * Created by ahmed on 6/23/2018.
 */

public class Comment {
    @SerializedName("comment_id")
    private int id;
    @SerializedName("comment_text")
    private String text;
    @SerializedName("comment_time")
    private String dateString;
    private Date date;
    @SerializedName("author_username")
    private String authorUsername;

    public Comment() {
    }

    public Comment(int id, String text, Date date, String authorUsername) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.authorUsername = authorUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getConvertedDate() {
        return DateUtils.convertPostComment(getDate());
    }
}
