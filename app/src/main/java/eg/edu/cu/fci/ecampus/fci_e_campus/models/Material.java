package eg.edu.cu.fci.ecampus.fci_e_campus.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.util.Date;

import eg.edu.cu.fci.ecampus.fci_e_campus.utils.DateUtils;

/**
 * Created by ahmed on 6/21/2018.
 */

public class Material {
    @SerializedName("MATERIALNAME") private String name;
    @SerializedName("MATERIALDESCRIPTION") private String description;
    @SerializedName("MATERIALFILEPATH") private String link;
    @SerializedName("UPLOADERUSERNAME") private String uploaderUsername;
    @SerializedName("DATEADDED") private String dateString;
    private Date date;
    @SerializedName("MATERIALTYPE") private String type;

    public Material(String name, String description, String link, String uploaderUsername, String uploaderType, Date date, String type) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.uploaderUsername = uploaderUsername;
        this.date = date;
        this.type = type;
    }

    public Material() {
        this.date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public void setUploaderUsername(String uploaderUsername) {
        this.uploaderUsername = uploaderUsername;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTime(){
        return DateUtils.convertCreatedDate(getDate());
    }

}
